package com.digi.android.dualvideoplayer;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Specifies the accepted extensions to be played.
 */
public class VideoFilter implements FilenameFilter {
	/*
	 * (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File dir, String name) {
		return (name.endsWith(".mp4") || name.endsWith(".3gp") 
				|| name.endsWith(".webm") || name.endsWith(".avi")
				|| name.endsWith(".wmv") || name.endsWith(".mkv")
				|| name.endsWith(".mov"));
	}
}
