package com.player.android;

import java.io.File;
import java.io.FilenameFilter;

public class SelectMusicFromSDCard implements  FilenameFilter{

	@Override
	public boolean accept(File arg0, String filename) {
		// TODO Auto-generated method stub
		return (filename.endsWith(".mp3") || filename.endsWith(".MP3"));
	}

}
