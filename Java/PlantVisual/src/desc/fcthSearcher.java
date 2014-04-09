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
 * Searches through the FCTH descriptors and compares against an image, giving a weighting of how similar they are
 * @author callummuir
 *
 */
public class fcthSearcher {

	private String fileLoc;
	private BufferedImage compImg;
	
	public fcthSearcher(String fileLocation){
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
	 * Takes an image in, and compares against the indexed FCTH descriptor files
	 * @param fileLoc location of file to compare to data 
	 */
	public void Search(String IndexFile){
		
		IndexReader ir;
		
		try {
			//Opens the file already indexed as FCTH
			//Note the //TODO chnage to a variable
			ir = DirectoryReader.open(FSDirectory.open(new File(IndexFile)));
			ImageSearcher searcher = ImageSearcherFactory.createFCTHImageSearcher(10);
		    ImageSearchHits hits = searcher.search(compImg, ir);
		    //For each indexed image
		    for (int i = 0; i < hits.length(); i++) {
		    	//gets the distance to image from indexed images, lower is better
	            String fileName = hits.doc(i).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
	            System.out.println(hits.score(i) + ": \t" + fileName);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
       
	}
	
}
