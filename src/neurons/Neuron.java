package neurons;

import java.util.ArrayList;

public class Neuron {

	
	private double val = 0;
	private ArrayList<Connection> ins;
	private ArrayList<Connection> outs;
	private String name;
	
	public Neuron(String n) {
		name = n;
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
	
	public ArrayList<Connection> getIns()
	{
		return ins;
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
		new Connection(n, this);
	}
	
	public void addOut(Connection n)
	{
		outs.add(n);
	}
	
	public void addOut(Neuron n)
	{
		new Connection(this, n);
	}

}
