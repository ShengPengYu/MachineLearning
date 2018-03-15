package cn.rocket.ml.neuralNetwork.model;

public class ImageModel {
    private double[] grayMatrix = null;
    private int digit = -1;
    private double[] outputList = new double[10];

    public ImageModel(double[] grayMatrix,int digit){
        this.grayMatrix = grayMatrix;
        this.digit = digit;
        for(int i=0;i<10;i++){
            if(this.digit == i){
                outputList[i] = 1.0;
            }else{
                outputList[i] = 0.0;
            }
        }
    }

    public double[] getGrayMatrix() {
        return grayMatrix;
    }

    public void setGrayMatrix(double[] grayMatrix) {
        this.grayMatrix = grayMatrix;
    }

    public int getDigit() {
        return digit;
    }

    public void setDigit(int digit) {
        this.digit = digit;
    }

    public double[] getOutputList() {
        return outputList;
    }

    public void setOutputList(double[] outputList) {
        this.outputList = outputList;
    }
}
