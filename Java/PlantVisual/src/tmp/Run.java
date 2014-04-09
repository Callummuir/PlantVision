package tmp;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

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
	
	//TODO not sure we need these to return anything
	
	/**
	 * Create a CEDD descriptor and index all of the images in a given directory
	 * @param imgLoc directory of files to be indexed
	 * @return CEDD descriptor object, used for searching
	 */
	private static ceddDiscriptor runCEDDin(String imgLoc){
		
		//name for the index file, need for the searcher (push down the stack?)
		String FCTHIndexFileName = "CEDDINDEX";
		//Create with directory of files
		ceddDiscriptor CEDDDisc = new ceddDiscriptor(imgLoc);
		//index images in the directory
		CEDDDisc.IndexCedd(FCTHIndexFileName);
		return CEDDDisc;
	}
	
	/**
	 * Create a FCTH descriptor and index all of the images in a given directory
	 * @param imgLoc directory of files to be indexed
	 * @return FCTH descriptor object, used for searching
	 */
	private static fcthDiscriptor runFCTHin(String imgLoc){
		//name for the index file, need for the searcher (push down the stack?)
				String FCTHIndexFileName = "FCTHINDEX";
		//create descriptor with the directory for files
		fcthDiscriptor FCTHDisc = new fcthDiscriptor(imgLoc);
		//index images into the directory
		FCTHDisc.indexFCTH(FCTHIndexFileName);
		return FCTHDisc;
	}
	
	/**
	 * Creates a .arff file that can be read by the random forest out of the CEDD and FCTH data
	 * TODO put the right data in from the FCTH and CEDD searchers 
	 * @return
	 */
	private static PrintWriter Fileout(){
		PrintWriter writer = null;
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
		writer.println("@ATTRIBUTE CEDDData NUMERIC");
		writer.println("@ATTRIBUTE FCTHData NUMERIC");
		//TODO Expand these classifications
		writer.println("@ATTRIBUTE Classification {Healthy, unHealthy}");
		writer.println(" ");
		writer.println("@DATA");
		
		//TODO input all the data in here
//		This form
//		http://www.cs.waikato.ac.nz/ml/weka/arff.html
		//for(all of the the data){
		//	read into the file
		//}
		writer.close();
		
		return writer;
	}
	
	public static void main(String[] args){
		//Start program
		System.out.println("Program Start");
		//Load openCV library
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    //location of test images     
		String TestImageLoc = "src/img/";
		
		//run the CEDD and FCTH descriptors 
		runCEDDin(TestImageLoc);
		runFCTHin(TestImageLoc);
		
		// create the data file
		Fileout();

	}
	
}
