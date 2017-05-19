package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;


public class WordList {

	//Imports files given path.
	//Then creates an array of randomly (stratified random sample) words.
	//The array at the beginning and at the end has words starting with 'a', because ending words were null otherwise
	
	private double length;
	private final double NumOfWords = 10000;
	private String[] words = new String[(int) NumOfWords];
	
	public WordList(String fileName) {
		
		try {
			getLines(fileName);
			getFile(fileName);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getWord()
	{
		return words[(int) (Math.random()*words.length)];
	}
	
	private void getLines(String fileName) throws IOException
	{
		LineNumberReader  lnr = new LineNumberReader(new FileReader(fileName));
		lnr.skip(Long.MAX_VALUE);
		length = (int) lnr.getLineNumber() + 1; //Add 1 because line index starts at 0
		// Finally, the LineNumberReader object should be closed to prevent resource leak
		lnr.close();
	}
	
	private void getFile(String fileName) throws IOException
	{	
		BufferedReader br = new BufferedReader(
			        new InputStreamReader(new FileInputStream(fileName)));
		
			try {
			    
			    //Chooses only a 
			    for(int i = 0; i < (int) NumOfWords; i++) {
			    	int sk = (int) (Math.random()* (length/NumOfWords)) - 1;
			    	skip(br, sk);
			    	words[i] = br.readLine();
			    	skip(br,(int) ((length/NumOfWords)- sk - 1));
			    	if(br.readLine() == null)
			    	{
			    		br = new BufferedReader(
						        new InputStreamReader(new FileInputStream(fileName)));
			    	}
			    }
			} finally {
			    br.close();
			}
	}
	
	private void skip(BufferedReader br, int lines) throws IOException
	{
		for(int k = 0; k < lines; k++)
		{
			br.readLine();
			
		}
	}
	
}
