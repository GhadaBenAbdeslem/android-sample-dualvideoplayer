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

import java.io.File;
import java.io.FilenameFilter;

/**
 * Specifies the accepted extensions to be played.
 */
public class VideoFilter implements FilenameFilter {
	@Override
	public boolean accept(File dir, String name) {
		return (name.endsWith(".mp4") || name.endsWith(".3gp") 
				|| name.endsWith(".webm") || name.endsWith(".avi")
				|| name.endsWith(".wmv") || name.endsWith(".mkv")
				|| name.endsWith(".mov"));
	}
}
