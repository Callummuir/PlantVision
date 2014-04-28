package desc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import classifier.classifiedData;
import net.semanticmetadata.lire.imageanalysis.CEDD;
import net.semanticmetadata.lire.imageanalysis.FCTH;
import net.semanticmetadata.lire.utils.FileUtils;


/**
 * Extracts descriptor info for given images for both CEDD and FCTH descriptors
 * @author callummuir
 *
 */
public class DescriptorBase {

	private static String FileLocation; 
	private static File f;
	
	/**
	 * @param FileLoc location of image files
	 */
	public DescriptorBase(String FileLoc){
		FileLocation = FileLoc;
		f = new File(FileLocation);
      
	}
	
	/**
	 * gets all files out of a directory
	 * @param fileLoc directory of files
	 * @return array list of files
	 */
	private ArrayList<String> getFilesFromDirectory(){
        ArrayList<String> imagePaths = null;
        try {
        	imagePaths = FileUtils.getAllImages(new File(FileLocation), true);
		} catch (IOException e) {
			System.err.println("CEDD descriptor: Image files not found");
		}
		return imagePaths;
	}
	

	/**
	 * runs CEDD descriptor on image
	 * @param img to run descriptor on
	 * @return the value of the description vector
	 */
	private double[] GetCedd(BufferedImage img){
		CEDD ceddDescriptor = new CEDD();
		ceddDescriptor.extract(img);
		
		
		return ceddDescriptor.getDoubleHistogram();
	}
	
	/**
	 * runs FCTH descriptor on image
	 * @param img to run descriptor on 
	 * @return the value of the description vector
	 */
	private double[] getFCTH(BufferedImage img){
		FCTH fcthDescriptor = new FCTH();
		fcthDescriptor.extract(img);
		return fcthDescriptor.getDoubleHistogram();
	}
	
	/**
	 * extracts the classification from the file name
	 * 
	 * Classification will be u for unhealthy, h for healthy
	 * 
	 * @param data the file name
	 * @return
	 */
	private String getClassification(String data){
		if(data.charAt(3) == 'h'){
//			System.out.println("HEALTHYYYYY");
			return "Healthy";
		}
		
		if(data.charAt(3) == 'u'){
//			System.out.println("ILLLLLLLLL");
			return "unHealthy";
		}
		
		
		return null;
	}
	
	
	/**
	 * Classifies the image at the given path to a classified object 
	 * @param imageFilePath where the image you want to classify is 
	 * @return classification object with data. If some data is missing then returns null
	 */
	private classifiedData classify(String imageFilePath){
		classifiedData DataIn = new classifiedData();
		
		try {
//			System.out.println("the file output: " + imageFilePath);
         	
			//load the image as a buffered image (works multiple features from a single image)
         	BufferedImage img = ImageIO.read(new FileInputStream(imageFilePath));
         	
            //Assuming only need last 10 chars TODO this will ch
         	//Input name of file unique , removes the path and the .jpg from the file
         	String fileName = imageFilePath.substring((imageFilePath.length() - 8), imageFilePath.length() - 4);
         	
         	//Fill the data object
         	DataIn.putImgName(fileName);
         	DataIn.putClassification(getClassification(fileName));
         	DataIn.putCEDDData(GetCedd(img));
            DataIn.putFCTHData(getFCTH(img));
            
         } catch (Exception e) {
             System.err.println("Error indexing images.");
         }
		
		if(DataIn.isComplete()){
			return DataIn;
		}
		
		return null;
	}
	
	
	/**
	 * Method to get all descriptors from images in a file location
	 * @return array list of data objects
	 */
	public ArrayList<classifiedData> GetDescriptors(){
		
		ArrayList<classifiedData> DataOut = new ArrayList<classifiedData>();
		classifiedData tmp = null;
		
		
		//Get all files from sub-directory
		ArrayList<String> imagePaths = getFilesFromDirectory();
		
		System.out.println("Indexing images in " + FileLocation);
		
        for (Iterator<String> it = imagePaths.iterator(); it.hasNext(); ) {
            String imageFilePath = it.next();
            tmp = classify(imageFilePath);
        	
            if(tmp != null){
            	DataOut.add(tmp);
            }
            
        }
        return DataOut;
		
	}
	
	public ArrayList<classifiedData> GetTestDescriptors(){
		ArrayList<classifiedData> DataOut = new ArrayList<classifiedData>();
		classifiedData tmp = null;
		
		
		//Get all files from sub-directory
		ArrayList<String> imagePaths = getFilesFromDirectory();
		
		System.out.println("Indexing test images in " + FileLocation);
		
        for (Iterator<String> it = imagePaths.iterator(); it.hasNext(); ) {
        	String imageFilePath = it.next();
        	System.out.println("---------------- ----------------- " + imageFilePath);
            
        	tmp = classifytest(imageFilePath);
        	DataOut.add(tmp);
            
            
        }
        return DataOut;
	}
	
	
	
	private classifiedData classifytest(String imageFilePath){
		classifiedData DataIn = new classifiedData();
		
		try {
//			System.out.println("the file output: " + imageFilePath);
         	
			//load the image as a buffered image (works multiple features from a single image)
         	BufferedImage img = ImageIO.read(new FileInputStream(imageFilePath));
         	
            //Assuming only need last 10 chars TODO this will ch
         	//Input name of file unique , removes the path and the .jpg from the file
         	String fileName = imageFilePath.substring((imageFilePath.length() - 8), imageFilePath.length() - 4);
         	
         	//Fill the data object
         	DataIn.putCEDDData(GetCedd(img));
            DataIn.putFCTHData(getFCTH(img));
            DataIn.putClassification("?");
            
         } catch (Exception e) {
             System.err.println("Error indexing images.");
         }
		
		return DataIn;
		
		
		
	}
	
	public classifiedData getDescriptor(){
		classifiedData testData = new classifiedData();
		ArrayList<String> imagePath = getFilesFromDirectory();
		String imageFilePath = imagePath.get(0);
		System.out.println("HEREE: " + imageFilePath);
		
		testData = classifytest(imageFilePath);
		
		System.out.println("classified test data");
		return testData;
	}
	
	
}
