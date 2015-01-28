package com.mybird.frameword;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.SharedPreferences;

public interface FileIO {
	public InputStream readAsset(String filename) throws IOException;
	
	public InputStream readFile(String filename) throws IOException;
	
	public OutputStream writeFile(String filename) throws IOException;
	
	public SharedPreferences getPreferences() throws Exception;
}
