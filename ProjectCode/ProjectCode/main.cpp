#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>


using namespace cv;
using namespace std;



int count(Mat &segmented);

void my_threshold(Mat &input, Mat &output, int t);

int thresholdOptimisation(Mat &input);


void kmeans(Mat &input, int k, Mat &output);


int main( int argc, char** argv )
{
    
    // load up the input images. Note here that the images are
    // in colour.
    Mat image1 = imread("/Users/callummuir/Documents/4thYear/Project/Code/ProjectCode/Images/plant1.jpg", CV_LOAD_IMAGE_COLOR);

    namedWindow("image 1 Original", CV_WINDOW_AUTOSIZE);
    
    imshow("image 1 Original", image1);
    
	waitKey();
    
	return 0;
    
}









