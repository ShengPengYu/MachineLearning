package cn.rocket.ml.neuralNetwork.ui;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cn.rocket.ml.neuralNetwork.constant.Constant;
import cn.rocket.ml.neuralNetwork.model.ImageModel;
import cn.rocket.ml.neuralNetwork.neuralnetwork.MyDenseDoubleMatrix2D;
import cn.rocket.ml.neuralNetwork.neuralnetwork.Network;
import cn.rocket.ml.neuralNetwork.util.ImageUtil;
import cn.rocket.utils.SerializeUtils;

public class MainFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	private int width = 450;
    private int height = 450;
    private Canvas canvas = null;

    //four buttons: clear,tell num,train,test
    private JButton jbClear = null;
    private JButton jbNum = null;
    private JButton jbTrain = null;
    private JButton jbTest = null;

    private Network network = null;

    public MainFrame(){
        super();
        this.setTitle("Digital Recognizer");
        this.setSize(width, height);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(300, 300);
        this.setLayout(null);

        this.canvas = new Canvas(280,280);
        this.canvas.setBounds(new Rectangle(85, 30, 280, 280));
        this.add(this.canvas);

        this.network = new Network(new int[]{28*28,30,10});

        this.jbClear = new JButton();
        this.jbClear.setText("clear");
        this.jbClear.setBounds(40, 360, 80, 30);
        this.add(jbClear);
        this.jbClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.clear();
                Constant.digit = -1;
            }
        });

        this.jbNum = new JButton();
        this.jbNum.setText("tell num");
        this.jbNum.setBounds(140, 360, 80, 30);
        this.add(jbNum);
        this.jbNum.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] outline = getOutline();
                if(outline[0] == -10){
                    canvas.clear();
                    JOptionPane.showMessageDialog(null, "Please draw one number into the rectangle");
                }else{
                    String str = (String) JOptionPane.showInputDialog(null, "Please input the number you write：\n", "Tell Me Number", JOptionPane.PLAIN_MESSAGE, null, null,
                            "");
                    try {
                        int digit = Integer.parseInt(str);
                        if (digit < 0 || digit > 9) {
                            canvas.clear();
                            JOptionPane.showMessageDialog(null, "I can only learn number 0~9");
                            Constant.digit = -1;
                        } else {
                            Constant.digit = digit;
                            //save image and digit
                            String fileName = saveJPanel(outline);
                            canvas.clear();
                            JOptionPane.showMessageDialog(null, "I have remember the number:" + digit + ". Image file path:" + fileName);
                        }
                    } catch (Exception ex) {
                        //ex.printStackTrace();
                        canvas.clear();
                        Constant.digit = -1;
                        JOptionPane.showMessageDialog(null, "I can only learn number 0~9");
                    }
                }
            }
        });

        this.jbTrain = new JButton();
        this.jbTrain.setText("train");
        this.jbTrain.setBounds(240, 360, 80, 30);
        this.add(jbTrain);
        this.jbTrain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                java.util.List<String> fileList = ImageUtil.getInstance().getImageList();
                
                if (fileList.size() < 500) {
                    JOptionPane.showMessageDialog(null, "You should create at least 500 train jpg. Try to use \"tell num\".");
                } else {
                    java.util.List<ImageModel> modelList = ImageUtil.getInstance().getImageModel(fileList);
                    
                    network.SGD(modelList, 140, 0.1);
                    try {
						SerializeUtils.serialize(network.getBiasMatrixList(), "datas/biasMatrix.md");
						SerializeUtils.serialize(network.getWeightMatrixList(), "datas/weightMatrix.md");
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
                }
            }
        });

        this.jbTest = new JButton();
        this.jbTest.setText("test");
        this.jbTest.setBounds(340, 360, 80, 30);
        this.add(jbTest);
        this.jbTest.addActionListener(new ActionListener() {
            @SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
            	ArrayList<MyDenseDoubleMatrix2D> weightMatrixList = null;
            	ArrayList<MyDenseDoubleMatrix2D> biasMatrixList = null;
            	try {
            		biasMatrixList = (ArrayList<MyDenseDoubleMatrix2D>) SerializeUtils.deserialization("datas/biasMatrix.md");
            		weightMatrixList = (ArrayList<MyDenseDoubleMatrix2D>) SerializeUtils.deserialization("datas/weightMatrix.md");
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
            	if (weightMatrixList != null && biasMatrixList != null) {
            		network.setBiasMatrixList(biasMatrixList);
            		network.setWeightMatrixList(weightMatrixList);
            	    int[] outline = getOutline();
                    int digit = network.predict(ImageUtil.getInstance().getGrayMatrixFromPanel(canvas, outline));
                    if(digit == -1){
                        JOptionPane.showMessageDialog(null,"I can not recognize this number");
                    }else{
                        JOptionPane.showMessageDialog(null,"I guess this number is:"+digit);
                    }
                
            	}else if(!network.isTrain()){
                    JOptionPane.showMessageDialog(null,"You should train neural network first");
                }
                
            }
        });

        this.setVisible(true);
    }

    public int[] getOutline(){
        double[] grayMatrix = ImageUtil.getInstance().getGrayMatrixFromPanel(canvas, null);
        int[] binaryArray = ImageUtil.getInstance().transGrayToBinaryValue(grayMatrix);
        int minRow = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE;
        int minCol = Integer.MAX_VALUE;
        int maxCol = Integer.MIN_VALUE;
        for(int i=0;i<binaryArray.length;i++){
            int row = i/28;
            int col = i%28;
            if(binaryArray[i] == 0){
                if(minRow > row){
                    minRow = row;
                }
                if(maxRow < row){
                    maxRow = row;
                }
                if(minCol > col){
                    minCol = col;
                }
                if(maxCol < col){
                    maxCol = col;
                }
            }
        }
        int len = Math.max((maxCol-minCol+1)*10, (maxRow-minRow+1)*10);

        int p = 0 ;
        p = (len+40 - (maxCol-minCol+1)*10-20-20)/2;
        if(p<0) p = 0;
        
       int x = minCol*10-20-p ;
       int y = minRow*10-20   ;
       int width = len+40 ;
       if(x<0 || y <0){
    	    x = minCol*10 ;
	        y = minRow*10 ;
	        width = len ;
       } 
       canvas.setOutLine(x, y, width,width );
        
        return new int[]{x, y, width,width};
    }

    public String saveJPanel(int[] outline){
        Dimension imageSize = this.canvas.getSize();
        BufferedImage image = new BufferedImage(imageSize.width,imageSize.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        this.canvas.paint(graphics);
        graphics.dispose();
        try {
            //cut
            if(outline[0] + outline[2] > canvas.getWidth()){
                outline[2] = canvas.getWidth()-outline[0];
            }
            if(outline[1] + outline[3] > canvas.getHeight()){
                outline[3] = canvas.getHeight()-outline[1];
            }
            image = image.getSubimage(outline[0],outline[1],outline[2],outline[3]);
            //resize
            Image smallImage = image.getScaledInstance(Constant.smallWidth, Constant.smallHeight, Image.SCALE_SMOOTH);
            BufferedImage bSmallImage = new BufferedImage(Constant.smallWidth,Constant.smallHeight,BufferedImage.TYPE_INT_RGB);
            Graphics graphics1 = bSmallImage.getGraphics();
            graphics1.drawImage(smallImage, 0, 0, null);
            graphics1.dispose();

            String fileName = String.format("%s/%d_%s.jpg",Constant.trainFolder,Constant.digit,java.util.UUID.randomUUID());
            ImageIO.write(bSmallImage, "jpg", new File(fileName));
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
