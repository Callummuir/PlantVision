package desc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.ImageSearcherFactory;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

/**
 * Searches through the CEDD descriptors and compares against an image, giving a weighting of how similar they are
 * @author callummuir
 *
 */
public class ceddSearcher {
	
	private String fileLoc;
	private BufferedImage compImg;
	
	public ceddSearcher(String fileLocation){
		fileLoc = fileLocation;
		compImg = loadImage();
	}
	
	
	/**
	 * Loads an image from the file location
	 * @return loaded image
	 */
	private BufferedImage loadImage(){
		BufferedImage img = null;
		File f = new File(fileLoc);
		
		// load image from file
		try {
			img = ImageIO.read(f);
		} catch (IOException e) {
			System.err.println("Could not open image file");
		}
		return img;
	}
	
	/**
	 * Takes an image in, and compares against the indexed CEDD descriptor files
	 * @param fileLoc location of image file to compare to data 
	 */
	public void Search(String indexLoc){
		
		IndexReader ir;
		
		try {
			//Opens the files already indexed as CEDD
			ir = DirectoryReader.open(FSDirectory.open(new File(indexLoc)));
			ImageSearcher searcher = ImageSearcherFactory.createCEDDImageSearcher(10);
		    ImageSearchHits hits = searcher.search(compImg, ir);
		    //For each indexed image
		    for (int i = 0; i < hits.length(); i++) {
		    	//gets distance to image lower is better 
	            String fileName = hits.doc(i).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
	            System.out.println(hits.score(i) + ": \t" + fileName);
	        }
		} catch (IOException e) {
			System.err.println("Could not search CEDD descriptor");
		}
       
	}

}
