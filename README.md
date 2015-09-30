Dual Video Player Sample
========================

This example demonstrates how to reproduce videos in two different
displays simultaneously in Android. For this purpose, a custom video player
is used. Application allows navigation in the Android file system to search
for desired video files to be reproduced. User is allowed to decide the
destination screen of the video to be played.

Demo requeriments
-----------------

To run this example you will need:
    - One compatible device to host the application.
    - Network connection between the device and the host PC in order to
      transfer and launch the application.
    - Establish remote target connection to your Digi hardware before running
      this application.
    - A secondary display attached to your device, preferably HDMI (Full-HD).
    - Video files to be played reproduced stored in the Android file system
      or in external uSD card.

Compatible Video file extensions include:
    - .avi
    - .mp4
    - .3gp
    - .webm
    - .wmv
    - .mkv

Demo setup
----------

 Make sure the hardware is set up correctly:
    - The device is powered on.
    - The device is connected directly to the PC or to the Local 
      Area Network (LAN) by the Ethernet cable.
    - The secondary display is connected to the SBC board.
    - You have any video file stored in your Android file system or in the
      uSD card to be played.

Demo run
--------

The example is already configured, so all you need to do is to build and 
launch the project.
  
Application allows user to navigate through the Android file system and 
select folders containing compatible video files. Once a folder is selected, 
all the folder available video files are listed creating a playlist.
  
A preview of the selected video file is displayed at the right side of the 
application. When clicking the full-screen button, user is prompted with a
dialog to choose the destination display to play the video.

Tested on
---------

ConnectCore 6 SBC
ConnectCore 6 SBC v2