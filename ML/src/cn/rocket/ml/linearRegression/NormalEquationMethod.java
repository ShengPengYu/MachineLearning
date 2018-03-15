package cn.rocket.ml.linearRegression;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;
import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;

import cn.rocket.data.DataSet;

/**
 * 
 * @author 余盛朋
 *
 */

//由于这个方法是采用正规方程的方法，所以是直接经过训练数据算出来的参数值，可以实现多变量回归
public class NormalEquationMethod {
	private Matrix X ;     //训练数据集
	private Matrix y ;     //标签集合
	private Matrix theta ; //待训练的参数
	private DataSet dataSet = new DataSet() ;
	public NormalEquationMethod() throws IOException {		
		dataSet.loadDataFromTxt("datas/linearRegression/singleData.txt", ",",1);           //加载数据集
		X = DenseMatrix.Factory.zeros(dataSet.getSize(),dataSet.getDatas().get(0).size()+1); //将数据集转换成矩阵
		y = DenseMatrix.Factory.zeros(dataSet.getSize(),1);
		theta = DenseMatrix.Factory.zeros(dataSet.getDatas().get(0).size()+1,1);
		
		for(int i = 0 ; i < dataSet.getSize() ;i++) {
			X.setAsDouble(1, i,0);
			for(int j = 0 ; j < dataSet.getDatas().get(i).size() ;j++) 
				X.setAsDouble(dataSet.getDatas().get(i).get(j), i,j+1);
			y.setAsDouble(dataSet.getLabels().get(i), i,0);
		}
		
	//	this.X = this.X.times(1.0/1000);
	//	this.y = this.y.times(1.0/100);
	}
	
	public Matrix calcTheta() {
		this.theta = (this.X.transpose().mtimes(this.X)).inv().mtimes(this.X.transpose()).mtimes(this.y);
		return this.getTheta();
	}


	public static void main(String[] args) throws IOException {
		NormalEquationMethod nem = new NormalEquationMethod() ;
		
		
		System.out.println(nem.X);
		System.out.println("theta= \n" + nem.calcTheta());
		
		List<Double> list = new ArrayList<Double>() ;
		for(int i = 0 ; i < nem.dataSet.getSize() ;i++) {
			list.add(nem.dataSet.getDatas().get(i).get(0));
		}
		
		nem.plot();
	}
	
	
	public void plot() {
		XYSeriesCollection c = new XYSeriesCollection();
		XYSeries trueSeries = new XYSeries("Train samples");
		XYSeries lineSeries = new XYSeries("Linear model");
		for(int i=0;i<this.dataSet.getSize();i++){
        		trueSeries.add(this.X.getAsDouble(i,1), this.y.getAsDouble(i,0));
	    }
		
		 for(double i=4 ; i < 25.0 ; i+=0.01) {
	        	lineSeries.add(i,this.theta.getAsDouble(0,0)+ this.theta.getAsDouble(1,0) * i);
		}
		
		c.addSeries(trueSeries);
		c.addSeries(lineSeries);
	
       //xydataset.addSeries("Method", falseData);
       final JFreeChart chart =ChartFactory.createScatterPlot("LinearRegression","x","y",c,PlotOrientation.VERTICAL,true,true,false);  
       chart.setBackgroundPaint(ChartColor.WHITE);
       XYPlot xyplot = (XYPlot) chart.getPlot();
       xyplot.setBackgroundPaint(new Color(255, 253, 246));  
       XYDotRenderer xydotrenderer = new XYDotRenderer();
		xydotrenderer.setDotWidth(5);
		xydotrenderer.setDotHeight(5);
		xyplot.setRenderer(xydotrenderer);		

		 
		 
		
       ChartFrame frame = new ChartFrame("Plot",chart);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
	}
	public Matrix getX() {
		return X;
	}

	public void setX(Matrix x) {
		X = x;
	}

	public Matrix getY() {
		return y;
	}

	public void setY(Matrix y) {
		this.y = y;
	}

	public Matrix getTheta() {
		return theta;
	}

	public void setTheta(Matrix theta) {
		this.theta = theta;
	}
}
