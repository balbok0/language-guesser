package neurons;

import java.util.ArrayList;

public class Connection {

	//TO DO:
	/*
	 * - weight bounded by -1 and 1 or not
	 */
	
	private double error = 0;
	private static double stepSize = 0.01;
	private double weight;
	private Neuron from;
	private Neuron to;
	
	public Connection(Neuron fr, Neuron t) 
	{
		weight = Math.random()*2 - 1;
		from = fr;
		to = t;
		fr.addOut(this);
		t.addIn(this);
	}

	public Connection(Neuron fr, Neuron t, double weig)
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
	
		//Adjusts a weight by a step size
		//Then loops through all the connections of neuron it comes from.
		//Kind of like a reversed tree - you start from output, not from inputs
	public void adjust(double wholeErr)
	{
		if((wholeErr > 0 && error * from.getVal() < 0) || (wholeErr < 0 && error*from.getVal() > 0))
		{
			weight += stepSize;
		}
		else if((wholeErr > 0 && error * from.getVal() > 0) || (wholeErr < 0 && error*from.getVal() < 0))
		{
			weight -= stepSize;
		}
		ArrayList<Connection> cons = from.getIns();
		for(Connection c : cons)
		{
			c.addErr(error*weight);
			c.adjust(wholeErr);
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
	
	public static double getStepSize()
	{
		return stepSize;
	}
	
	//Setters
	public static void setStepSize(double sS)
	{
		stepSize = sS;
	}
	
	public void zeroErr()
	{
		error = 0;
	}

	public void addErr(double ad)
	{
		error += ad;
	}
}
