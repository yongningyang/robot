package com.innsmap.data.util;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by long on 2018/11/20.
 */

public class MathUtil {

    /**
     * 2点之间的距离
     *
     * @param p1
     * @param p2
     * @return
     */
    public static float spacing(PointF p1, PointF p2) {
        float x = p1.x - p2.x;
        float y = p1.y - p2.y;
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 2点之间的距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static float spacing(float x1, float y1, float x2, float y2) {
        float x = x1 - x2;
        float y = y1 - y2;
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 分割一条线段，使之成为点集
     *
     * @param startLength
     * @param stepLength
     * @param startPoint
     * @param endPoint
     * @return
     */
    public static List<PointF> separateLine(float startLength, float stepLength, PointF startPoint, PointF endPoint) {
        List<PointF> list = new ArrayList<PointF>();
        if (startLength <= 0 || stepLength <= 0 || startPoint == null || endPoint == null)
            return list;
        float x1 = startPoint.x;
        float y1 = startPoint.y;
        float x2 = endPoint.x;
        float y2 = endPoint.y;
        float space = spacing(x1, y1, x2, y2);

        float a = x2 - x1; // a=0,与y轴平行
        float b = y2 - y1; // b=0,与x轴平行
        double stepX = 0;
        double stepY = 0;
        double stepStartX = 0;
        double stepStartY = 0;
        if (a == 0 && b == 0) {
            return list;
        } else if (a == 0) {
            stepX = 0;
            stepY = y2 > y1 ? stepLength : -stepLength;
            stepStartX = 0;
            stepStartY = y2 > y1 ? startLength : -startLength;
        } else if (b == 0) {
            stepX = x2 > x1 ? stepLength : -stepLength;
            stepY = 0;
            stepStartX = x2 > x1 ? startLength : -startLength;
            stepStartY = 0;
        } else {
            double angle = 0;
            angle = Math.atan2(b, a);
            double sin = Math.sin(angle);// 手机Y轴与数学Y轴相反造成的
            double cos = Math.cos(angle);
            stepX = stepLength * cos;
            stepY = stepLength * sin;
            stepStartX = startLength * cos;
            stepStartY = startLength * sin;
        }

        int stepNum = 0;
        while (startLength + stepLength * stepNum < space) {
            PointF f = new PointF((float) (x1 + stepStartX + stepX * stepNum), (float) (y1 + stepStartY + stepY * stepNum));
            list.add(f);
            stepNum++;
        }
        return list;

    }
}
