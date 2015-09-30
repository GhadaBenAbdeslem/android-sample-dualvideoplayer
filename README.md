Dual Video Player Sample
========================

This example demonstrates how to reproduce videos in two different displays 
simultaneously in Android. For this purpose, a custom video player is used. 
This application allows navigation in the Android file system to search for 
desired video files to be reproduced. You can select the destination screen of 
the video to be played.

Demo requirements
-----------------

To run this example you need:

* One compatible device to host the application.
* Network connection between the device and the host PC in order to transfer 
  and launch the application.
* Establish remote target connection to your Digi hardware before running this 
  application.
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
2. The device is connected directly to the PC or to the Local Area Network (LAN)
   by the Ethernet cable.
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

Tested on
---------

* ConnectCore 6 SBC
* ConnectCore 6 SBC v2