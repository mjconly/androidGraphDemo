package com.example.mike.a381_a3;

//Mike Conly

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

/**
 * Mini graph view (400 x 400), displays all data in the model and is a
 * scaled down version of the main view. The current location of the main
 * graph view port is indicated on the mini view by a white view finder that
 * is scaled down to fit in the mini view
 */
public class MiniGraphView extends View implements MainGraphViewListener  {
    Paint p;
    GraphModel graphModel;
    MainGraphViewController graphController;
    float viewPortX;
    float viewPortY;
    float viewPortW;
    float viewPortH;

    /**
     * creates a new instance of the mini view
     * @param c activity context
     */
    public MiniGraphView(Context c){
        super(c);
        this.p = new Paint();
        this.setBackgroundColor(Color.parseColor("#455a64"));
    }

    /**
     * sets the x coord of the view finder to be scaled down in onDraw
     * @param x left coord of main graph
     */
    public void setViewPortX(float x){
        this.viewPortX = x;
    }

    /**
     * sets the y coord of the view finder to be scaled down in onDraw
     * @param y top coord of main graph
     */
    public void setViewPortY(float y){
        this.viewPortY = y;
    }

    /**
     * sets width of view finder to be scaled down in onDraw
     * @param w width of main graph
     */
    public void setViewPortW(float w){
        this.viewPortW = viewPortX + w;
    }

    /**
     * sets height of view finder to be scaled down in onDraw
     * @param h height of main graph
     */
    public void setViewPortH(float h){
        this.viewPortH = viewPortY + h;
    }


    /**
     * draws model data to scale in the 400 x 400 mini view, also draws the scaled down view
     * finder which represents the main graph view port on its 3000 x 3000 canvas
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.WHITE);
        p.setStrokeWidth(2);
        canvas.drawRect(viewPortX * .1333f, viewPortY * .1333f, viewPortW * .1333f,
                viewPortH * .1333f, p);


        for (Edge e : graphModel.getEdges()){
            e.draw(canvas);
        }

        for (Vertex v : graphModel.getVertices()){
            v.draw(canvas);
        }
    }


    /**
     * forces mini view to redraw
     */
    @Override
    public void onChange() {
        this.invalidate();

    }

    /**
     * sets the model attribute
     * @param m an instance of the model
     */
    public void setModel(GraphModel m){
        this.graphModel = m;
    }


    /**
     * sets the controller attribute
     * @param c an instance of the controller
     */
    public void setGraphController(MainGraphViewController c){
        //no listeners on mini, don't want to draw from here
        this.graphController = c;
    }
}
