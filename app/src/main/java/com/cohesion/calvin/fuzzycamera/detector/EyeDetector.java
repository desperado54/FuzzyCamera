package com.cohesion.calvin.fuzzycamera.detector;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Created by Calvin He on 5/18/2015.
 */
public class EyeDetector {
    public static MatOfRect findPupils(Mat input, CascadeClassifier faceClassifier) {
        int height = input.rows();
        int faceSize = Math.round(height * 0.3F);

        MatOfRect faces = new MatOfRect();
        //faceClassifier.detectMultiScale(input, faces, 1.1, 2, 0, new Size(faceSize, faceSize),new Size());

        faceClassifier.detectMultiScale(input, faces);

        return faces;
        //Mat gray = intermediate;
        //Imgproc.threshold(input, input, 125, 255, Imgproc.THRESH_BINARY);

        //List<MatOfPoint> contours = new ArrayList<>();
        //Imgproc.findContours(input, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        //Imgproc.drawContours(input, contours, -1, new Scalar(255,255,255), -1);
    }
}
