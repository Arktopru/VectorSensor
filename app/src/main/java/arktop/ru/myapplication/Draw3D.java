package arktop.ru.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arcady on 07.03.2017.
 */

public class Draw3D extends View {

    private Paint mPaint = new Paint();
    private float screenWidth;
    private float screenHeight;
    private float xValueFromSensor;
    private float yValueFromSensor;
    private float zValueFromSensor;

    private boolean drawVertex = false;
    private boolean drawFigure = false;
    private boolean drawAxes = false;
    private boolean isSensorOn = false;

    private double xAxisAngle;
    private double yAxisAngle;
    private double zAxisAngle;

    private float axisLength = 300;

    private float centerX;
    private float centerY;

    public Draw3D(Context context) {
        super(context);
    }

    public Draw3D(
            Context context,
            float screenHeight,
            float screenWidth,
            float centerX, 
            float centerY
    ) {
        super(context);
        this.centerX = centerX;
        this.centerY = centerY;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        xValueFromSensor = 0;
        yValueFromSensor = 0;
        zValueFromSensor = 0;
        xAxisAngle = 45;
        yAxisAngle = xAxisAngle + 90;
        zAxisAngle = yAxisAngle + 155;
    }

    /**
     *       270
     *        |
     *        |
     * 180------------- 0
     *        |
     *        |
     *       90
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
//        canvas.drawPaint(mPaint);
        mPaint.setColor(Color.BLUE);

        /* конец оси X */
        float endOfXAxis_X = (float) (centerX + axisLength * Math.cos(Math.toRadians(xAxisAngle)));
        float endOfXAxis_Y = (float) (centerY + axisLength * Math.sin(Math.toRadians(xAxisAngle)));

        /* конец оси Y */
        float endOfYAxis_X = (float) (centerX + axisLength * Math.cos(Math.toRadians(yAxisAngle)));
        float endOfYAxis_Y = (float) (centerY + axisLength * Math.sin(Math.toRadians(yAxisAngle)));

        /* конец оси Z */
        float endOfZAxis_X = (float) (centerX + axisLength * Math.cos(Math.toRadians(zAxisAngle)));
        float endOfZAxis_Y = (float) (centerY + axisLength * Math.sin(Math.toRadians(zAxisAngle)));

        if (isSensorOn) {
            mPaint.setColor(Color.GREEN);
        } else {
            mPaint.setColor(Color.RED);
        }
        //Сенсор включен?
        canvas.drawCircle(centerX - screenWidth / 2.5f, centerY - screenHeight / 2.5f, 20, mPaint);

        //Рисовать оси?
        if (drawAxes) {
            mPaint.setColor(Color.BLUE);
            mPaint.setTextSize(24);
            canvas.drawText("X " + (int) xValueFromSensor, 20, 200, mPaint);
            canvas.drawText("Y " + (int) yValueFromSensor, 20, 230, mPaint);
            canvas.drawText("Z " + (int) zValueFromSensor, 20, 260, mPaint);
            canvas.drawText("x angle " + (int) xAxisAngle, 20, 290, mPaint);
            canvas.drawText("y angle " + (int) yAxisAngle, 20, 320, mPaint);
            canvas.drawText("z angle " + (int) zAxisAngle, 20, 350, mPaint);
            mPaint.setColor(Color.MAGENTA);
            canvas.drawLine(centerX, centerY, endOfXAxis_X, endOfXAxis_Y, mPaint);//X ось
            canvas.drawText("X", endOfXAxis_X - 20, endOfXAxis_Y, mPaint);
            canvas.drawLine(centerX, centerY, endOfYAxis_X, endOfYAxis_Y, mPaint);//Y ось
            canvas.drawText("Y", endOfYAxis_X, endOfYAxis_Y - 10, mPaint);
            canvas.drawLine(centerX, centerY, endOfZAxis_X, endOfZAxis_Y, mPaint);//Z ось
            canvas.drawText("Z", endOfZAxis_X - 20, endOfZAxis_Y + 20, mPaint);
        }

        Map<String, Map<String, Float>> figureMap = getFigureVectorsMap();

        //Та самая вершина, положение которой описывалось
        mPaint.setColor(Color.GREEN);
        canvas.drawCircle(
                figureMap.get("VECTOR_FROM_TOP_OF_FIGURE_FROM_Y_AXIS_PARALLEL_TO_X_AXIS").get("TO_X"),
                figureMap.get("VECTOR_FROM_TOP_OF_FIGURE_FROM_Y_AXIS_PARALLEL_TO_X_AXIS").get("TO_Y"),
                10,
                mPaint);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);

        if (drawVertex) {
            canvas.drawCircle(centerX, centerY, 10, mPaint);

            for (String vectorName : figureMap.keySet()) {
                canvas.drawCircle(
                        figureMap.get(vectorName).get("TO_X"),
                        figureMap.get(vectorName).get("TO_Y"),
                        10,
                        mPaint);
            }
        }

        if (drawFigure) {

            for (String vectorName : figureMap.keySet()) {
                canvas.drawLine(
                        figureMap.get(vectorName).get("FROM_X"),
                        figureMap.get(vectorName).get("FROM_Y"),
                        figureMap.get(vectorName).get("TO_X"),
                        figureMap.get(vectorName).get("TO_Y"),
                        mPaint);
            }
        }
        canvas.restore();
    }

    public void setDrawAxes(boolean drawAxes) {
        this.drawAxes = drawAxes;
    }

    public void setDrawVertex(boolean drawVertex) {
        this.drawVertex = drawVertex;
    }

    public void setDrawFigure(boolean drawFigure) {
        this.drawFigure = drawFigure;
    }

    public void setSensorOn(boolean sensorOn) {
        isSensorOn = sensorOn;
    }

    public void setxValueFromSensor(float xValueFromSensor) {
        this.xValueFromSensor = xValueFromSensor;
    }

    public void setyValueFromSensor(float yValueFromSensor) {
        this.yValueFromSensor = yValueFromSensor;
    }

    public void setzValueFromSensor(float zValueFromSensor) {
        this.zValueFromSensor = zValueFromSensor;
    }

    public void setScreenWidth(float screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setScreenHeight(float screenHeight) {
        this.screenHeight = screenHeight;
    }

    public double getxAxisAngle() {
        return xAxisAngle;
    }

    public double getyAxisAngle() {
        return yAxisAngle;
    }

    public double getzAxisAngle() {
        return zAxisAngle;
    }

    public void setAxisLength(float axisLength) {
        this.axisLength = axisLength;
    }

    public float getAxisLength() {
        return axisLength;
    }

    public void setxAxisAngle(double xAxisAngle) {
        this.xAxisAngle = xAxisAngle;
    }

    public void setyAxisAngle(double yAxisAngle) {
        this.yAxisAngle = yAxisAngle;
    }

    public void setzAxisAngle(double zAxisAngle) {
        this.zAxisAngle = zAxisAngle;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    private Map<String, Map<String, Float>> getFigureVectorsMap() {
        Map<String, Map<String, Float>> figureMap = new HashMap<>();

        Map<String, Float> vector = new HashMap<>();
        //------->
        vector.put("FROM_X", centerX);
        vector.put("FROM_Y", centerY);
        vector.put("TO_X", (float) (centerX + xValueFromSensor * Math.cos(Math.toRadians(xAxisAngle))));
        vector.put("TO_Y", (float) (centerY + xValueFromSensor * Math.sin(Math.toRadians(xAxisAngle))));
        figureMap.put("VECTOR_FROM_CENTER_BY_X_AXIS", vector);
        //z
        //|
        //|       ^
        //|       |
        //|-------|x
        vector = new HashMap<>();
        vector.put("FROM_X", figureMap.get("VECTOR_FROM_CENTER_BY_X_AXIS").get("TO_X"));
        vector.put("FROM_Y", figureMap.get("VECTOR_FROM_CENTER_BY_X_AXIS").get("TO_Y"));
        vector.put("TO_X", (float) (vector.get("FROM_X") + zValueFromSensor * Math.cos(Math.toRadians(zAxisAngle))));
        vector.put("TO_Y", (float) (vector.get("FROM_Y") + zValueFromSensor * Math.sin(Math.toRadians(zAxisAngle))));
        figureMap.put("VECTOR_FROM_X_AXIS_PARALLEL_TO_Z_AXIS", vector);
        //z
        //<------|
        //       |
        //       |
        //-------|x
        vector = new HashMap<>();
        vector.put("FROM_X", figureMap.get("VECTOR_FROM_X_AXIS_PARALLEL_TO_Z_AXIS").get("TO_X"));
        vector.put("FROM_Y", figureMap.get("VECTOR_FROM_X_AXIS_PARALLEL_TO_Z_AXIS").get("TO_Y"));
        vector.put("TO_X", (float) (vector.get("FROM_X") + xValueFromSensor * Math.cos(Math.toRadians(xAxisAngle + 180))));
        vector.put("TO_Y", (float) (vector.get("FROM_Y") + xValueFromSensor * Math.sin(Math.toRadians(xAxisAngle + 180))));
        figureMap.put("VECTOR_TO_Z_AXIS_PARALLEL_TO_X_AXIS", vector);
        //
        //---------/x
        //       /
        //     /
        //   >
        vector = new HashMap<>();
        vector.put("FROM_X", figureMap.get("VECTOR_FROM_CENTER_BY_X_AXIS").get("TO_X"));
        vector.put("FROM_Y", figureMap.get("VECTOR_FROM_CENTER_BY_X_AXIS").get("TO_Y"));
        vector.put("TO_X", (float) (vector.get("FROM_X") + yValueFromSensor * Math.cos(Math.toRadians(yAxisAngle))));
        vector.put("TO_Y", (float) (vector.get("FROM_Y") + yValueFromSensor * Math.sin(Math.toRadians(yAxisAngle))));
        figureMap.put("VECTOR_FROM_X_AXIS_PARALLEL_TO_Y_AXIS", vector);
        //       /
        //     /
        //   /
        // < y
        vector = new HashMap<>();
        vector.put("FROM_X", centerX);
        vector.put("FROM_Y", centerY);
        vector.put("TO_X", (float) (vector.get("FROM_X") + yValueFromSensor * Math.cos(Math.toRadians(yAxisAngle))));
        vector.put("TO_Y", (float) (vector.get("FROM_Y") + yValueFromSensor * Math.sin(Math.toRadians(yAxisAngle))));
        figureMap.put("VECTOR_FROM_CENTER_BY_Y_AXIS", vector);
        //        /
        //      /
        //    /
        // y|--------->
        vector = new HashMap<>();
        vector.put("FROM_X", figureMap.get("VECTOR_FROM_CENTER_BY_Y_AXIS").get("TO_X"));
        vector.put("FROM_Y", figureMap.get("VECTOR_FROM_CENTER_BY_Y_AXIS").get("TO_Y"));
        vector.put("TO_X", (float) (vector.get("FROM_X") + xValueFromSensor * Math.cos(Math.toRadians(xAxisAngle))));
        vector.put("TO_Y", (float) (vector.get("FROM_Y") + xValueFromSensor * Math.sin(Math.toRadians(xAxisAngle))));
        figureMap.put("VECTOR_FROM_Y_AXIS_PARALLEL_TO_X_AXIS", vector);
        // ^
        // |      /
        // |    /
        // |  /
        // |y
        vector = new HashMap<>();
        vector.put("FROM_X", figureMap.get("VECTOR_FROM_CENTER_BY_Y_AXIS").get("TO_X"));
        vector.put("FROM_Y", figureMap.get("VECTOR_FROM_CENTER_BY_Y_AXIS").get("TO_Y"));
        vector.put("TO_X", (float) (vector.get("FROM_X") + zValueFromSensor * Math.cos(Math.toRadians(zAxisAngle))));
        vector.put("TO_Y", (float) (vector.get("FROM_Y") + zValueFromSensor * Math.sin(Math.toRadians(zAxisAngle))));
        figureMap.put("VECTOR_FROM_Y_AXIS_PARALLEL_TO_Z_AXIS", vector);
        // |       /
        // | --------->
        // |    /
        // |  /
        // | y
        vector = new HashMap<>();
        vector.put("FROM_X", figureMap.get("VECTOR_FROM_Y_AXIS_PARALLEL_TO_Z_AXIS").get("TO_X"));
        vector.put("FROM_Y", figureMap.get("VECTOR_FROM_Y_AXIS_PARALLEL_TO_Z_AXIS").get("TO_Y"));
        vector.put("TO_X", (float) (vector.get("FROM_X") + xValueFromSensor * Math.cos(Math.toRadians(xAxisAngle))));
        vector.put("TO_Y", (float) (vector.get("FROM_Y") + xValueFromSensor * Math.sin(Math.toRadians(xAxisAngle))));
        figureMap.put("VECTOR_FROM_TOP_OF_FIGURE_FROM_Y_AXIS_PARALLEL_TO_X_AXIS", vector);
        //                >
        //               /
        //         /   /
        // |---------/
        // |    /
        // |  /
        // | y
        vector = new HashMap<>();
        vector.put("FROM_X", figureMap.get("VECTOR_FROM_TOP_OF_FIGURE_FROM_Y_AXIS_PARALLEL_TO_X_AXIS").get("TO_X"));
        vector.put("FROM_Y", figureMap.get("VECTOR_FROM_TOP_OF_FIGURE_FROM_Y_AXIS_PARALLEL_TO_X_AXIS").get("TO_Y"));
        vector.put("TO_X", (float) (vector.get("FROM_X") + yValueFromSensor * Math.cos(Math.toRadians(yAxisAngle + 180))));
        vector.put("TO_Y", (float) (vector.get("FROM_Y") + yValueFromSensor * Math.sin(Math.toRadians(yAxisAngle + 180))));
        figureMap.put("VECTOR_FROM_TOP_OF_FIGURE_FROM_CLOSEST_VERTEX_TO_X_AXIS", vector);
        //               /
        //         /   /
        // |---------/
        // |    /    |
        // |  /      |
        // | y       <
        vector = new HashMap<>();
        vector.put("FROM_X", figureMap.get("VECTOR_FROM_TOP_OF_FIGURE_FROM_Y_AXIS_PARALLEL_TO_X_AXIS").get("TO_X"));
        vector.put("FROM_Y", figureMap.get("VECTOR_FROM_TOP_OF_FIGURE_FROM_Y_AXIS_PARALLEL_TO_X_AXIS").get("TO_Y"));
        vector.put("TO_X", (float) (vector.get("FROM_X") + zValueFromSensor * Math.cos(Math.toRadians(zAxisAngle + 180))));
        vector.put("TO_Y", (float) (vector.get("FROM_Y") + zValueFromSensor * Math.sin(Math.toRadians(zAxisAngle + 180))));
        figureMap.put("VECTOR_FROM_TOP_OF_FIGURE_FROM_CLOSEST_VERTEX_TO_BOTTOM_CLOSEST_VERTEX", vector);
        //       >
        //     /
        //   /
        // /
        // |
        // |    /
        // |  /
        // |/y
        vector = new HashMap<>();
        vector.put("FROM_X", figureMap.get("VECTOR_FROM_Y_AXIS_PARALLEL_TO_Z_AXIS").get("TO_X"));
        vector.put("FROM_Y", figureMap.get("VECTOR_FROM_Y_AXIS_PARALLEL_TO_Z_AXIS").get("TO_Y"));
        vector.put("TO_X", (float) (vector.get("FROM_X") + yValueFromSensor * Math.cos(Math.toRadians(yAxisAngle + 180))));
        vector.put("TO_Y", (float) (vector.get("FROM_Y") + yValueFromSensor * Math.sin(Math.toRadians(yAxisAngle + 180))));
        figureMap.put("VECTOR_FROM_TOP_OF_FIGURE_Y_AXIS_TO_Z_AXIS", vector);
        //    z
        //    ^
        //    |
        //    |_______x
        //   /
        // /y
        vector = new HashMap<>();
        vector.put("FROM_X", centerX);
        vector.put("FROM_Y", centerY);
        vector.put("TO_X", (float) (vector.get("FROM_X") + zValueFromSensor * Math.cos(Math.toRadians(zAxisAngle))));
        vector.put("TO_Y", (float) (vector.get("FROM_Y") + zValueFromSensor * Math.sin(Math.toRadians(zAxisAngle))));
        figureMap.put("VECTOR_FROM_CENTER_BY_Z_AXIS", vector);
        return figureMap;
    }
}