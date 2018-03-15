package cn.rocket.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：从txt文档中加载训练数据
 * @author rocket
 *
 */
public class DataSet implements IDataSet{
	private List<List<Double>> datas=new ArrayList<List<Double>>() ;
	private List<Double> labels=new ArrayList<>() ;
	private List<List<Object>> datasets = new ArrayList<List<Object>>();
	public DataSet() {
		
	}
	/**
	 * 
	 * @param filename 文件名称
	 * @param sep 分割符
	 * @throws IOException 
	 */
	public void loadDataFromTxt(String filename, String sep,Integer labelpos) throws IOException {
		File f = new File(filename) ;
		BufferedReader reader=null;  
        String temp=null;  
        try{  
                reader=new BufferedReader(new FileReader(f));  
                while((temp=reader.readLine())!=null){  
                	String[] str = temp.split(sep);
                	List<Double> datalist = new ArrayList<Double>();
                	for(int i = 0 ; i < str.length ;i++) 
                		if(i == labelpos) this.labels.add(Double.parseDouble(str[i]));
                		else datalist.add(Double.parseDouble(str[i])) ;
                	datas.add(datalist) ;
                	 
                }
                
                for(int i = 0 ; i < this.datas.size() ;i++) {
                	List<Object> list = new ArrayList<Object>();
                	list.add(this.datas.get(i)) ;
                	list.add(this.labels.get(i)) ;
                	this.datasets.add(list) ;
                }
        }  
        catch(Exception e){  
            e.printStackTrace();  
        }  
        finally{  
            if(reader!=null){  
                try{  
                    reader.close();  
                }  
                catch(Exception e){  
                    e.printStackTrace();  
                }  
            }  
        }  
	}
	@Override
	public List<List<Double>> getDatas() {
		// TODO Auto-generated method stub
		return this.datas;
	}
	@Override
	public List<Double> getLabels() {
		// TODO Auto-generated method stub
		return this.labels;
	}
	@Override
	public List<List<Object>> getDatasets() {
		// TODO Auto-generated method stub
		return this.datasets;
	}
	
	public static void main(String args[]) throws IOException {
		DataSet dataSet = new DataSet() ;
		dataSet.loadDataFromTxt("datas/house_price.txt", ",",2);
		System.out.println(dataSet.getSize());
		
	}

	@Override
	public Integer getSize() {
		// TODO Auto-generated method stub
		return this.datas.size();
	}
}
