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
 * Simple image retrieval with Lire
 * @author Mathias Lux, mathias <at> juggle <dot> at
 */
public class Searcher {
    public static void main(String[] args) throws IOException {
    	String imgLoc = "src/img/plant1.jpg";
        // Checking if arg[0] is there and if it is an image.
        BufferedImage img = null;
        boolean passed = false;
        if (args.length > 0) {
            File f = new File(args[0]);
            if (f.exists()) {
                try {
                    img = ImageIO.read(f);
                    passed = true;
                } catch (IOException e) {
                    e.printStackTrace();  
                }
            }
        }
        if (!passed) {
            System.out.println("No image given as first argument.");
            System.out.println("Run \"Searcher <query image>\" to search for <query image>.");
            System.exit(1);
        }
 
        IndexReader ir = DirectoryReader.open(FSDirectory.open(new File("index")));
        ImageSearcher searcher = ImageSearcherFactory.createCEDDImageSearcher(10);
 
        ImageSearchHits hits = searcher.search(img, ir);
        for (int i = 0; i < hits.length(); i++) {
            String fileName = hits.doc(i).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
            System.out.println(hits.score(i) + ": \t" + fileName);
        }
    }
}