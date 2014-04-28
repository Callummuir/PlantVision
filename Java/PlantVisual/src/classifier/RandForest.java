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
		
		
		System.out.println("Building forest");
		
		try {
			//Read in data from the .arff file
			reader = new BufferedReader(new FileReader(fileLoc)); 
			
			data = new Instances(reader);
			
			reader.close();
			data.setClassIndex(data.numAttributes() - 1);
		} catch (IOException e) {
			System.err.println("Buffered reader for Random Forest input could not be created: " + e);
		}
		
		
		try {
			eval = new Evaluation(data);
			Evaluation evalu = new Evaluation(data);
			System.out.println("Crossfold10");
			
			evalu.crossValidateModel(rf, data, 10, new Random(1));
			System.out.println("number correct: " +evalu.correct());
			System.out.println("number incorrect: " +evalu.incorrect());
			System.out.println("percent correct: " + evalu.pctCorrect());
			System.out.println("percent incorrect: " + evalu.pctIncorrect());
			System.out.println("percent NULL: " + evalu.pctUnclassified());
			
			System.out.println("Classifying data");
			rf.buildClassifier(data);
			
		} catch (Exception e) {
			System.err.println("Could not build Random forrest from data: " + e);
		}
		
		
		
		
	}

	public void classifyTest(String fileLoc){
	
		Instances data = null;
		BufferedReader reader = null;
		
		
		System.out.println("Reading in Test data ");
		
		try {
			//Read in data from the .arff file
			reader = new BufferedReader(new FileReader(fileLoc)); 
			
			data = new Instances(reader);
			
			reader.close();
			data.setClassIndex(data.numAttributes() - 1);
		} catch (IOException e) {
			System.err.println("Buffered reader for Random Forest input could not be created: " + e);
		}
		
		System.out.println("[[[[[[[[trying to classify]]]]]]]]pppp]]");
		for(int i = 0 ; i < data.size(); i++){
			try {
				 eval.evaluateModelOnceAndRecordPrediction(rf, data.get(i));
			} catch (Exception e1) {
				System.err.println("Testing not working: " + e1);
			}
			
		}
		 
		 ArrayList<Prediction> pre = eval.predictions();
		 for(int i = 0 ; i < pre.size(); i++){
			 System.out.println("Image: " + i + " is classified as: " + pre.get(i).predicted() + " weighted: " + pre.get(i).weight());

			 
		 }
		 
		 
		 
		 
		
		
	}


	
	
}
