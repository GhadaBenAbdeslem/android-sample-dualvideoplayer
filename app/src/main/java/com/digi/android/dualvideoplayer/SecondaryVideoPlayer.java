package com.example.android.dualvideoplayer;

import android.app.Presentation;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Display;
import android.widget.VideoView;

public class SecondaryVideoPlayer extends Presentation {

	// Variables.
	private VideoView video;

	private String path;
	
	private Display display;

	/**
	 * Class constructor. Instantiates a new {@code SecondaryVideoPlayer}
	 * object with the given parameters.
	 * 
	 * @param outerContext Application context.
	 * @param display Display where this presentation will be displayed.
	 */
	public SecondaryVideoPlayer(Context outerContext, Display display) {
		super(outerContext, display);
		this.display = display;
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_fullscreen);

		// Initialize UI components.
		initializeUIComponents();
	}

	/**
	 * Initializes all the UI components.
	 */
	private void initializeUIComponents() {
		video = (VideoView)this.findViewById(R.id.video_fs);
		video.requestFocus();
		video.setOnCompletionListener(new OnCompletionListener() {
			/*
			 * (non-Javadoc)
			 * @see android.media.MediaPlayer.OnCompletionListener#onCompletion(android.media.MediaPlayer)
			 */
			public void onCompletion(MediaPlayer mp) {
				playVideo(path);
			}
		});
	}
	
	/**
	 * Plays the given video path.
	 * 
	 * @param videoPath Path of the video to play.
	 */
	public void playVideo(String videoPath) {
		if (videoPath == null)
			return;
		this.path = videoPath;
		startVideo();
	}

	/**
	 * Starts the configured video.
	 */
	private void startVideo() {
		video.setVideoPath(path);
		video.start();
	}
	
	/**
	 * Returns the secondary display name.
	 * 
	 * @return The secondary display name.
	 */
	public String getDisplayName() {
		return display.getName();
	}
}
