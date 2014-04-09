package classifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import weka.core.Instances;
import hr.irb.fastRandomForest.FastRandomForest;

/**
 * creating and working on the random forrest classifier
 * @author callummuir
 *
 */
public class RandForest {
	
	/**
	 * Builds the random forest from the given data file
	 * @param fileLoc location of file for the input file for the classifier
	 */
	public void buildForrest(String fileLoc){
		FastRandomForest rf = new FastRandomForest();
		Instances data = null;
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(fileLoc)); 
			data = new Instances(reader);
			reader.close();
		} catch (IOException e) {
			System.err.println("Buffered reader for Random Forest input could not be created");
		}
		
		//TODO
		//TODO HERE - the input file doesnt work, so it cannot make a random forrest from it
		//TODO
		try {
			rf.buildClassifier(data);
		} catch (Exception e) {
			System.err.println("Could not build Random forrest from data");
		}
	}



	
	
}
