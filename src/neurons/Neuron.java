package neurons;

import java.util.ArrayList;

import main.Functions;

public class Neuron {

	//TO DO:
	/* 
	 * - Make a backpropagation support method (here?)
	 */
	
	private double val;
	private ArrayList<Connection> ins;
	private ArrayList<Connection> outs;
	private String name;
	
	public Neuron(String n) {
		ins = new ArrayList<>();
		outs = new ArrayList<>();
	}
	
	//Loops through all the out connections, and adds values to neurons on the other side.  
	public void process() 
	{
		for(Connection c : outs)
		{
			c.addValTo(val);
		}
	}
	
	//Zeros value, and all the errors of functions before.
	public void reset()
	{
		val = 0;
		for(Connection n : ins)
		{
			n.zeroErr();
		}
	}
	
	
//Getters
	public double getVal() 
	{
		return val;
	}

	public String getName()
	{
		return name;
	}
	

//Setters

	public void setVal(double s)
	{
		val = s;
	}
	
//Adders
	public void addVal(double add)
	{
		val += add;
	}
	
	public void addIn(Connection n)
	{
		ins.add(n);
	}
	
	public void addIn(Neuron n)
	{
		ins.add(new Connection(n, this));
	}
	
	public void addOut(Connection n)
	{
		outs.add(n);
	}

	public void addOut(Neuron n)
	{
		outs.add(new Connection(this, n));
	}

}
