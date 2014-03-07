#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>


using namespace cv;
using namespace std;



/**
 * Edge detector, this will detect all edges in the image, use a x-axis kernel
 * and a y-axis kernel, then add the two images magnitudes together to get
 * a full
 *
 * Param:img - image to run the filter over
 * Param:output_x - output image in the x direction
 * Param:output_y - output image in the y direction
 * Param:output_mag - output maginitued image
 * Param:output_dir -
 **/
void sobel(Mat& img, Mat& output_x, Mat& output_y, Mat& output_mag, Mat& output_dir)
{
    output_x.create(img.size(), img.type());
    output_y.create(img.size(), img.type());
    double xkernel[3][3] = {{-1,0,1} ,   {-2,0,2} , {-1,0,1}};
    double ykernel[3][3] = {{-1,-2,-1} , {0,0,0} ,  {1,2,1}};
    
    Mat kernel_x = Mat( 3, 3, CV_64F, xkernel );
    Mat kernel_y = Mat( 3, 3, CV_64F, ykernel );
    
    // we need to create a padded version of the input
	// or there will be border effects
	int kernelRadiusX = 1, kernelRadiusY = 1;
    
	cv::Mat paddedInput;
	cv::copyMakeBorder( img, paddedInput,
                       kernelRadiusX, kernelRadiusX, kernelRadiusY, kernelRadiusY,
                       cv::BORDER_REPLICATE );
    
	// now we can do the convolution
	for ( int i = 0; i < img.rows; i++ )
	{
		for( int j = 0; j < img.cols; j++ )
		{
			double sum_x = 0.0, sum_y = 0.0;
			for( int m = -kernelRadiusX; m <= kernelRadiusX; m++ )
			{
				for( int n = -kernelRadiusY; n <= kernelRadiusY; n++ )
				{
					// find the correct indices we are using
					int imagex = i + 1 + m;
					int imagey = j + 1 + n;
					int kernelx = m + kernelRadiusX;
					int kernely = n + kernelRadiusY;
                    
					// get the values from the padded image and the kernel
					int imageval_x = ( int ) paddedInput.at<uchar>( imagex, imagey );
					double kernalval_x = kernel_x.at<double>( kernelx, kernely );
                    
                    int imageval_y = ( int ) paddedInput.at<uchar>( imagex, imagey );
					double kernalval_y = kernel_y.at<double>( kernelx, kernely );
                    
					// do the multiplication
					sum_x += imageval_x * kernalval_x;
					sum_y += imageval_y * kernalval_y;
				}
			}
			// set the output value as the sum of the convolution
			output_x.at<uchar>(i, j) = (uchar) MIN(255,MAX(0, (sum_x+255)/2));
            output_y.at<uchar>(i, j) = (uchar) MIN(255,MAX(0, (sum_y+255)/2));
		}
	}
    
}


int main( int argc, char** argv )
{
    
    Mat image1 = imread("/Users/callummuir/Documents/4thYear/Project/Code/ProjectCode/Images/plant1.jpg", CV_LOAD_IMAGE_GRAYSCALE);
    Mat image1original;
    image1.copyTo(image1original);
    //	Mat coins2 = imread("images/coins2.png", CV_LOAD_IMAGE_GRAYSCALE);
    //	Mat coins3 = imread("images/coins3.png", CV_LOAD_IMAGE_GRAYSCALE);
    
    GaussianBlur( image1, image1, Size(3,3), 0, 0, BORDER_DEFAULT );
    
    // My sobel
    Mat output_x,output_y,combined;
    sobel(image1,output_x,output_y,output_x,output_x);
    addWeighted( output_x, 0.5, output_y, 0.5, 0, combined );
    
	namedWindow("image 1 Sobel X", CV_WINDOW_AUTOSIZE);
	namedWindow("image 1 Sobel Y", CV_WINDOW_AUTOSIZE);
	namedWindow("image 1 Original", CV_WINDOW_AUTOSIZE);
	namedWindow("image 1 CV::Sobel", CV_WINDOW_AUTOSIZE);
    //	namedWindow("Coins 2", CV_WINDOW_AUTOSIZE);
    //	namedWindow("Coins 3", CV_WINDOW_AUTOSIZE);
    
	imshow("image 1 Sobel X", output_x);
	imshow("image 1 Sobel Y", output_y);
	imshow("image 1 Original", image1original);
    imshow("image 1 CV::Sobel", combined);
    moveWindow("image 1 Sobel X", 0, 400);
    moveWindow("image 1 Sobel Y", 400, 0);
    moveWindow("image 1 CV::Sobel", 400, 400);
    //	imshow("Coins 2", coins2);
    //	imshow("Coins 3", coins3);
    
	waitKey();
    
	return 0;
}
















