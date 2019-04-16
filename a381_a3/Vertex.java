package com.example.mike.a381_a3;


//Mike Conly

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * A vertex that is added to the model and drawn to the view. Stores
 * a list of its edges
 */
public class Vertex extends Drawable {
    float myX;
    float myY;
    float myRadius;
    boolean isSelected, settingEdge;
    List<Edge> myEdges;
    Paint p;
    int label;
    boolean mini;

    /**
     * a new vertex is created
     * @param x center x indicated by user input
     * @param y center y indicated by user input
     * @param l label indicating what vertex it is
     */
    public Vertex(float x, float y, int l){
        myX = x;
        myY = y;
        myRadius = 100;
        isSelected = false;
        settingEdge = false;
        p = new Paint();
        label = l;
        myEdges = new ArrayList<>();
        mini = true;
    }

    /**
     * sets mini flag to true if being drawn to the mini view
     */
    private void setMiniFlagTrue(){
        this.mini = true;
    }

    /**
     * sets mini flag to false if being drawn to the main view
     */
    private void setMiniFlagFalse(){
        this.mini = false;
    }



    /**
     * updates the x,y coords of the vertex when it is being dragged
     * @param moveX x coord to move to
     * @param moveY y coord to move to
     */
    public void move(float moveX, float moveY){
        myX = moveX;
        myY = moveY;
    }


    /**
     * indicated if the user has touched the vertex
     * @param x user x coord
     * @param y user y coord
     * @return true if user input is within the vertex, false if not
     */
    public boolean checkSelect(float x, float y){
        return Math.sqrt(Math.pow((x - myX), 2) + Math.pow((y - myY), 2)) <= 100;
    }




    /**
     * add edge to vertex if edge connected to vertex
     * @param e an edge that has a connection to the vertex
     */
    public void addEdgeToVertex(Edge e){
        this.myEdges.add(e);
    }


    /**
     * add edge from vertex if edge originated from vertex and made
     * a connection to another
     * @param e an edge that has a connection to the vertex
     */
    public void addEdgeFromVertex(Edge e){
        this.myEdges.add(e);
    }

    /**
     * draw method for vertex, called from views onDraw
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        if (isSelected){
            p.setColor(Color.parseColor("#ffa726"));
        }
        else {
            p.setColor(Color.parseColor("#42a5f5"));
        }
        if (mini) {
            drawToMini(canvas);
            this.setMiniFlagFalse();
        }
        else {
            drawToMain(canvas);
            this.setMiniFlagTrue();
        }

    }

    /**
     * helper method to draw the vertex to the main graph view
     * @param canvas
     */
    private void drawToMain(Canvas canvas){
        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(myX, myY, myRadius, p);
        p.setColor(Color.WHITE);
        p.setTextSize(35);
        canvas.drawText("V" + label, myX - 20, myY + 15, p);

        if (settingEdge){
            p.setStrokeWidth(10);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(myX, myY, myRadius, p);
        }
    }

    /**
     * helper method to draw the vertex to the mini graph view
     * @param canvas
     */
    private void drawToMini(Canvas canvas){
        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(myX * .1333f, myY * .1333f, myRadius * .1333f, p);

        if (settingEdge){
            p.setStrokeWidth(10);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(myX * .1333f, myY * .1333f, myRadius * .1333f, p);
        }
    }

    /**
     * required to extend drawable
     * @param alpha
     */
    @Override
    public void setAlpha(int alpha) {
        p.setAlpha(alpha);
    }

    /**
     * required to extend drawable
     * @param colorFilter
     */
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        p.setColorFilter(colorFilter);
    }

    /**
     * required to extend drawable
     * @return
     */
    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
