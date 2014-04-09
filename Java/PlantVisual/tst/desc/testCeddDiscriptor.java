package desc;

import org.junit.Test;
import org.opencv.core.Core;

public class testCeddDiscriptor {

	
	@Test
	public void testIndexCedd(){
		//Start program
		System.out.println("Program Start");
		//Load openCV library
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    //location of test images     
		String TestImageLoc = "src/img/";
				
		ceddDiscriptor CEDDDisc = new ceddDiscriptor(TestImageLoc);
		CEDDDisc.IndexCedd("CEDDINDEX");
		ceddSearcher CEDDSearch = new ceddSearcher("src/img/plant1.jpg");
		CEDDSearch.Search("CEDDINDEX");
	}
	
}
