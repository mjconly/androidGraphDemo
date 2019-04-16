package com.example.mike.a381_a3;

//Mike Conly


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Represents and edge between two vertices
 */
public class Edge extends Drawable {
    float x1,x2,y1,y2;
    boolean isSet;
    Paint p;
    boolean mini;

    /**
     * Creates a new instance of Edge with x1y1 and x2y2
     * @param fromX float indicating x1 coord
     * @param fromY float indicating y1 coord
     * @param toX float indicating x2 coord
     * @param toY float indicating y2 coord
     */
    public Edge(float fromX, float fromY, float toX, float toY){
        x1 = fromX;
        x2 = toX;
        y1 = fromY;
        y2 = toY;
        isSet = false;
        p = new Paint();
        mini = true;
    }

    /**
     * sets miniFlag to true for drawing to mini view
     */
    private void setMiniFlagTrue(){
        this.mini = true;
    }


    /**
     * sets miniFlag to false for drawing to main view
     */
    private void setMiniFlagFalse(){
        this.mini = false;
    }



    /**
     * updates the x,y coordinates of the temporary edge being drawn
     * @param moveX x coord to move to
     * @param moveY y coord to move to
     */
    public void move(float moveX, float moveY){
        x2 = moveX;
        y2 = moveY;
    }


    /**
     * updates the x1 and y1 coordinate of an edge when the vertext is being dragged
     * @param x an x coord to move to
     * @param y a y coord to move to
     */
    public void moveFirstCoords(float x, float y){
        x1 = x;
        y1 = y;
    }

    /**
     * updates the x2 and y2 coordinate of an edge when the vertext is being dragged
     * @param x an x coord to move to
     * @param y a y coord to move to
     */
    public void moveSecondCoords(float x, float y){
        x2 = x;
        y2 = y;
    }


    /**
     * sets the x2 and y2 coordinate of a temporary edge when a user has dragged the edge into
     * an existing vertex. This will set the edge and it will no longer be temporary and presist when
     * user touches up
     * @param v a Vertex that the user has dragged an edge to
     */
    public void setEdge(Vertex v){
        this.x2 = v.myX;
        this.y2 = v.myY;
        this.isSet = true;
    }

    /**
     * Checks if the temporary edge has reached a vertex
     * @param v a vertex with center x,y and a radius of 100
     * @return true if edge is inside of vertex, false if not
     */
    public boolean checkConnect(Vertex v){
        return Math.sqrt(Math.pow((v.myX - x2), 2) + Math.pow((v.myY - y2), 2)) <= 100;
    }

    /**
     * The edges draw method, will draw to both this mini view and main view
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        if (mini){
            drawToMini(canvas);
        }
        else{
            drawToMain(canvas);
        }
    }

    /**
     * Helper method called from draw, will draw to the main view
     * @param canvas
     */
    private void drawToMain(Canvas canvas){
        p.setStrokeWidth(5);
        canvas.drawLine(x1, y1, x2, y2, p);
        this.setMiniFlagTrue();
    }

    /**
     * Helper method called from draw, will draw to the mini view
     * @param canvas
     */
    private void drawToMini(Canvas canvas){
        p.setStrokeWidth(1);
        canvas.drawLine(x1 * .1333f, y1 * .1333f, x2 * .1333f,
                y2 * .1333f, p);
        this.setMiniFlagFalse();
    }

    /**
     * method required for edge to extend drawable
     * @param alpha
     */
    @Override
    public void setAlpha(int alpha) {
        p.setAlpha(alpha);
    }

    /**
     * method required for edge to extend drawable
     * @param colorFilter
     */
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        p.setColorFilter(colorFilter);
    }


    /**
     * method required for edge to extend drawable
     * @return
     */
    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}