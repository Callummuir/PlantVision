package tmp;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import classifier.RandForest;
import classifier.classifiedData;
import desc.DescriptorBase;
import desc.ceddDiscriptor;
import desc.ceddSearcher;
import desc.fcthDiscriptor;

/**
 * Run the program in total, i.e. index and search, full menu, should be able to 
 * be taken apart to make something more usable
 * @author callummuir
 *
 */
public class Run {
	
	/**
	 * Create a CEDD descriptor and index all of the images in a given directory
	 * @param imgLoc directory of files to be indexed
	 * @return CEDD descriptor object, used for searching
	 */
	private static void runCEDDin(String imgLoc){
		
		//name for the index file, need for the searcher (push down the stack?)
		String CEDDIndexFileName = "CEDDINDEX";
		//Create with directory of files
		ceddDiscriptor CEDDDisc = new ceddDiscriptor(imgLoc);
		//index images in the directory
		CEDDDisc.IndexCedd(CEDDIndexFileName);
	
	}
	
	/**
	 * Create a FCTH descriptor and index all of the images in a given directory
	 * @param imgLoc directory of files to be indexed
	 * @return FCTH descriptor object, used for searching
	 */
	private static void runFCTHin(String imgLoc){
		//name for the index file, need for the searcher (push down the stack?)
		String FCTHIndexFileName = "FCTHINDEX";
		//create descriptor with the directory for files
		fcthDiscriptor FCTHDisc = new fcthDiscriptor(imgLoc);
		//index images into the directory
		FCTHDisc.indexFCTH(FCTHIndexFileName);
		
	}
	
	/**
	 * gets descriptor info from images, then puts it to file
	 * @param imgLoc location of images folder
	 */
	private static void runDescriptorIn(String imgLoc, String FileOutLoc){
		ArrayList<classifiedData> DataOut = null;
		DescriptorBase DBase = new DescriptorBase(imgLoc);
		DataOut = DBase.GetDescriptors();
		Fileout(DataOut, FileOutLoc);
	}
	
	private static void runDescriptorInTest(String imgLoc, String FileOutLoc){
		System.out.println("Classifying Test Data good times!!");
		ArrayList<classifiedData> DataOut = null;
		DescriptorBase DBase = new DescriptorBase(imgLoc);
		DataOut = DBase.GetTestDescriptors();
		FileTestout(DataOut, FileOutLoc);
	}
	
	
	private static String testDataToStiring(classifiedData Data){

		String out =  "";
		
		//Add Cedd data
		int i = 0;
		for(i = 0; i < Data.getCEDDData().length; i++){
			out = out + Data.getCEDDData()[i] + ",";
		}
//		System.out.println("CEDD data added for image " + Data.getImgName() + " is: " + i);
		
		//Add FCTH data
		out = out + Data.getFCTHData()[0];
		int j = 1;
		for(j = 1; j < Data.getFCTHData().length; j++){
			out = out +"," + Data.getFCTHData()[j]  ;
		}
//		System.out.println("FCTH data added for image " + Data.getImgName() + " is: " + j);
		out = out + ",?";
		  
		return out;
	}
		
	
	
	/**
	 * concats the data in the data object into a string to add to file
	 * @param Data object to be turned to a string 
	 * @return string with data
	 */
	private static String DataToStiring(classifiedData Data){
		
		String out =  "";
		
		//Add Cedd data
		int i = 0;
		for(i = 0; i < Data.getCEDDData().length; i++){
			out = out + Data.getCEDDData()[i] + ",";
		}
//		System.out.println("CEDD data added for image " + Data.getImgName() + " is: " + i);
		
		//Add FCTH data
		int j = 0;
		for(j = 0; j < Data.getFCTHData().length; j++){
			out = out + Data.getFCTHData()[j] + ",";
		}
//		System.out.println("FCTH data added for image " + Data.getImgName() + " is: " + j);
		out = out + Data.getClassification();
		  
		return out;
	}
	
	
	
	private static void FileTestout(ArrayList<classifiedData> Data, String FileOutLoc){
		PrintWriter writer = null;
		
		int CEDDSize = 144;
		int FCTHSize = 192;
		//write to the data file
		try {
			writer = new PrintWriter(FileOutLoc, "UTF-8");
		} catch (FileNotFoundException e) {
			System.err.println("Could not open data file");
		} catch (UnsupportedEncodingException e) {
			System.err.println("Could not encode data to this file");
		}
		//header for .arff file
		writer.println("@RELATION CEDDFCTHTESTDATA");
		writer.println(" ");
		
		
		//Write CEDD out
		int i = 0;
		for(i = 0; i < CEDDSize; i++){
			writer.println("@ATTRIBUTE CEDDData"+ i + " NUMERIC");
		}
//		System.out.println("CEDD @Atts added: " + i);
		
		//write FCTH out
		int j = 0;
		for(j = 0; j < FCTHSize; j++){
			writer.println("@ATTRIBUTE FCTHData"+ j + " NUMERIC");
		}
//		System.out.println("FCTH @Atts added: " + j);
		
		
		
		writer.println("@ATTRIBUTE Classification {Healthy, unHealthy}");
		writer.println(" ");
		writer.println("@DATA");
		//String to hold all of the data for each new line 
		String tmp = null;
		
		//for each object in the data array list
		for(int k = 0; k < Data.size(); k++){
			//fill tmp String with the data
			tmp = testDataToStiring(Data.get(k));
			//write it
			writer.println(tmp);
		}
		System.out.println("Finished Writing to File");
		writer.close();
	}
		
		
	/**
	 * Creates a .arff file that can be read by the random forest out of the CEDD and FCTH data
	 * TODO put the right data in from the FCTH and CEDD searchers 
	 * @return
	 */
	private static void Fileout(ArrayList<classifiedData> Data, String FileOutLoc){
		PrintWriter writer = null;
		
		int CEDDSize = 144;
		int FCTHSize = 192;
		//write to the data file
		try {
			writer = new PrintWriter(FileOutLoc, "UTF-8");
		} catch (FileNotFoundException e) {
			System.err.println("Could not open data file");
		} catch (UnsupportedEncodingException e) {
			System.err.println("Could not encode data to this file");
		}
		//header for .arff file
		writer.println("@RELATION CEDDFCTHDATA");
		writer.println(" ");
		
		
		//Write CEDD out
		int i = 0;
		for(i = 0; i < CEDDSize; i++){
			writer.println("@ATTRIBUTE CEDDData"+ i + " NUMERIC");
		}
//		System.out.println("CEDD @Atts added: " + i);
		
		//write FCTH out
		int j = 0;
		for(j = 0; j < FCTHSize; j++){
			writer.println("@ATTRIBUTE FCTHData"+ j + " NUMERIC");
		}
//		System.out.println("FCTH @Atts added: " + j);
		
		
		
		//TODO Expand these classifications?
		writer.println("@ATTRIBUTE Classification {Healthy, unHealthy}");
		writer.println(" ");
		writer.println("@DATA");
		//String to hold all of the data for each new line 
		String tmp = null;
		
		//for each object in the data array list
		for(int k = 0; k < Data.size(); k++){
			//fill tmp String with the data
			tmp = DataToStiring(Data.get(k));
			//write it
			writer.println(tmp);
		}
		System.out.println("Finished Writing to File");
		writer.close();
		
	}
	

	
	/**
	 * runs the program //TODO better
	 * @param args
	 */
	public static void main(String[] args){
		//Start program
		System.out.println("Program Start");
		//Load openCV library
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    //location of test images     
		String TrainImageLoc = "src/img/";
		String TrainFileLoc = "src/img/ClassifierData.arff";
		String testImageLoc = "src/testimg";
		String testFileLoc = "src/testimg/TestData.arff";
		
		//run the CEDD and FCTH descriptors TODO tmp commented out 
//		runCEDDin(TestImageLoc);
//		runFCTHin(TestImageLoc);
		
		//Train data
		System.out.println("Single Classification");
		runDescriptorIn(TrainImageLoc, TrainFileLoc  );
		
		
		//Test data
		System.out.println("Classify TestData");
//		runDescriptorInTest(testImageLoc, testFileLoc);
		
		
		
		RandForest RFClass = new RandForest();
		RFClass.buildForrest(TrainFileLoc);
		
		//System.out.println("Classifying Testdata");
		RFClass.classifyTest(testFileLoc);
		
		

	}
	
}
