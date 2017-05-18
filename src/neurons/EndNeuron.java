package neurons;

import main.Functions;

public class EndNeuron extends Neuron {

	// This class will create output which will be a probability of a certain language
	
	public EndNeuron(String name) {
		// TODO Auto-generated constructor stub
		super(name);
	}

	@Override
	public void process()
	{
		super.setVal(Functions.sigmoid(super.getVal()));
	}

	//Basically ensures that output is between 0-1 (inclusive), because that's how probabilities look like
	public double getVal()
	{
		return Functions.ReLU(super.getVal());
	}

}
