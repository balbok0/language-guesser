package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import neurons.*;

public class Main {

	//TO DO:
	/*
	 * - Build a method to zero all the neurons
	 * - Build a method to backpropagate
	 * - Build something which will output a accuracy of net (and thus modify UI method)
	 * - Learn how to handle umlauts, etc.
	 * - UI method - basically a true main
	 * - Saving the results
	 */
	
	//Chooses languages:
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
	private static int[] choices = {0, 1, 2, 3, 4};
	//Number of layers neural net has. This number DOES NOT include input and output layer.
	private static int layers = 1;

	
	private static String[] langs = {"english", "german", "polish", "french", "spanish", "mandarin", "korean", "japanese"};
	private static ArrayList<ArrayList<Neuron>> net = new ArrayList<>();
	private static String basePath = new File("").getAbsolutePath() + "/src/languages/";
	private static ArrayList<WordList> wordLists = new ArrayList<>();
	private static int[] actual = new int[langs.length];
	private static int wordCounter = 0;
	private static int maxInpLen = 10;
	
	public static void main(String[] args)
	{
		//initializing
		for(int c : choices)
		{
			wordLists.add(new WordList(basePath + langs[c] + ".txt")); 
		}
		createNet();
		//End of initialization
		
		//After the whole thing is initialized, we are waiting what the user will do
		UI();
		
		//Code for saving weights to an file
		
		System.out.println("Thanks.");
	}
	
	//This is where most of code works.
	private static void UI()
	{
		String input = getRandomWord();
		int maxLen = input.length();

		//Here put the code for commands user can give
		System.out.println("Possible commands are:");
		System.out.println();
		System.out.println("A. User input of word.");
		System.out.println("B. Train from a random word.");
		System.out.println("C. Train from a 100 random words.");
		System.out.println("D. Train from N random words (N is chosen by user).");
		System.out.println("E. Exit. (Data exports to a file.)");
		Scanner in = new Scanner(System.in);
		String userIn = in.nextLine();
		while(!userIn.equals("E"))
		{
			if(userIn.equals("A"))
			{
				userIn = in.nextLine().toLowerCase();
				//Process through network with word as an input
			}
			else if(userIn.equals("B"))
			{

			}
			else if(userIn.equals("C"))
			{

			}
			else if(userIn.equals("D"))
			{

			}
			else
			{
				System.out.println("Invalid input, please try again.");

			}
			userIn = in.nextLine();
		}
		in.close();
		System.out.println("Don't close compiler yet. Saving data to the file...");
	}
	
	//Returns a random word from a random language
	//Makes it's value 1 in 'actual' array
	//Rest is zero
	private static String getRandomWord()
	{
		wordCounter++;
		int k = (int) (Math.random()*wordLists.size());
		actual = new int[langs.length];
		actual[k] = 1;
		//If there where more than 10000 words taken, then it creates new random words list. (So batches differ)
		if(wordCounter > 10000)
		{
			wordLists = new ArrayList<>();
			for(int c : choices)
			{
				wordLists.add(new WordList(basePath + langs[c] + ".txt")); 
			}
		}
		return wordLists.get(k).getWord();
	}

	//Creates a net for a given number of layers (field called layers).
	//With 10 neurons in each
	private static void createNet()
	{
		//The last layer will actually be populated with EndNeurons!!!
		for(int i = 0; i < layers + 2 ; i++)
		{
			net.add(new ArrayList<Neuron>());
		}
		//Populates only last layer (with no connections)
		for(int i = 0; i < langs.length; i++)
		{
			net.get(net.size()-1).add(new EndNeuron((net.size()-1) + "|" + i));
		}
		//Populates each layer (expect for last) with 10 neurons, plus creates connections to all of neurons from next layer
		for(int layer = layers - 2; layer >= 0 ; layer--)
		{
			for(int i = 0; i < 10; i++)
			{
				net.get(layer).add(new Neuron(layer + "|" + i));
				Neuron n = net.get(layer).get(i);
				for(Neuron to : net.get(layer + 1))
				{
					n.addOut(to);
				}
			}
		}
		
	}

	//Add a given amount of neurons to a given layer
	//Then creates connections to each Neuron in neighboring layers
	private static void addNeurons(int layerN , int howMuch)
	{
		
		if(layerN == 0)
		{
			for(int i = 0; i < howMuch; i++)
			{
				net.get(layerN).add(new Neuron(layerN + "|" + net.get(layerN).size()));
				Neuron newOne = net.get(layerN).get(net.get(layerN).size()-1);
				for(int j = 0; j < net.get(layerN + 1).size(); j++)
				{
					newOne.addOut(new Connection(newOne, net.get(layerN + 1).get(j)));
				}
			}
		}
		else if(layerN == layers + 1)
		{
			for(int i = 0; i < howMuch; i++)
			{
				net.get(layerN).add(new EndNeuron(layerN + "|" + net.get(layerN).size()));
				Neuron newOne = net.get(layerN).get(net.get(layerN).size()-1);
				for(int j = 0; j < net.get(layerN + 1).size(); j++)
				{
					newOne.addOut(new Connection(net.get(layerN - 1).get(j), newOne));
				}
			}
		}
		
		else
		{
			for(int i = 0; i < howMuch; i++)
			{
				net.get(layerN).add(new Neuron(layerN + "|" + net.get(layerN).size()));
				Neuron newOne = net.get(layerN).get(net.get(layerN).size()-1);
				for(int j = 0; j < net.get(layerN + 1).size(); j++)
				{
					newOne.addOut(new Connection(net.get(layerN - 1).get(j), newOne));
					newOne.addOut(new Connection(newOne, net.get(layerN + 1).get(j)));
				}
			}
		}
	}
	
	//Goes through the whole net
	//Returns an array of probabilities - with indexes respective to languages
	//This is so called predicted output
	private double[] goThroughNet(String inp)
	{
		
		if(inp.length() > maxInpLen)
		{
			addNeurons(0, inp.length()-maxInpLen);
			maxInpLen = inp.length();
		}
		for(int i = 0; i < inp.length(); i++)
		{
			//-60 because 'a' is at index of 61
			net.get(0).get(i).addVal(inp.charAt(0) - 60);
		}
		for(ArrayList<Neuron> lay : net)
		{
			for(Neuron n : lay)
			{
				n.process();
			}
		}
		double[] ret = new double[langs.length];
		for(int i = 0; i < net.get(net.size()-1).size(); i++)
		{
			ret[i] = net.get(net.size()-1).get(i).getVal();
		}
		
		return ret;
	}
}
