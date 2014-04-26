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
	private static void runDescriptorIn(String imgLoc){
		ArrayList<classifiedData> DataOut = null;
		DescriptorBase CEDDBase = new DescriptorBase(imgLoc);
		DataOut = CEDDBase.GetDescriptors();
		Fileout(DataOut);
	}
	
	/**
	 * concats the data in the data object into a string to add to file
	 * @param Data object to be turned to a string 
	 * @return string with data
	 */
	private static String DataToStiring(classifiedData Data){
		
		String out =  Data.getImgName() + ",";
		
		//Add Cedd data
		for(int i = 0; i < Data.getCEDDData().length; i++){
			out = out + Data.getCEDDData()[i] + ",";
		}
		//Add FCTH data
		for(int j = 0; j < Data.getFCTHData().length; j++){
			out = out + Data.getFCTHData()[j] + ",";
		}
		out = out + Data.getClassification();
		  
		return out;
	}
	
	/**
	 * Creates a .arff file that can be read by the random forest out of the CEDD and FCTH data
	 * TODO put the right data in from the FCTH and CEDD searchers 
	 * @return
	 */
	private static void Fileout(ArrayList<classifiedData> Data){
		PrintWriter writer = null;
		
		int CEDDSize = 143;
		int FCTHSize = 192;
		//write to the data file
		try {
			writer = new PrintWriter("src/img/ClassifierData.arff", "UTF-8");
		} catch (FileNotFoundException e) {
			System.err.println("Could not open data file");
		} catch (UnsupportedEncodingException e) {
			System.err.println("Could not encode data to this file");
		}
		//header for .arff file
		writer.println("@RELATION CEDDFCTHDATA");
		writer.println(" ");
		writer.println("@ATTRIBUTE imgName string");
		
		//Write CEDD out
		for(int i = 0; i < CEDDSize; i++){
			writer.println("@ATTRIBUTE CEDDData"+ i + " NUMERIC");
		}
		
		//write FCTH out
		for(int j = 0; j < FCTHSize; j++){
			writer.println("@ATTRIBUTE FCTHData"+ j + " NUMERIC");
		}
		
		
		
		
		//TODO Expand these classifications?
		writer.println("@ATTRIBUTE Classification {Healthy, unHealthy}");
		writer.println(" ");
		writer.println("@DATA");
		//String to hold all of the data for each new line 
		String tmp = null;
		
		//for each object in the data array list
		for(int i = 0; i < Data.size(); i++){
			//fill tmp String with the data
			tmp = DataToStiring(Data.get(i));
			//write it
			writer.println(tmp);
			//TODO HERE note this is prinitng nothing out they are not getting added at some point
			//need to trace back along
		}
				
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
		String TestImageLoc = "src/img/";
		
		//run the CEDD and FCTH descriptors TODO tmp commented out 
//		runCEDDin(TestImageLoc);
//		runFCTHin(TestImageLoc);
		
		System.out.println("Single Classification");
		runDescriptorIn(TestImageLoc);
		
	
		
		

	}
	
}
