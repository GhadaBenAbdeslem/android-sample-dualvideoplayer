/**
 * Copyright (c) 2014-2016, Digi International Inc. <support@digi.com>
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.digi.android.sample.dualvideoplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Dual Video Player sample
 *
 * <p>This application demonstrates how to reproduce and manage videos in the
 * android platform using two different displays.</p>
 *
 * <p>For a complete description on the example, refer to the 'README.md' file
 * included in the example directory.</p>
 */
public class VideoPlayerActivity extends ListActivity implements OnCompletionListener, DisplayListener {

	// Constants.
	private static final int SCREEN_STATUS_NORMAL = 0;
	private static final int SCREEN_STATUS_FULL = 1;
	
	// Variables.

	// TextView that shows the selected source folder for the videos.
	private TextView pathText;
	
	// VideoView where the video will be played.
	private VideoView video;
	private VideoView fullscreenVideo;

	private ImageButton normalscreenButton;

	// List of video files contained in the selected path.
	private ArrayList<String> videos;
	// List of folders.
	private ArrayList<String> folders;
	
	// ListView which launched the onListItemClick() event.
	private ListView videosListView;
	// ListView for folders.
	private ListView folderListView;

	// Black background for full screen.
	private LinearLayout blackBackground;
	
	// Index of the selected video of the list.
	private int selectedVideoIndex = -1;
	// Screen status.
	private int screenStatus = SCREEN_STATUS_NORMAL;

	// Adapters for the lists.
	private VideoListAdapter videosAdapter;
	private FolderListAdapter foldersAdapter;
	
	// Object used to control video preview.
	private MediaController ctlr;

	// Current folder.
	private File currentFolder;
	
	// Display Manager service.
	private DisplayManager displayManager;
	
	// Secondary display controller.
	private SecondaryVideoPlayer secondaryVideoPlayer;
	
	private final Object videosLock = new Object();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.main);

		// Initialize variables.
		folders = new ArrayList<String>();
		videos = new ArrayList<String>();
		videosAdapter = new VideoListAdapter(this, R.layout.video_item, this.videos);
		foldersAdapter = new FolderListAdapter(this, R.layout.explorer_item, folders);
		ctlr = new MediaController(this);
		
		// Find and instance UI components.
		initializeUIElements();
		
		// Assign values.
		setListAdapter(videosAdapter);
		folderListView.setAdapter(foldersAdapter);
		currentFolder = Environment.getExternalStorageDirectory();
		ctlr.setMediaPlayer(video);
		video.setMediaController(ctlr);
		fullscreenVideo.setMediaController(ctlr);
		pathText.setText(currentFolder.toString());
		ctlr.setAnchorView(findViewById(R.id.video_container));
		
		// Get managers.
		displayManager = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);
		// Register for display events.
		displayManager.registerDisplayListener(this, null);
		
		// Update folder list.
		updateFolderList();
	}
	
	/**
	 * Initializes all the UI elements used in the application and assigns the
	 * corresponding listeners.
	 */
	private void initializeUIElements() {
		pathText = (TextView)this.findViewById(R.id.path);
		ImageButton fullscreenButton = (ImageButton) this.findViewById(R.id.fullscreen);
		fullscreenButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				handleFullScreenButtonPressed();
			}
		});
		normalscreenButton = (ImageButton)this.findViewById(R.id.normalscreen);
		normalscreenButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				handleNormalScreenButtonPressed();
			}
		});
		video = (VideoView)this.findViewById(R.id.video_view);
		video.setOnCompletionListener(this);
		fullscreenVideo = (VideoView)this.findViewById(R.id.fullscreen_video_view);
		fullscreenVideo.setOnCompletionListener(this);
		folderListView = (ListView)findViewById(R.id.folder_list);
		folderListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 > 0)
					currentFolder = new File(currentFolder, folders.get(arg2));
				else {
					if (currentFolder.getParentFile() != null)
						currentFolder = currentFolder.getParentFile();
				}
				video.stopPlayback();
				video.setEnabled(false);
				selectedVideoIndex = -1;
				updateFolderList();
			}
		});
		videosListView = getListView();
		blackBackground = (LinearLayout)findViewById(R.id.black_background);
	}

	/**
	 * Fills the ListView with the video files in the selected folder.
	 */
	public void updateVideoList() {
		clearVideoList();
		if (currentFolder.exists() && currentFolder.listFiles() != null &&
				currentFolder.listFiles(new VideoFilter()).length > 0) {
			for (File file : currentFolder.listFiles(new VideoFilter()))
				videos.add(file.getName());
			videosAdapter.sort(new Comparator<String>() {
				public int compare(String object1, String object2) {
					return object1.compareTo(object2);
				}
			});
			videosAdapter.notifyDataSetChanged();
			onListItemClick(videosListView, null, 0, 0);
		}
	}

	@Override
	public void onDisplayAdded(int displayId) {
		startVideoPlayerPresentation();
	}

	@Override
	public void onDisplayChanged(int displayId) {
		// Do nothing.
	}

	@Override
	public void onDisplayRemoved(int displayId) {
		secondaryVideoPlayer.dismiss();
		secondaryVideoPlayer = null;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if (selectedVideoIndex == videos.size() - 1)
			onListItemClick(videosListView, null, 0, 0);
		else
			onListItemClick(videosListView, null, selectedVideoIndex + 1, 0);
	}

	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		synchronized (videosLock) {
			switch (screenStatus) {
				case SCREEN_STATUS_FULL:
					fullscreenVideo.stopPlayback();
					break;
				case SCREEN_STATUS_NORMAL:
					video.stopPlayback();
					break;
			}

			if (videos.isEmpty()) {
				selectedVideoIndex = -1;
				return;
			}
			getListView().postDelayed(new Runnable() {
				@Override
				public void run() {
					// Check if item is not visible and if so, scroll to it.
					if (position <= getListView().getFirstVisiblePosition() || position >= getListView().getLastVisiblePosition())
						getListView().setSelectionFromTop(position, 10);
				}
			}, 100L);

			selectedVideoIndex = position;

			switch (screenStatus) {
				case SCREEN_STATUS_FULL:
					fullscreenVideo.setEnabled(true);
					fullscreenVideo.setVideoPath(new File(currentFolder, videos.get(position)).toString());
					fullscreenVideo.requestFocus();
					fullscreenVideo.start();
					break;
				case SCREEN_STATUS_NORMAL:
					video.setEnabled(true);
					video.setVideoPath(new File(currentFolder, videos.get(position)).toString());
					video.requestFocus();
					video.start();
					break;
			}

			videosAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (video != null && video.isPlaying())
			video.pause();
		// Unregister from display change events.
		displayManager.unregisterDisplayListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (video != null && selectedVideoIndex != -1)
			video.start();
		// Register for display change events.
		displayManager.registerDisplayListener(this, null);
		// Start secondary display video player.
		startVideoPlayerPresentation();
	}
	
	/**
	 * Starts the video player presentation in the secondary display.
	 */
	private void startVideoPlayerPresentation() {
		Display[] displays = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
		if (displays != null && displays.length > 0) {
			secondaryVideoPlayer = new SecondaryVideoPlayer(this, displays[0]);
			secondaryVideoPlayer.show();
		}
	}
	
	/**
	 * Fills the ListView with the folders within the current folder.
	 */
	private void updateFolderList() {
		clearFolderList();
		pathText.setText(currentFolder.toString());
		if (currentFolder.listFiles() != null) {
			for (File file : currentFolder.listFiles()){
				if (file.isDirectory())
					folders.add(file.getName());
			}
			foldersAdapter.sort(new Comparator<String>() {
				public int compare(String object1, String object2) {
					return object1.compareTo(object2);
				}
			});
			foldersAdapter.notifyDataSetChanged();
		}
		updateVideoList();
	}
	
	/**
	 * Clears the folders list
	 */
	private void clearFolderList() {
		folders.clear();
		folders.add(getString(R.string.parent_folder));
		foldersAdapter.notifyDataSetChanged();
	}
	
	/**
	 * Clears the video list
	 */
	private void clearVideoList() {
		videos.clear();
		videosAdapter.notifyDataSetChanged();
	}

	/**
	 * Handles what happens when the full screen button is pressed.
	 */
	private void handleFullScreenButtonPressed() {
		if (selectedVideoIndex == -1)
			return;
		showDisplaySelectionDialog();
	}
	
	/**
	 * Handles what happens when the normal screen button is pressed.
	 */
	private void handleNormalScreenButtonPressed() {
		if (selectedVideoIndex == -1)
			return;
		changeToNormalScreen();
	}
	
	/**
	 * Changes to full screen video mode.
	 */
	private void changeToFullScreen() {
		synchronized (videosLock) {
			int seek = video.getCurrentPosition();
			video.stopPlayback();
			video.setVisibility(View.INVISIBLE);
			blackBackground.setVisibility(View.VISIBLE);
			fullscreenVideo.setVisibility(View.VISIBLE);
			normalscreenButton.setVisibility(View.VISIBLE);
			hideSystemUI();
			fullscreenVideo.setVideoPath(new File(currentFolder, videos.get(selectedVideoIndex)).toString());
			fullscreenVideo.start();
			fullscreenVideo.seekTo(seek);
			ctlr.setMediaPlayer(fullscreenVideo);
			ctlr.setAnchorView(findViewById(R.id.fullscreen_video_view));
			videosListView.setEnabled(false);
			folderListView.setEnabled(false);
			screenStatus = SCREEN_STATUS_FULL;
		}
	}
	
	/**
	 * Changes to normal screen video mode.
	 */
	private void changeToNormalScreen() {
		synchronized (videosLock) {
			int seek = fullscreenVideo.getCurrentPosition();
			fullscreenVideo.stopPlayback();
			fullscreenVideo.setVisibility(View.INVISIBLE);
			blackBackground.setVisibility(View.INVISIBLE);
			normalscreenButton.setVisibility(View.INVISIBLE);
			showSystemUI();
			ctlr.setMediaPlayer(video);
			ctlr.setAnchorView(findViewById(R.id.video_container));
			video.setVisibility(View.VISIBLE);
			video.setVideoPath(new File(currentFolder, videos.get(selectedVideoIndex)).toString());
			video.start();
			video.seekTo(seek);
			videosListView.setEnabled(true);
			folderListView.setEnabled(true);
			screenStatus = SCREEN_STATUS_NORMAL;
		}
	}
	
	/**
	 * Displays a popup dialog to choose the display to play video in.
	 */
	private void showDisplaySelectionDialog() {
		// First check if we have multiple displays.
		if (secondaryVideoPlayer == null) {
			changeToFullScreen();
			return;
		}

		// Build displays array.
		String[] displayNames = new String[2];
		displayNames[0] = "Primary display";
		displayNames[1] = secondaryVideoPlayer.getDisplayName();

		// Create and show the dialog.
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select Display");
		builder.setItems(displayNames, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case 0:
						changeToFullScreen();
						break;
					default:
						if (secondaryVideoPlayer != null)
							secondaryVideoPlayer.playVideo(new File(currentFolder, videos.get(selectedVideoIndex)).toString());
						break;
				}
			}
		});
		builder.show();
	}
	
	/**
	 * Hides the system bars.
	 */
	private void hideSystemUI() {
		// Set the IMMERSIVE flag.
		// Set the content to appear under the system bars so that the content
		// doesn't resize when the system bars hide and show.
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
						| View.SYSTEM_UI_FLAG_FULLSCREEN); // hide status bar
	}

	/**
	 * Shows the system bars. It does this by removing all the flags except for the ones that make
	 * the content appear under the system bars.
	 */
	private void showSystemUI() {
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}
	
	/**
	 * Populates a ListView with the data contained in the given ArrayList 
	 */
	class VideoListAdapter extends ArrayAdapter<String> {
		private ArrayList<String> videoItems;

		/**
		 * Class constructor. Instantiates a new {@code VideoListAdapter} object
		 * with the given parameters.
		 * 
		 * @param context Android application context.
		 * @param textViewResourceId Resource view used to display array item.
		 * @param items List of items for the array adapter.
		 */
		VideoListAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
			super(context, textViewResourceId, items);
			videoItems = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			View row = convertView;
			LayoutInflater inflater = getLayoutInflater();
			String listItem = videoItems.get(position);
			String selectedItem = null;
			if(selectedVideoIndex != -1)
				selectedItem = videos.get(selectedVideoIndex);
			if (listItem != null && selectedItem != null && listItem.equals(selectedItem)) {
				if (row == null || row.getId() != R.layout.selected_video_item)
					row = inflater.inflate(R.layout.selected_video_item, parent, false);
			} else {
				if (row == null || row.getId() != R.layout.video_item)
					row = inflater.inflate(R.layout.video_item, parent, false);
			}
			TextView videoTitle = (TextView)row.findViewById(R.id.video_title);
			videoTitle.setText(listItem);
			return(row);
		}
	}
	
	/**
	 * Populates a ListView with the data contained in the given ArrayList.
	 */
	class FolderListAdapter extends ArrayAdapter<String> {
		private ArrayList<String> folderItems;

		/**
		 * Class constructor. Instantiates a new {@code FolderListAdapter}
		 * object with the given parameters.
		 * 
		 * @param context Android application context.
		 * @param textViewResourceId Resource view used to display array item.
		 * @param items List of items for the array adapter.
		 */
		FolderListAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
			super(context, textViewResourceId, items);
			folderItems = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			View row = convertView;
			LayoutInflater inflater = getLayoutInflater();
			String listItem = folderItems.get(position);
			if (row == null || row.getId() != R.layout.explorer_item)
				row = inflater.inflate(R.layout.explorer_item, parent, false);
			TextView folderName = (TextView)row.findViewById(R.id.folder_name);
			if (position == 0)
				((ImageView)row.findViewById(R.id.folder_icon)).setImageResource(R.drawable.folder_icon_parent);
			folderName.setText(listItem);
			return(row);
		}
	}
}
