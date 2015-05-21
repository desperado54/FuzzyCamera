package com.cohesion.calvin.fuzzycamera.detector;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Calvin He on 5/18/2015.
 */
public class ContourDetector {
    public static void applyCanny(Mat input, Mat intermediate){
        Size sizeRgba = input.size();
        int rows = (int) sizeRgba.height;
        int cols = (int) sizeRgba.width;

        int left = cols / 8;
        int top = rows / 8;

        int width = cols * 3 / 4;
        int height = rows * 3 / 4;

        Mat rgbaInnerWindow = input.submat(top, top + height, left, left + width);
        Imgproc.Canny(rgbaInnerWindow, intermediate, 80, 90);
        Imgproc.cvtColor(intermediate, rgbaInnerWindow, Imgproc.COLOR_GRAY2BGRA, 4);
        rgbaInnerWindow.release();

    }
}
