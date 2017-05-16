package neurons;

import java.util.ArrayList;

public class Neuron {

	//TO DO:
	/*
	 * - Make a process method (loop through all out connections)
	 */
	
	private double val;
	private ArrayList<Connection> ins;
	private ArrayList<Connection> outs;
	private String name;
	
	public Neuron(String n) {
		ins = new ArrayList<>();
		outs = new ArrayList<>();
	}
	
	public void process(Neuron[][] n) {
		// TODO Auto-generated method stub
		
	}
	
	public void reset()
	{
		val = 0;
		for(Connection n : outs)
		{
			n.zeroErr();
		}
	}
	
	
//Getters
	public double getVal() {
		return val;
	}

	public String getName()
	{
		return name;
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
