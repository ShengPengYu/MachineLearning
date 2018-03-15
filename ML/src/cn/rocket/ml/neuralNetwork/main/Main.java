package cn.rocket.ml.neuralNetwork.main;
import java.io.File;

import cn.rocket.ml.neuralNetwork.constant.Constant;
import cn.rocket.ml.neuralNetwork.ui.MainFrame;

public class Main {
    //the main function of whole project
    public static void main(String[] args){
       /* String folderName = System.getProperty("java.io.tmpdir");
        //create training data store path
        String trainFolder = folderName + "/digital_recognizer_train";
        File folder = new File(trainFolder);
        if(!folder.isDirectory()){
            folder.mkdir();
        }
        Constant.trainFolder = trainFolder;
*/
        new MainFrame();
    }
}
