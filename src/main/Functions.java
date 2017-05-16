package main;

public final class Functions {

	public static double sigmoid(double x)
	{
		return 1/(1+Math.pow(Math.E, -x));
	}

	public static double ReLU(double x)
	{
		if(x < 0)
		{
			return 0;
		}
		return x;
	}
}
