Dual Video Player Sample
========================

This example demonstrates how to reproduce videos in two different displays 
simultaneously in Android. For this purpose, a custom video player is used. 

The application allows navigation in the Android file system to search for
desired video files to be reproduced. A preview of the selected video file
is displayed at the right side of the application, allowing you to reproduce
it in full-screen mode using the change to full-screen button and choose the
destination display.

Demo requirements
-----------------

To run this example you need:

* One compatible device to host the application.
* A USB connection between the device and the host PC in order to transfer and
  launch the application.
* A secondary display attached to your device, preferably HDMI (Full-HD).
* Video files to play stored in the Android file system or in a external 
  uSD card.

Compatible Video file extensions include:

* .avi
* .mp4
* .3gp
* .webm
* .wmv
* .mkv

Demo setup
----------

Make sure the hardware is set up correctly:

1. The device is powered on.
2. The device is connected directly to the PC by the micro USB cable.
3. The secondary display is connected to the SBC board.
4. You have video files stored in your Android file system or in the uSD card.

Demo run
--------

The example is already configured, so all you need to do is to build and 
launch the project.
  
Navigate through the Android file system and select a folder containing 
compatible video files. All available videos in the folder are included in a 
playlist.

A preview of the selected video file is displayed at the right side of the 
application. 

Click **full-screen** and choose the destination display when a prompt appears
to play the selected video.

Compatible with
---------------

* ConnectCore 6 SBC
* ConnectCore 6 SBC v2

License
-------

This software is open-source software. Copyright Digi International, 2014-2015.

This Source Code Form is subject to the terms of the Mozilla Public License,
v. 2.0. If a copy of the MPL was not distributed with this file, you can obtain
one at http://mozilla.org/MPL/2.0/.