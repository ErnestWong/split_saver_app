package com.noname.splitsaver.ocr;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ImageProcess {
    private Bitmap bitmap;

    public ImageProcess(Bitmap bitmap) {
//        System.loadLibrary("opencv_java");
        this.bitmap = bitmap;
    }
    	/**
	 * converts bitmap to single channel 8 bit Mat
	 *
	 * @param bmp
	 *            bitmap to convert
	 * @return Mat of same size as bmp; 8 channel single bit
	 */
	public static Mat bitmapToMat(Bitmap bmp) {
		Mat mat = new Mat();
		Utils.bitmapToMat(bmp, mat);

		return mat;
	}

	/**
	 * converts mat to RGB bitmap
	 *
	 * @param mat
	 *            mat to convert
	 * @return bitmap of same size as mat, in ARGB8888 format
	 */
	public static Bitmap matToBitmap(Mat mat) {
		Bitmap bmp = Bitmap.createBitmap(mat.cols(), mat.rows(),
				Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(mat, bmp);

		return bmp;
	}

    public static Bitmap process(Bitmap bitmap){
        System.loadLibrary("opencv_java");

        Mat main = bitmapToMat(bitmap);
        Mat rgb = new Mat();

        Imgproc.pyrDown(main, rgb);

        Mat small = new Mat();

        Imgproc.cvtColor(rgb, small, Imgproc.COLOR_RGB2GRAY);

        Mat grad = new Mat();

        Mat morphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3,3));

        Imgproc.morphologyEx(small, grad, Imgproc.MORPH_GRADIENT , morphKernel);

        Mat bw = new Mat();

        Imgproc.threshold(grad, bw, 0.0, 255.0, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        Mat connected = new Mat();

        morphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(9,1));

        Imgproc.morphologyEx(bw, connected, Imgproc.MORPH_CLOSE  , morphKernel);


        Mat mask = Mat.zeros(bw.size(), CvType.CV_8UC1);

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Mat hierarchy = new Mat();

        Imgproc.findContours(connected, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        List<Rect> boundingRects = new ArrayList<Rect>();

        for(int idx = 0; idx < contours.size(); idx++)
        {
            Rect rect = Imgproc.boundingRect(contours.get(idx));

            Mat maskROI = new Mat(mask, rect);
            maskROI.setTo(new Scalar(0, 0, 0));

            Imgproc.drawContours(mask, contours, idx, new Scalar(255, 255, 255), Core.FILLED);

            double r = (double)Core.countNonZero(maskROI)/(rect.width*rect.height);

            if (r > .45 && (rect.height > 8 && rect.width > 8))
            {
//                Imgproc.rectangle(rgb, rect.br() , new Point( rect.br().x-rect.width ,rect.br().y-rect.height),  new Scalar(0, 255, 0));
                double x = rect.br().x- rect.width;
                double y = rect.br().y- rect.height;
                boundingRects.add(new Rect(rect.br(), new Point( rect.br().x-rect.width ,rect.br().y-rect.height)));
                Log.d("Image Process", String.format("x: %d, y: %d", (int)x, (int)y));
            }



        }

        List<Rect> mergedRects = mergeRects(boundingRects);
            for (Rect r1 : mergedRects) {
                Imgproc.rectangle(rgb, r1.br() , new Point( r1.br().x-r1.width ,r1.br().y-r1.height),  new Scalar(0, 255, 0));
            }
        return matToBitmap(rgb);
    }

    private static List<Rect> mergeRects(List<Rect> input) {
        List<Rect> mergedOverlapMatPoints = mergeOverlappingRects(input);
        return mergeRowRects(mergedOverlapMatPoints);
    }

    private static List<Rect> mergeRowRects(List<Rect> input) {
        // merge all who are on same row
        List<Rect> output = new ArrayList<Rect>();
        Set<Rect> visited = new HashSet<Rect>();
        for (Rect rect : input) {
            if (visited.contains(rect)) continue;
            Rect expanded = expandRect(rect, 1.5f);
            List<Rect> containedInRect = findRectsInRect(input, expanded);
            visited.addAll(containedInRect);
            containedInRect.add(rect);
            output.add(combineRects(containedInRect));
        }

        return output;
    }

    private static Rect expandRect(Rect rect, float factor) {
        Point start = new Point(rect.x - (rect.width), (rect.y));
        Point end = new Point(rect.br().x + (rect.width), (rect.br().y));
        return new Rect(start, end);

    }

    private static List<Rect> mergeOverlappingRects(List<Rect> input) {
        //merge all that  are self contained inside
        List<Rect> output = new ArrayList<Rect>();
        Set<Rect> visited = new HashSet<Rect>();

        for (Rect rect : input) {
            if (visited.contains(rect)) continue;
            List<Rect> containedInRect = findRectsInRect(input, rect);
            visited.addAll(containedInRect);
            containedInRect.add(rect);
            output.add(combineRects(containedInRect));
        }

        if (output.size() != input.size()) {
            return mergeOverlappingRects(output);
        } else {
            return output;
        }
    }

    private static List<Rect> findRectsInRect(List<Rect> rects, Rect rect) {
        List<Rect> output = new ArrayList<Rect>();
        for (Rect r : rects) {
            if (rect.contains(r.br()) || rect.contains(r.tl())) {
                output.add(r);
            }
        }
        return output;
    }

    private static Rect combineRects(List<Rect> rects) {
        int startX = Integer.MAX_VALUE;
        int startY = Integer.MAX_VALUE;
        double endX = Integer.MIN_VALUE;
        double endY = Integer.MIN_VALUE;
        for (Rect r : rects) {
            startX = startX < r.x ? startX : r.x;
            startY = startY < r.y ? startY : r.y;
            endX = endX > r.br().x ? endX : r.br().x;
            endY = endY > r.br().y ? endY : r.br().y;
        }

        Point start = new Point(startX, startY);
        Point end = new Point(endX, endY);

        return new Rect(start, end);
    }


//    public static Bitmap process(Bitmap bitmap) {
//        System.loadLibrary("opencv_java");
//        MatOfPoint2f approxCurve = new MatOfPoint2f();
//
//        Mat imgMat = bitmapToMat(bitmap);
//
//        Mat grad = new Mat();
//
//        Imgproc.cvtColor(imgMat, grad, Imgproc.COLOR_BGR2GRAY);
//
//        Imgproc.adaptiveThreshold(imgMat, imgMat,  255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 11, 2);
//        Core.re
//
//		return matToBitmap(imgMat);
//
//        /*
//        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(imgMat.width(), imgMat.height()));
//
//        Imgproc.morphologyEx(imgThreshold, imgThreshold, Imgproc.MORPH_CLOSE, element);
//
//        Imgproc.cvtColor(imgMat, imgMat, Imgproc.COLOR_BGR2GRAY);
//
//
//        List<MatOfPoint> contours = new ArrayList<>();
//        Imgproc.findContours(imgThreshold, contours, new Mat(), Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE, new org.opencv.core.Point(0, 0));
//
//        for (int i = 0; i < contours.size(); i++) {
//
//            //Convert contours(i) from MatOfPoint to MatOfPoint2f
//            MatOfPoint2f contour2f = new MatOfPoint2f( contours.get(i).toArray() );
//
//            //Processing on mMOP2f1 which is in type MatOfPoint2f
//            double approxDistance = Imgproc.arcLength(contour2f, true)*0.02;
//            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);
//
//            //Convert back to MatOfPoint
//            MatOfPoint points = new MatOfPoint( approxCurve.toArray() );
//
//            // Get bounding rect of contour
//            org.opencv.core.Rect rect = Imgproc.boundingRect(points);
//
////            Imgproc.rectangle(imgMAT, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
//        }
//        Utils.matToBitmap(imgMat, bitmap);
//        return bitmap;
//        */
//    }
    	/**
	 * returns undistorted version of Mat using transformation from OpenCV
	 * library
	 *
	 * @param upLeft
	 *            top left corner coordinates
	 * @param upRight
	 *            top right corner coordinates
	 * @param downLeft
	 *            bottom left corner coordinates
	 * @param downRight
	 *            bottom right corner coordinates
	 * @param source
	 *            source Mat
	 * @return
	 */
	public static Mat fixPerspective(Point upLeft, Point upRight,
                                     Point downLeft, Point downRight, Mat source) {
		List<Point> src = new ArrayList<Point>();
		List<Point> dest = new ArrayList<Point>();
		Mat result = new Mat(source.size(), source.type());

		// add the four corners to List
		src.add(upLeft);
		src.add(upRight);
		src.add(downLeft);
		src.add(downRight);

		Point topLeft = new Point(0, 0);
		Point topRight = new Point(source.cols(), 0);
		Point bottomLeft = new Point(0, source.rows());
		Point bottomRight = new Point(source.cols(), source.rows());

		// add destination corners to List (adjusted for rotation)
		dest.add(topRight);
		dest.add(bottomRight);
		dest.add(topLeft);
		dest.add(bottomLeft);

		// convert List to Mat
		Mat srcM = Converters.vector_Point2f_to_Mat(src);
		Mat destM = Converters.vector_Point2f_to_Mat(dest);

		// apply perspective transform using 3x3 matrix
		Mat perspectiveTrans = new Mat(3, 3, CvType.CV_32FC1);
		perspectiveTrans = Imgproc.getPerspectiveTransform(srcM, destM);
		Imgproc.warpPerspective(source, result, perspectiveTrans, result.size());

		return result;
	}

    public Bitmap getBitmap() {
        return bitmap;
    }
}
