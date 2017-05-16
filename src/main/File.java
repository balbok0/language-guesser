package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class File {

	public File() {
		// TODO Auto-generated constructor stub
	}

	
	private ArrayList<String> getFile(String fileName) throws IOException
	{	
		ArrayList<String> ret = new ArrayList<>();
		BufferedReader br = new BufferedReader(
			        new InputStreamReader(new FileInputStream(fileName)));
		
			try {
			    String line;
			    while ((line = br.readLine()) != null) {
			        ret.add(line);
			    }
			} finally {
			    br.close();
			}
			
		return ret;
	}
}
