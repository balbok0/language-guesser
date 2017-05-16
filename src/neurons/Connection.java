package neurons;

public class Connection {

	//TO DO:
	/*
	 * - weight adjustments (due to step size)
	 * - derivative calculation
	 * - weight bounded by -1 and 1 or not
	 * 
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
	}

	public Connection(Neuron fr, Neuron t, double weig)
	{
		weight = weig;
		from = fr;
		to = t;
	}
	
	//Engine
	public void addValTo(double a)
	{
		to.addVal(a*weight);
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

	public void addErr(double a)
	{
		error += a;
	}
}
