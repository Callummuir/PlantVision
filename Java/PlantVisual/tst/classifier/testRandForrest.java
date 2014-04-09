package classifier;

import org.junit.Test;

public class testRandForrest {

	@Test
	public void testRandForrest(){
		RandForest ranfor = new RandForest();
		String fileLoc = "src/img/ClassifierData.arff";
		ranfor.buildForrest(fileLoc);
		
	}
}
