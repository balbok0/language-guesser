package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import neurons.*;

public class Main {

	//TO DO:
	/*
	 *  - Change the process method to go through Connections not through Neuron
	 * 	- Saving the results
	*/
	
	//Chooses languages:
	/* 
	*	0 - English
	* 	1 - German
	* 	2 - Polish
	* 	3 - French
	* 	4 - Spanish
	*	5 - Mandarin
	* 	6 - Korean
	*	7 - Japanese
	*/
	private static int[] choices = {0, 1};
	//Number of layers neural net has. This number DOES NOT include input and output layer.
	private static int layers = 4; 

	private static boolean[] accuracy = new boolean[1000];
	private static String[] langs = {"english", "german", "polish", "french", "spanish", "mandarin", "korean", "japanese"};
	private static ArrayList<ArrayList<Neuron>> net = new ArrayList<>();
	private static String basePath = new File("").getAbsolutePath() + "/src/languages/";
	private static ArrayList<WordList> wordLists = new ArrayList<>();
	private static int[] actual = new int[langs.length];
	private static int wordCounter = 0;
	private static int maxInpLen = 10;
	public static final int NUMBEROFLANGS = langs.length;
	
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
		Scanner in = new Scanner(System.in);
		String userIn = "random";
		while(!userIn.equals("E"))
		{
			
			System.out.println("Possible commands are:");
			System.out.println();
			System.out.println("O. Show with what accuracy of this neural net.");
			System.out.println("A. User input of word.");
			System.out.println("B. Train from a random word.");
			System.out.println("C. Train from a 100 random words.");
			System.out.println("D. Train from N random words (N is chosen by user).");
			System.out.println("E. Exit. (Data exports to a file.)");
			userIn = in.nextLine();
			
			if(userIn.toUpperCase().equals("O"))
			{
				System.out.println("Accuracy of net is " + (getAccuracy()*100) + "%.");
			}
			else if(userIn.toUpperCase().equals("A"))
			{
				System.out.println("Please put in your word:");
				int guess = process1NonRandomInput(in.nextLine().toLowerCase());
				System.out.println("Is it " + langs[guess] + "?");
				String ans = "something different than while loop requirements";
				while(!(ans.equals("yes") || ans.equals("y") || ans.equals("no") || ans.equals("n")))
				{
					System.out.println("Input Yes or No");
					ans = in.nextLine().toLowerCase();
					if(ans.equals("yes") || ans.equals("y"))
					{
						wordCounter++;
						accuracy[wordCounter%accuracy.length] = true;
					}
					else if(ans.equals("no") || ans.equals("n"))
					{
						wordCounter++;
						accuracy[wordCounter%accuracy.length] = false;

					}
					else
					{
						System.out.println("Invalid input. Try again.");
					}
				}
				
			}
			else if(userIn.toUpperCase().equals("B"))
			{
				process1Input(getRandomWord());
			}
			else if(userIn.toUpperCase().equals("C"))
			{
				for(int i = 0; i < 100; i++)
				{
					process1Input(getRandomWord());
				}
			}
			else if(userIn.toUpperCase().equals("D"))
			{
				System.out.println("Please enter how many words you want to train through:");
				int howMany = 0;
				try{
					howMany = Integer.parseInt(in.nextLine());
					
				} catch(Exception e)
				{
					System.out.println("Invalid input. Command failed.");
				}
				for(int i = 0; i < howMany; i++)
				{
					process1Input(getRandomWord());
				}
			}
			else
			{
				System.out.println("Invalid input, please try again.");

			}
			System.out.println();
			System.out.println();
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
		//If there is k*10000 words taken, then it creates new random words list. (So batches differ)
		if(wordCounter%10000 == 0)
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
		
		for(int layer = layers; layer >= 0 ; layer--)
		{
			for(int i = 0; i < maxInpLen; i++)
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
	
	//Returns how many  of Neural Net where correct
	private static double getAccuracy()
	{
		if(wordCounter == 0)
		{
			System.out.println("ERROR, ERROR, ERROR!!! No words given yet.");
			return 0;
		}
		if(wordCounter < accuracy.length)
		{
			int counter = 0;
			for(int i = 0; i < wordCounter; i++)
			{
				if(accuracy[i])
				{
					counter++;
				}
			}
			return counter*1.0/(wordCounter);
		}
		int counter = 0;
		for(int i = 0; i < accuracy.length; i++)
		{
			if(accuracy[i])
			{
				counter++;
			}
		}
		return counter*1.0/accuracy.length;
	}
	
	private static void process1Input(String input)
	{
		while (input == null)
		{
			input = getRandomWord();
		}
		double[] predicted = goThroughNet(input);
		
		int maxIndex = 0;
		for(int k = 0; k < predicted.length; k++)
		{
			if(predicted[maxIndex] < predicted[k])
			{
				maxIndex = k;
			}
		}
		if(actual[maxIndex] == 1)
		{
			accuracy[(wordCounter-1)%accuracy.length] = true;
		}
		else
		{
			accuracy[wordCounter%accuracy.length] = false;
		}
		//Error = (predicted - actual)^2
		backpropagate(predicted, actual);
		reset();
	}
	
	private static int process1NonRandomInput(String input)
	{
		while (input == null)
		{
			input = getRandomWord();
		}
		double[] predicted = goThroughNet(input);
		int maxIndex = 0;
		for(int k = 0; k < predicted.length; k++)
		{
			if(predicted[maxIndex] < predicted[k])
			{
				maxIndex = k;
			}
		}
		reset();
		return maxIndex;
	}
	
	//Goes through the whole net
	//Returns an array of probabilities - with indexes respective to languages
	//This is so called predicted output
	private static double[] goThroughNet(String inp)
	{
		
		if(inp.length() > maxInpLen)
		{
			addNeurons(0, inp.length()-maxInpLen);
			maxInpLen = inp.length();
		}
		for(int i = 0; i < inp.length(); i++)
		{
			//-60 because 'a' is at index of 61
			if(inp.toLowerCase().charAt(i) > 122)
			{
				specialLetters(inp.toLowerCase().charAt(i), i);	
			}
			else
			{
				net.get(0).get(i).addVal(inp.charAt(0) - 96);
			}
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
	
		private static void specialLetters(char c, int i)
		{
			if((c <= 'æ' && c >= 'à') || (c <= 'ą' && c >= 'Ā'))
			{
				net.get(0).get(i).addVal('a' - 96);
			}
			else if(c == 'ç' || (c <= 'č' && c >= 'Ć'))
			{
				net.get(0).get(i).addVal('c' - 96);
			}
			else if(c == 'ð' || c == 'ď' || c == 'đ')
			{
				net.get(0).get(i).addVal('d' - 96);
			}
			else if((c <= 'ë' && c >= 'è') || (c >= 'Ē' && c <= 'ě'))
			{
				net.get(0).get(i).addVal('e' - 96);
			}
			else if((c >= 'Ĝ' && c <= 'ģ') || c == 'ǧ' || c == 'ǵ')
			{
				net.get(0).get(i).addVal('g' - 96);
			}
			else if(c == 'ĥ' || c == 'ħ' || c == 'ȟ')
			{
				net.get(0).get(i).addVal('h' - 96);
			}
			else if((c >= 'ì' && c <= 'ï') || 
					(c >= 'Ĩ' && c <= 'ı') || 
					c == 'ǐ'|| c == 'ȉ' || c == 'ȋ')
			{
				net.get(0).get(i).addVal('i' - 96);
			}
			else if(c == 'ĵ' || c == 'ǰ' || c == 'ʲ')
			{
				net.get(0).get(i).addVal('j' - 96);
			}
			else if(c == 'ķ' || c == 'ǩ')
			{
				net.get(0).get(i).addVal('k' - 96);
			}
			else if(c >= 'Ĺ' && c <= 'ł')
			{
				net.get(0).get(i).addVal('l' - 96);
			}
			else if(c == 'ǹ' || (c >= 'Ń' && c <= 'ň'))
			{
				net.get(0).get(i).addVal('n' - 96);
			}
			else if((c >= 'ò' && c <= 'ø') || (c >= 'Ȫ' && c <= 'ȱ') ||
					(c >= 'Ō' && c <= 'ő') || c == 'ơ' ||
					c == 'ǒ' || c == 'ǫ' || c == 'ǭ' ||
					c == 'ǿ' || c == 'ȍ' || c == 'ȏ')
			{
				net.get(0).get(i).addVal('o' - 96);
			}
			else if((c >= 'Ŕ' && c <= 'ř') || 
					(c >= 'Ȑ' && c <= 'ȓ'))
			{
				net.get(0).get(i).addVal('r' - 96);
			}
			else if((c >= 'Ś' && c <= 'š') ||
					c == 'ſ' || c == 'ș')
			{
				net.get(0).get(i).addVal('s' - 96);
			}
			else if(c == 'ţ' || c == 'ť'
					|| c == 'ț')
			{
				net.get(0).get(i).addVal('t' - 96);
			}
			else if((c >= 'ù' && c <= 'ü') ||
					(c >= 'Ũ' && c <= 'ų') ||
					(c >= 'Ǔ' && c <= 'ǜ') ||
					c == 'ư' || c == 'ȕ' || c == 'ȗ')
			{
				net.get(0).get(i).addVal('u' - 96);
			}
			else if(c == 'ŵ')
			{
				net.get(0).get(i).addVal('w' - 96);
			}
			else if(c == 'ý' || c == 'ÿ' || c == 'ŷ' ||
					c == 'ȳ' || c == 'Ÿ')
			{
				net.get(0).get(i).addVal('y' - 96);
			}
			else if(c >= 'Ź' || c <= 'ž')
			{
				net.get(0).get(i).addVal('z' - 96);
			}
		}
	
	private static void backpropagate(double[] predicted, int[] actual)
	{
		//All neurons in last layer
		for(int n = 0; n < net.get(net.size()-1).size(); n++)
		{
			for(int c = 0; c < net.get(net.size()-1).get(n).getIns().size(); c++)
			{
				net.get(net.size()-1).get(n).getIns().get(c).addErr(n, 1);
			}
		}
		for(int lay = net.size()-1; lay > 0; lay--)
		{
			for(Neuron n : net.get(lay))
			{
				for(Connection c : n.getIns())
				{
					c.adjust(predicted, actual);
					c.backpropagate();
				}
			}
		}
		
	}
	
	private static void reset()
	{
		for(ArrayList<Neuron> lay : net)
		{
			for(Neuron n : lay)
			{
				n.reset();
			}
		}
	}
}
