package desc;

import org.junit.Test;
import org.opencv.core.Core;

public class testFCTHDiscriptor {


	@Test
	public void testIndexFcth(){
	
		
		//Start program
		System.out.println("Program Start");
		//Load openCV library
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    //location of test images     
		String TestImageLoc = "src/img/";
				
		fcthDiscriptor FCTHDisc = new fcthDiscriptor(TestImageLoc);
		FCTHDisc.indexFCTH();
		fcthSearcher FCTHSearch = new fcthSearcher();
		FCTHSearch.Search("src/img/plant1.jpg");
		
	}


	
}