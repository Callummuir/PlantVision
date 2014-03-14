#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>


using namespace cv;
using namespace std;



void splitImage(int regionX, int regionY, Mat& image);


int main( int argc, char** argv )
{
    
    // load up the input images. Note here that the images are
    // in colour.
    Mat image1 = imread("/Users/callummuir/Documents/4thYear/Project/Code/ProjectCode/Images/plant1.jpg", CV_LOAD_IMAGE_COLOR);

    
    int xSize = image1.rows;
    int ySize = image1.cols;
    
    

    
    splitImage(25, 25, image1);
    
//    namedWindow("image 1 Original", CV_WINDOW_AUTOSIZE);
//    imshow("image 1 Original", image1);
    
    cout << "X size " << xSize << endl;
	cout << "Y size " << ySize << endl;
	
    
    waitKey();
    
	return 0;
    
}


void splitImage(int regionXSize, int regionYSize, Mat& image){
    int xregions = image.rows / regionXSize;
    int yregions = image.cols / regionYSize;
    
    int xreg = xregions;
    int yreg = yregions;
    
    bool inRegion = true;
    
    //TODO here, get the pixels in each section to change colour
    
    for(int i = 0; i < image.rows; i++){
        for(int j = 0; j < image.cols; j++){
            if(inRegion == true){
                //cout << "changing values " << endl;
                image.at<cv::Vec3b>(i, j)[0] = 0;
                image.at<cv::Vec3b>(i, j)[1] = 0;
                image.at<cv::Vec3b>(i, j)[2] = 0;
                yreg--;
                if(yreg == 0){
                    yreg = yregions;
                    inRegion = false;
                }
            }else{
                image.at<cv::Vec3b>(i, j)[0] = 255;
                image.at<cv::Vec3b>(i, j)[1] = 255;
                image.at<cv::Vec3b>(i, j)[2] = 255;
                yreg--;
                if(yreg == 0){
                    yreg = yregions;
                    inRegion = true;
                }
            }
        }
        
        namedWindow("image 1 Original", CV_WINDOW_AUTOSIZE);
        imshow("image 1 Original", image);
        
    }
    
    
    cout << "X regions " << xregions << endl;
    cout << "Y regions " << yregions << endl;
    
}









