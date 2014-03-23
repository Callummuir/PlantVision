package tmp;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import desc.ceddDiscriptor;
import desc.ceddSearcher;

public class Run {

	public static void main(String[] args){
		//Start program
		System.out.println("Program Start");
		//Load openCV library
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    //location of test images     
		String TestImageLoc = "src/img/";
		
		ceddDiscriptor CEDDDisc = new ceddDiscriptor(TestImageLoc);
		CEDDDisc.IndexCedd();
		ceddSearcher CEDDSearch = new ceddSearcher();
		CEDDSearch.Search("src/img/plant1.jpg");
		
		
		
	
	}
	
	
}
