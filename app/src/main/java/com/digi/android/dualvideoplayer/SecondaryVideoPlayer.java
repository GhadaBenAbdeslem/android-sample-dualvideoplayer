/**
 * Copyright (c) 2014-2015 Digi International Inc.,
 * All rights not expressly granted are reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Digi International Inc. 11001 Bren Road East, Minnetonka, MN 55343
 * =======================================================================
 */

package com.digi.android.dualvideoplayer;

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
	 * Returns the secondary display name.
	 *
	 * @return The secondary display name.
	 */
	public String getDisplayName() {
		return display.getName();
	}

	@Override
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
	 * Starts the configured video.
	 */
	private void startVideo() {
		video.setVideoPath(path);
		video.start();
	}
}
