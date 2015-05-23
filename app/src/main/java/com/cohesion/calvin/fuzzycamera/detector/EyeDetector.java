package com.cohesion.calvin.fuzzycamera.detector;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Calvin He on 5/18/2015.
 */
public class EyeDetector {
    public static MatOfRect findFaces(Mat input, CascadeClassifier faceClassifier) {
        MatOfRect faces = new MatOfRect();
        Mat temp = new Mat();
        Imgproc.cvtColor(input, temp, Imgproc.COLOR_RGBA2GRAY);
        int iteration = (int)Math.log((double)(input.rows() / 128));
        for(int i = 0; i < iteration; i++) {
            Imgproc.pyrDown(temp, temp);
        }
        int b = temp.rows();

        faceClassifier.detectMultiScale(temp, faces);

        double rScale = Math.pow(2.0d, (double) iteration);

        for (Rect rect : faces.toArray()) {
            Point p1 = new Point(rect.x * rScale, rect.y * rScale);
            Point p2 = new Point(p1.x + rect.width * rScale, p1.y + rect.height * rScale);
            Core.rectangle(input, p1, p2, new Scalar(0, 255, 0, 255));
        }
        return faces;
    }

    public static MatOfRect findEyes(Mat input, CascadeClassifier eyesClassifier) {
        MatOfRect eyes = new MatOfRect();
        //Mat temp = new Mat();
        Mat temp = input.clone();
        //Imgproc.cvtColor(input, temp, Imgproc.COLOR_RGBA2GRAY);
        int scalar = closestpower2((input.rows() / 256));
        scalar = scalar == 0 ? 1 : scalar;

        int iteration = (int)(Math.log(scalar) / Math.log(2));
        for(int i = 0; i < iteration; i++) {
            Imgproc.pyrDown(temp, temp);
        }

        eyesClassifier.detectMultiScale(temp, eyes);

        Rect[] rects = eyes.toArray();
        if(rects.length > 0) {
            int mss = 0;
            int idx = 0;
            Rect rect;
            for(int i = 0; i < rects.length; i++){
                rect = rects[i];
                int ss = rect.height * rect.width;
                if(ss > mss){
                    idx = i;
                    mss = ss;
                }
            }
            rect = rects[idx];
//            Point p1 = new Point(rect.x * scalar, rect.y * scalar);
//            Point p2 = new Point(p1.x + rect.width * scalar, p1.y + rect.height * scalar);
            //Core.rectangle(input, p1, p2, new Scalar(0, 255, 0, 255));

//            Mat ea = input.submat(new Rect(p1, p2));
            Mat ea = temp.submat(rect);
            Mat gray = new Mat();
            Imgproc.cvtColor(ea, gray, Imgproc.COLOR_RGBA2GRAY);
            Imgproc.threshold(gray, gray, 65, 255, Imgproc.THRESH_BINARY_INV);

            List<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(gray.clone(), contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
            Imgproc.drawContours(gray, contours, -1, new Scalar(255, 255, 255), -1);

            double pp = (ea.rows() * ea.height()) / 32;
            for (int i = 0; i < contours.size(); i++) {
                double area = Imgproc.contourArea(contours.get(i));
                Rect br = Imgproc.boundingRect(contours.get(i));
                int radius = rect.height * scalar / 2;

                // Look for round shaped blob
                if (area >= pp) {
                    //Core.rectangle(ea, rect.tl(), rect.br(), new Scalar(0, 255, 0, 255));
                    Point p1 = new Point((rect.x + br.x + br.width / 2)  * scalar , (rect.y + br.y + br.height / 2) * scalar);
                    Core.circle(input, p1, radius, new Scalar(255, 0, 0), 2);
                }
            }

        }

        return eyes;
    }

    public static void findPupils(Mat input) {
        Imgproc.threshold(input, input, 220, 255, Imgproc.THRESH_BINARY);

    }
    public static int closestpower2(int v) {
        v--;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        v++;
        v >>= 1;
        return v;
    }
}
