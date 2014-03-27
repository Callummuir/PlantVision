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


public class fcthSearcher {

	/**
	 * Takes a file in and compares it through a FCTH descriptor TODO better comment
	 * 
	 * @param fileLoc location of file to comapare to data 
	 */
	public void Search(String fileLoc){
		BufferedImage img = null;
		File f = new File(fileLoc);
		try {
			img = ImageIO.read(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IndexReader ir;
		try {
			ir = DirectoryReader.open(FSDirectory.open(new File("FCTHINDEX")));
			ImageSearcher searcher = ImageSearcherFactory.createFCTHImageSearcher(10);
			
		    ImageSearchHits hits = searcher.search(img, ir);
		    for (int i = 0; i < hits.length(); i++) {
	            String fileName = hits.doc(i).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
	            System.out.println(hits.score(i) + ": \t" + fileName);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
       
		
		
	}
	
}















