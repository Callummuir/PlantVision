package classifier;

/**
 * This holds all of the data for classification using the full descriptor data
 * @author callummuir
 *
 */
public class classifiedData {
	
	private String imgName = null;
	private double CEDDData[] = null;
	private double FCTHData[] = null;
	private String classification = null;
	
	/**
	 * @return This image name
	 */
	public String getImgName(){
		return imgName;
	}
	
	/**
	 * @return the value for the CEDD descriptor
	 */
	public double[] getCEDDData(){
		return CEDDData;
	}
	
	/**
	 * @return the value for the FCTH descriptor
	 */
	public double[] getFCTHData(){
		return FCTHData;
	}
	
	/**
	 * @return The classification of the image, healthy not healthy etc
	 */
	public String getClassification(){
		return classification;
	}
	
	/**
	 * @param assigns imgname
	 */
	public void putImgName(String data){
		imgName = data;
	}
	
	/**
	 * 
	 * @param assigns CEDD descriptor data
	 */
	public void putCEDDData(double[] data){
		CEDDData = data;
	}
	
	/**
	 * @param assigns FCTH descriptor data 
	 */
	public void putFCTHData(double[] data){
		FCTHData = data;
	}
	
	/**
	 * @param assigns the classification for the data
	 */
	public void putClassification(String data){
		classification = data;
	}
	
	/**
	 * @return true if the object has the full complement of data
	 */
	public boolean isComplete(){
		if(imgName != null && CEDDData != null && FCTHData != null && classification != null){
			return true;
		}
		return false;
	}

}
