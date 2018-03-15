package cn.rocket.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.rocket.ml.neuralNetwork.neuralnetwork.Network;



public class SerializeUtils {
	//序列化 讲对象以字节码的形式保存到文件，方便回复对象
		public static void serialize(Object object,String filename) throws FileNotFoundException, IOException {
			File file = new File(filename) ;
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));  
		     oos.writeObject(object);  
		     oos.close();  
			
		}
		
		//反序列化 从序列化文件中回复对象
		public static Object deserialization(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
		     ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));  
			Object citys  = ois.readObject();  
		     ois.close(); 
		     return citys;
		}
}
