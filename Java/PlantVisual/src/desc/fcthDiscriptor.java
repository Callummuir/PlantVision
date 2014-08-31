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

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.DocumentBuilderFactory;
import net.semanticmetadata.lire.imageanalysis.CEDD;
import net.semanticmetadata.lire.utils.FileUtils;
import net.semanticmetadata.lire.utils.LuceneUtils;

/**
 * Code for making the FCTH descriptor and indexing the given images
 * @author callummuir
 *
 */
public class fcthDiscriptor {
	
	private static String FileLocation; 
	private static File f;
	private static DocumentBuilder builder;
	private IndexWriter indexWriter = null;
	
	
	/**
	 * @param FileLoc The location of the image files to be indexed
	 */
	public fcthDiscriptor(String FileLoc){
		FileLocation = FileLoc;
		File f = new File(FileLocation);
		builder = DocumentBuilderFactory.getFCTHDocumentBuilder();
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
	 * Index the image files using the FCTH document builder, add to the FCTH index
	 */
	public void indexFCTH(String IndexFile){
		System.out.println("Indexing FCTH descriptor");
		System.out.println("Indexing images in " + FileLocation);
        
        //Get all files from sub directory
		ArrayList<String> imagePaths = getFilesFromDirectory();
        
        //create standard config file for the index writer 
        IndexWriterConfig conf = new IndexWriterConfig(LuceneUtils.LUCENE_VERSION, 
        												new WhitespaceAnalyzer(LuceneUtils.LUCENE_VERSION));
        
        try {
        	indexWriter = new IndexWriter(FSDirectory.open(new File(IndexFile)), conf);
        	System.out.println("created FCTH index");
		} catch (IOException e) {
			System.err.println("Could not create FCTH file indexer");
		}
        
        // Iterating through images in the directory, building the FCTH features
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
        
        //close the writer
        try {
        	indexWriter.close();
		} catch (IOException e) {
			System.err.println("Error closing FCTH image indexer.");
		}
        System.out.println("FCTH descriptor: Finished indexing.");
	}
}

