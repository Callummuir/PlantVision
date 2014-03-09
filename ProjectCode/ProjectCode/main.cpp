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
    

    // here we convert the "cells" image to grayscale for the
    // first task. You will need to do this for the penguins image too...
    Mat imageGray, image_threshold;
    cvtColor(image1, imageGray, CV_RGB2GRAY);

    
    namedWindow("image 1 Original", CV_WINDOW_AUTOSIZE);
    namedWindow("image 1 gray", CV_WINDOW_AUTOSIZE);

    cout << "~~~ THRESHOLD OPTIMISATION ~~~" << endl;
    
    int t = thresholdOptimisation(image_threshold);
    my_threshold(imageGray,image_threshold,t);
    
    imshow("image 1 Original", image1);
	imshow("image 1 gray", imageGray);
    
	waitKey();
    
	return 0;
    
    /*
     
     my_threshold(penguinGray,penguins_threshold,t);
     int cell_count = count(cells_threshold);
     int penguin_count = count(penguins_threshold);
     cout << "CELLS COUNTED: " << cell_count << endl;
     cout << "PENGUINS COUNTED: " << penguin_count << endl;
     // create the windows
     namedWindow("Cells", CV_WINDOW_AUTOSIZE);
     namedWindow("Penguins", CV_WINDOW_AUTOSIZE);
     //namedWindow("Penguins", CV_WINDOW_AUTOSIZE);
     
     //display the images
     imshow("Cells", cells);
     imshow("Cells Threshold", cells_threshold);
     imshow("Penguins", penguins);
     imshow("Penguinss Threshold", penguins_threshold);
     //imshow("Penguins", penguins);
     
     cout << "~~~ KMEANS ~~~" << endl;
     Mat cells_kmeans, penguins_kmeans, cells_kmeans_gray, penguins_kmeans_gray;
     kmeans(cells,4,cells_kmeans);
     imshow("Cells KM", cells_kmeans);
     kmeans(penguins,4,penguins_kmeans);
     imshow("Penguins KM", penguins_kmeans);
     
     cvtColor(cells_kmeans, cells_kmeans_gray, CV_RGB2GRAY);
     cvtColor(penguins_kmeans, penguins_kmeans_gray, CV_RGB2GRAY);
     cell_count = count(cells_kmeans_gray);
     penguin_count = count(penguins_kmeans_gray);
     cout << "CELLS COUNTED: " << cell_count << endl;
     cout << "PENGUINS COUNTED: " << penguin_count << endl;
     
     waitKey();
     return 0;
     */
    
}



void kmeans(Mat &input, int k, Mat &output)
{
	input.copyTo(output);
	int x = input.size().width, y = input.size().height;
    
	// create matrix to store our pixel classifications in
	int** classifier = (int**)malloc(x * sizeof(int*));
	for(int i = 0; i < x; i++){
		classifier[i] = (int*)malloc(y * sizeof(int));
	}
    
	// pick k random points
    //is this needed?
	srand ( time(NULL) );
	int** k_m = (int**)malloc(k * sizeof(int*));
    
	std::cout << "RANDOM K VECTORS: " << std::endl;
	for(int i = 0; i < k; i++){
		// TODO: generate 3 (or more) rounds of randomness then mean them together.
		k_m[i] = (int*)malloc(3 * sizeof(int));
		k_m[i][0] = rand() % 255;
		k_m[i][1] = rand() % 255;
		k_m[i][2] = rand() % 255;
		std::cout << k_m[i][0] << " " << k_m[i][1] << " " << k_m[i][2] << " " << std::endl;
	}
	
	// Make storage for previous rounds k-means.
	int** k_m_old = (int**)malloc(k * sizeof(int*));
    
	// Convergence loop
	while (true) {
		// Store previous round's k-means
		for(int i = 0; i < k; i++){
			k_m_old[i] = (int*)malloc(3 * sizeof(int));
			k_m_old[i][0] = k_m[i][0];
			k_m_old[i][1] = k_m[i][1];
			k_m_old[i][2] = k_m[i][2];
		}
        
		// k_dist stores the 3D distance from a point to each mean (3*k)
		float** k_dist = (float**)malloc(k * sizeof(float*));
		for(int i = 0; i < k; i++){
			k_dist[i] = (float*)malloc(3 * sizeof(float));
		}
		// k_mag stores the euclidean distance from a point to each mean (k)
		float* k_mag = (float*)malloc(k *sizeof(float));
        
		// maintains the total number of pixels classified as k.
		int* k_total = (int*)calloc(k,sizeof(int));
        
		Vec3b p;
		for(int i = 0; i < x; i++){
			for(int j = 0; j < y; j++){
				// Store current pixel
				p = input.at<Vec3b>(j,i);
				for(int curr_k = 0; curr_k < k; curr_k++){
					for(int rgb = 0; rgb < 3; rgb++){
						// Store distance to each mean.
						k_dist[curr_k][rgb] = (float)p[rgb] - (float)k_m[curr_k][rgb];
						k_dist[curr_k][rgb] = (float)p[rgb] - (float)k_m[curr_k][rgb];
						k_dist[curr_k][rgb] = (float)p[rgb] - (float)k_m[curr_k][rgb];
                        
						// Store Eucledian distance to each mean.
						k_mag[curr_k] = sqrt(k_dist[curr_k][0]*k_dist[curr_k][0]
                                             + k_dist[curr_k][1]*k_dist[curr_k][1]
                                             + k_dist[curr_k][2]*k_dist[curr_k][2]);
					}
				}
				// Work out which mean our pixel is nearest to in RGB space.
				int classification;
				// Initialise minimum to a number it is guaranteed to be smaller than.
				float min = (float)(x+y);
				for(int curr_k = 0; curr_k < k; curr_k++){
					if(k_mag[curr_k] < min){
						min = k_mag[curr_k];
						classification = curr_k;
					}
				}
				classifier[i][j] = classification;
			}
		}
		for(int i = 0; i < x; i++){
			for(int j = 0; j < y; j++){
				p = input.at<Vec3b>(j,i);
				// Count total pixels for each mean
				k_total[classifier[i][j]]++;
				// Sum pixel values for each mean
				k_m[classifier[i][j]][0] += p[0];
				k_m[classifier[i][j]][1] += p[1];
				k_m[classifier[i][j]][2] += p[2];
			}
		}
		std::cout << "CURRENT K-TOTAL: ";
		for(int z = 0; z < k; z++){
			std::cout << k_total[z] << " ";
		}
		std::cout << std::endl;
		for(int curr_k = 0; curr_k < k; curr_k++){
			// Adding one prevents us ever dividing by zero
			k_m[curr_k][0] = k_m[curr_k][0] / (k_total[curr_k]+1);
			k_m[curr_k][1] = k_m[curr_k][1] / (k_total[curr_k]+1);
			k_m[curr_k][2] = k_m[curr_k][2] / (k_total[curr_k]+1);
		}
		// Break out if we have converged
		bool moved = false;
		for(int i = 0; i < k; i++){
			if(k_m_old[i][0] != k_m[i][0] || k_m_old[i][1] != k_m[i][1] ||	k_m_old[i][2] != k_m[i][2]){
				moved = true;
			}
		}
		if(moved == false){
            std::cout << "FINAL K-TOTAL: ";
            for(int z = 0; z < k; z++){
                std::cout << k_total[z] << " ";
            }
            std::cout << std::endl;
            break;
		}
	}
	// Output colourised image
	for(int i = 0; i < x; i++){
		for(int j = 0; j < y; j++){
			uchar colour = 255 / (k-1) * classifier[i][j];
			output.at<Vec3b>(j,i)[0] = colour;
			output.at<Vec3b>(j,i)[1] = colour;
			output.at<Vec3b>(j,i)[2] = colour;
            //Giving errors, apparently not used
//			int rgb_k = classifier[i][j] % 3;
//			int rgb_k_2 = classifier[i][j]+1 % 3;	
		}
	}
}


// Returns the optimum thresholding point of an input image
int thresholdOptimisation(Mat &input){
	/*
     APPROACH:
     1. Select and initialise threshold Tn
     2. Segment the image using Tn and produce groups Go and Gb which represent the object and
     background respectively
     3. Compute the means m1 and m2 of the groups
     4. Compute new threshold Tn+1 = (m1 + m2) /2
     5. If Tn != Tn+1 set Tn = Tn+1 and repeat
     */

	Size s = input.size();
	int width = s.width;
	int height = s.height;
	// Allocate 1D array to store matrix values for easy sorting
	uchar* input_array = (uchar*)malloc((width)*(height)*sizeof(uchar));
	
	// Fill array with matrix values
	for(int x = 0; x < width; x++){
		for(int y = 0; y < height; y++){
			input_array[x+y*x] = input.at<uchar>(y,x);
		}
	}
    
	// Sorts array
	int elements = sizeof(input_array)/sizeof(input_array[0]);
	sort(input_array,input_array + elements);
    
	// Initialise first threshold guess with median brightness
	int Tn = (int)input_array[elements/2], Tn2 = 1;
    
	// Loops until convergence
	while(Tn != Tn2){
		cout << "Tn = " << Tn << endl;
		int m1 = 0, m2 = 0, i;
		// Compute the mean for the bottom group, m1
		for(i = 0; (int)input_array[i] < Tn; i++){
			m1 = m1 + (int)input_array[i];
		}
		m1 = m1/(i+1);
		// Compute the mean for the top group, m2
		for(i = 0; (int)input_array[elements-1-i] >= Tn; i++){
			m2 = m2 + (int)input_array[elements-1-i];
		}
		m2 = m2/(i+1);
        
		// Recompute Tn2
		Tn2 = Tn;
		Tn = (m1 + m2) / 2;
	}
	cout << "FINAL THRESHOLD: Tn = " << Tn << endl;
	
	return Tn;
}


// Takes an input image and output address. Outputs the thresholded image.
// Uses adaptive thresholding to find the optimum thresholding point
void my_threshold(Mat &input, Mat &output, int t){
	input.copyTo(output);
	int width = input.size().width;
	int height = input.size().height;
    
	for(int x = 0; x < width; x++){
		for(int y = 0; y < height; y++){
            if(output.at<uchar>(y,x) >= t){
				output.at<uchar>(y,x) = (uchar)255;
			} else {
				output.at<uchar>(y,x) = (uchar)0;
			}
		}
	}
}


/*
 * You can use this function to count the number of distinct areas of
 * pixel value 255 in a segmented image.
 * It takes a segmented image and returns the number of regions
 *
 * NOTE: make sure that the regions you want to count have intensity
 *       values of 255. This is because the counting operation will only
 *       count regions of this value.
 */
int count(Mat &segmented)
{
	// get a random number generator
	RNG rng(12345);
    
	// threshold the image to only show the objects to be counted
	Mat thresholdedImage;
	threshold(segmented, thresholdedImage, 254, 255, CV_THRESH_BINARY);
	
	// find the contours of the objects in the image
	vector<vector<Point> > contours;
	vector<Vec4i> hierarchy;
	findContours(thresholdedImage, contours, hierarchy, CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
    
	//return the number of contours
	return (int) contours.size();
}













