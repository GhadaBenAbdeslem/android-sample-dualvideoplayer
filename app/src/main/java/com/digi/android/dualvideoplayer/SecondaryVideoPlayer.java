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
