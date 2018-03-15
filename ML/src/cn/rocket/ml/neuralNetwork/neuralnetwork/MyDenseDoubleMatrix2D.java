package cn.rocket.ml.neuralNetwork.neuralnetwork;

import java.io.Serializable;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;

public class MyDenseDoubleMatrix2D extends DenseDoubleMatrix2D implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected MyDenseDoubleMatrix2D(int arg0, int arg1, double[] arg2, int arg3, int arg4, int arg5, int arg6) {
		super(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
		// TODO Auto-generated constructor stub
	}

	public MyDenseDoubleMatrix2D(int arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public MyDenseDoubleMatrix2D(double[][] arg0) {
		super(arg0);
		
	}

}
