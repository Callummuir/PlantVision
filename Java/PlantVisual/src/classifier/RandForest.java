package classifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import hr.irb.fastRandomForest.FastRandomForest;

/**
 * creating and working on the random forrest classifier
 * @author callummuir
 *
 */
public class RandForest {
	
	FastRandomForest rf = new FastRandomForest();
	Evaluation eval;
	
	
	/**
	 * Builds the random forest from the given data file
	 * @param fileLoc location of file for the input file for the classifier
	 */
	public void buildForrest(String fileLoc){
		Instances data = null;
		BufferedReader reader = null;
		
		System.out.println("---------------BUILDING Random Forest Classifier---------------");
		
		try {
			//Read in data from the .arff file
			reader = new BufferedReader(new FileReader(fileLoc)); 
			data = new Instances(reader);
			reader.close();
			//reads in as .arff file 
			
			//Sets the class index (what to classify on) to the last value in the data file, this case {healthy,unhealthy}
			data.setClassIndex(data.numAttributes() - 1);
		} catch (IOException e) {
			System.err.println("Buffered reader for Random Forest input could not be created: " + e);
		}
		
		try {
			//Evaluator with the data, for use when classifying (Generally useful)
			eval = new Evaluation(data);
			//Classify the random forest with the data 
			System.out.println("-----Classifying data in Random Forest-----");
			rf.buildClassifier(data);
			
		} catch (Exception e) {
			System.err.println("Could not build Random forrest from data: " + e);
		}
		
		System.out.println("---------------Random Forest Classifier BUILT---------------");
		
	}
	
	

	
	/**
	 * Tests the data in crossfold10 validation //TODO reqord this comment ?
	 * Must be done after the forrest is built
	 * @param fileLoc location of data file
	 */
	public void crossFold10(String fileLoc){
		Instances data = null;
		BufferedReader reader = null;
		
		System.out.println("---------------Running Crossfold10 Testing Using Random Forrest On Data---------------");
		
		// Need to build data to fit into evaluation
		try {
			//Read in data from the .arff file
			reader = new BufferedReader(new FileReader(fileLoc)); 
			data = new Instances(reader);
			reader.close();
			//Sets the class index (what to classify on) to the last value in the data file, this case {healthy,unhealthy}
			data.setClassIndex(data.numAttributes() - 1);
		} catch (IOException e) {
			System.err.println("Buffered reader for Random Forest input could not be created: " + e);
		}
		
		
		try {
			//evaluate the data 
			Evaluation evalu = new Evaluation(data);

			int folds = 10;
			//runs crossfold validation on the randomforest
			evalu.crossValidateModel(rf, data, folds, new Random(1));
			
			//Output for crossfold 10 
			System.out.println("CrossFold10 classification details: " + evalu.toClassDetailsString());
			System.out.println("CrossFold10 Stats:" + evalu.toSummaryString(true));
			System.out.println("Crossfold10 confusion matrix: " + evalu.toMatrixString());
			
			System.out.println("number of correctly classified images: " +evalu.correct());
			System.out.println("number of incorrectly classified images: " +evalu.incorrect());
			System.out.println("percent of images correctly classified: " + evalu.pctCorrect());
			
			
		} catch (Exception e) {
			System.err.println("Could not build Random forrest from data: " + e);
		}
		
	}

	/**
	 * Classify the test data 
	 * @param fileLoc location of test images 
	 */
	public void classifyTest(String fileLoc){
		Instances data = null;
		BufferedReader reader = null;
		
		System.out.println("Reading in Test data ");
		try {
			//Read in data from the .arff file
			reader = new BufferedReader(new FileReader(fileLoc)); 
			data = new Instances(reader);
			reader.close();
			//Sets the class index (what to classify on) to the last value in the data file, this case {healthy,unhealthy}
			data.setClassIndex(data.numAttributes() - 1);
		} catch (IOException e) {
			System.err.println("Buffered reader for Random Forest input could not be created: " + e);
		}
		
		System.out.println("----------Classifying Test Images----------");

		Evaluation evalu = null;
		try {
			evalu = new Evaluation(data);
			
			//For each test image classify and record data 	
			for(int i = 0 ; i < data.size(); i++){
				//Evaluate it against the training data 
				evalu.evaluateModelOnceAndRecordPrediction(rf, data.get(i));
			}
			
			//Get predictions 
			ArrayList<Prediction> pre = evalu.predictions();
			
			//for each prediction print it out //TODO the weighting is incorrect I think
			for(int i = 0 ; i < pre.size(); i++){
				System.out.println("Image: " + i + " is classified as: " + pre.get(i).predicted() + " weighted: " + pre.get(i).weight());
			}
		} catch (Exception e) {
			System.err.println("Error testing images: " + e);
		}
	}
}


	
	

