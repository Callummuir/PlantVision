#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <iostream>
#include "opencv2/core/core_c.h"
#include "opencv2/core/core.hpp"
#include "opencv2/imgproc/imgproc_c.h"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/video/tracking.hpp"
#include "opencv2/features2d/features2d.hpp"
#include "opencv2/flann/flann.hpp"
#include "opencv2/calib3d/calib3d.hpp"
#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/legacy/compat.hpp"

using namespace cv;
using namespace std;

int main( int argc, char** argv )
{
    
    Mat image1;
    Mat greyImage;
    char* imLoc1 = "/Users/callummuir/Documents/4thYear/Project/Code/ProjectCode/Images/plant1.jpg";
    image1 = imread(imLoc1, CV_LOAD_IMAGE_COLOR);   // Read the file
    
    
    cvtColor( image1, greyImage, CV_BGR2GRAY );
    
    
    
    namedWindow( "Orig", CV_WINDOW_AUTOSIZE );
    namedWindow( "Gray image", CV_WINDOW_AUTOSIZE );
    
    imshow( "Orignal", image1 );
    imshow( "Gray image", greyImage );
    
    
    
    waitKey(0);                                          // Wait for a keystroke in the window
    return 0;
}