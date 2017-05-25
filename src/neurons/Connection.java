package neurons;

import java.util.ArrayList;

import main.Functions;
import main.Main;

public class Connection {

	//TO DO:
	/*
	 * - fix adding values to farther connections (to include sigmoid)
	 * - do the sigmoid derivative
	 */
	
	private double errDer[] = new double[Main.NUMBEROFLANGS];;
	private boolean[] isConnected = new boolean[Main.NUMBEROFLANGS];
	private final double stepSize = 0.001;
	private double weight;
	private Neuron from;
	private Neuron to;
	private final double upperBound = 1;
	private final double lowerBound = -1;

	
	public Connection(Neuron fr, Neuron t) 
	{
		weight = Math.random()*2 - 1;
		from = fr;
		to = t;
		fr.addOut(this);
		t.addIn(this);
	}

	public Connection(Neuron fr, Neuron t, double weig, int NofLangs)
	{
		weight = weig;
		from = fr;
		to = t;
		fr.addOut(this);
		t.addIn(this);
	}
	
	//Engine
	public void addValTo(double a)
	{
		to.addVal(a*weight);
	}
	
	
	
		//Loops through all the 'in' connections of neuron it comes from.
	public void backpropagate()
	{
		ArrayList<Connection> loop = from.getIns();
		
		for(int i = 0; i < loop.size(); i++)
		{
			for(int ei = 0; ei < errDer.length && isConnected[ei]; ei++)
			{
				loop.get(i).addErr(ei, errDer[ei] * weight);
			}
		}
	}
	
	//Adjusts a weight by a step size
	public void adjust(double[] pred, int[] act)
	{
		double sum = 0;
		for(int i = 0; i < errDer.length; i++)
		{
			sum += Functions.sigmoidDerivative(pred[i])*(2*pred[i]-2*act[i])*errDer[i]*from.getVal();
		}
		if(sum > 0)
		{
			weight -= stepSize;
		}
		else if(sum < 0)
		{
			weight += stepSize;
		}
		notCrossBoundary();
	}
	
		private void notCrossBoundary()
		{
			if(weight >= upperBound)
			{
				weight = upperBound;
			}
			else if(weight <= lowerBound)
			{
				weight = lowerBound;
			}
		}
	
	//Getters
	
	public double getWeight()
	{
		return weight;
	}
	

	public Neuron getNeuronTo()
	{
		return to;
	}
	
	
	public Neuron getNeuronFrom()
	{
		return from;
	}
	
	
	//Changing Error
	
	public void zeroErr()
	{
		for(int i = 0; i < errDer.length; i++)
		{
			errDer[i] = 0;
			isConnected[i] = false;
		}
	}

	
	public void addErr(int index, double ad)
	{
		isConnected[index] = true;
		errDer[index] += ad;
	}
}
