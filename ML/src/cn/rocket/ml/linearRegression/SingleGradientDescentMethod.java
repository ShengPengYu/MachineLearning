package cn.rocket.ml.linearRegression;


import java.awt.Color;
import java.io.IOException;
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

import cn.rocket.data.DataSet;

/**
 * 
 * @author 余盛朋
 *
 */
//这个例子我只是实现梯度下降的思想。目前只实现了单变量线性回归。模型是y=theta0 + theta1*x 我们的目标就是根据训练数据去拟合theta0根theta1
public class SingleGradientDescentMethod {

	private double theta0 = 0.0 ;  //截距
	private double theta1 = 0.0 ;  //斜率
	private double alpha = 0.01 ;  //学习速率
	
	private int max_itea = 7000 ; //最大迭代步数
	
	private DataSet dataSet = new DataSet() ;
	
	public  SingleGradientDescentMethod() throws IOException{
		dataSet.loadDataFromTxt("datas/linearRegression/singleData.txt", ",",1);
	}
	
	
	public double predict(double x){
		return theta0+theta1*x ;
	}
	
	public double calc_error(double x, double y) {
		return predict(x)-y;
	}
	

	
	public void gradientDescient(){//梯度下降求参数theta0跟theta1
		double sum0 =0.0 ;
		double sum1 =0.0 ;
		
		for(int i = 0 ; i < dataSet.getSize() ;i++) {
			sum0 += calc_error(dataSet.getDatas().get(i).get(0), dataSet.getLabels().get(i)) ;
			sum1 += calc_error(dataSet.getDatas().get(i).get(0), dataSet.getLabels().get(i))*dataSet.getDatas().get(i).get(0) ;
		}
		
		this.theta0 = theta0 - alpha*sum0/dataSet.getSize() ; 
		this.theta1 = theta1 - alpha*sum1/dataSet.getSize() ; 
	
	}
	
	public void main() {
		int itea = 0 ;
		while( itea< max_itea){//迭代求参，直到达到最大迭代步数为止
			System.out.println("The current step is :"+itea);
			System.out.println("theta0 "+theta0);
			System.out.println("theta1 "+theta1);
			System.out.println();
			gradientDescient();
			itea ++ ;
		}
	} ;
	
	public void plot() {
		XYSeriesCollection c = new XYSeriesCollection();
		XYSeries sampleSeries = new XYSeries("Train samples");
		XYSeries lineSeries = new XYSeries("Linear model");
		for(int i=0;i<this.dataSet.getSize();i++){
        		sampleSeries.add(this.dataSet.getDatas().get(i).get(0), this.dataSet.getLabels().get(i));
	    }
		
		 for(double i=4 ; i < 25.0 ; i+=0.01) {
	        	lineSeries.add(i,this.predict(i));
		}
		
		c.addSeries(sampleSeries);
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
	
	public static void main(String[] args) throws IOException {
		SingleGradientDescentMethod linearRegression = new SingleGradientDescentMethod() ;
		linearRegression.main();
		
		linearRegression.plot();
		
	}
	
}

