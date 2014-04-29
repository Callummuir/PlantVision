package desc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import tmp.OpenCVTest;
import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.DocumentBuilderFactory;
import net.semanticmetadata.lire.imageanalysis.CEDD;
import net.semanticmetadata.lire.utils.FileUtils;
import net.semanticmetadata.lire.utils.LuceneUtils;

/**
 * Code for making the CEDD Descriptor and indexing the given images
 * @author callummuir
 *
 */
public class ceddDiscriptor {
	
	private static String FileLocation; 
	private static File f;
	private static DocumentBuilder builder;
	private IndexWriter indexWriter = null;
	
	/**
	 * @param FileLoc location of the image files to be indexed
	 */
	public ceddDiscriptor(String FileLoc){
		FileLocation = FileLoc;
		f = new File(FileLocation);
        builder = DocumentBuilderFactory.getCEDDDocumentBuilder();
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
	 * Index the image files using the CEDD document builder, add to the CEDD index 
	 * @param indexLoc location of files
	 */
	public void IndexCedd(String indexLoc){
		System.out.println("-----Indexing images for CEDD descriptor in: " + FileLocation + "-----");
		
		//Get all files from sub-directory
		ArrayList<String> imagePaths = getFilesFromDirectory();
		  
        //Create standard config file for the index writer 
        IndexWriterConfig conf = new IndexWriterConfig(LuceneUtils.LUCENE_VERSION, 
        												new WhitespaceAnalyzer(LuceneUtils.LUCENE_VERSION));
        
        try {
        	//Note the filename "CEDDINDEX" is used to differentiate where the index is 
        	indexWriter = new IndexWriter(FSDirectory.open(new File(indexLoc)), conf);
        	System.out.println("created CEDD index");
		} catch (IOException e) {
			System.err.println("Could not create CEDD file indexer");
		}
       
        //Iterate through the images in the directory, building the CEDD features
        for (Iterator<String> it = imagePaths.iterator(); it.hasNext(); ) {
            String imageFilePath = it.next();
            System.out.println("Indexing " + imageFilePath);
            
            try {
            	//load the image as a buffered image (works multiple features from a single image)
            	BufferedImage img = ImageIO.read(new FileInputStream(imageFilePath));
            
            	//create the CEDD document using the document factory, then add to the index
                Document document = builder.createDocument(img, imageFilePath);
                indexWriter.addDocument(document);
                
            } catch (Exception e) {
                System.err.println("Error indexing images.");
            }
        }
        
        //Close the writer
        try {
        	indexWriter.close();
		} catch (IOException e) {
			System.err.println("Error closing CEDD image indexer.");
		}
        System.out.println("CEDD descriptor: Finished indexing.");
		
	}

}
