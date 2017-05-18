package main;

import java.io.File;
import java.util.ArrayList;

public class Main {

	//TO DO:
	/*
	 * - Build the Neural Network - connections, neurons and stuff
	 * - Saving the results
	 */
	private static String basePath = new File("").getAbsolutePath() + "/src/languages/";
	private static String[] langs = {"english", "german", "polish", "french", "spanish", "mandarin", "korean", "japanese"};
	private static ArrayList<WordList> wordLists = new ArrayList<>();
	private static int[] actual = new int[langs.length];
	
	public static void main(String[] args)
	{
		//Chooses languages
		/* 
		 0 - English
		 1 - German
		 2 - Polish
		 3 - French
		 4 - Spanish
		 5 - Mandarin
		 6 - Korean
		 7 - Japanese
		*/
		int[] choices = {2};
		for(int c : choices)
		{
			wordLists.add(new WordList(basePath + langs[c] + ".txt")); 
		}
		System.out.println(getRandomWord());
	}
	
	//Returns a random word from a random language
	//Makes it's value 1 in 'actual' array
	//Rest is zero
	private static String getRandomWord()
	{
		int k = (int) (Math.random()*wordLists.size());
		actual = new int[langs.length];
		actual[k] = 1;
		return wordLists.get(k).getWord();
	}
}
