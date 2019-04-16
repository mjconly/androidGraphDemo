package com.example.mike.a381_a3;

//Mike Conly

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;


/**
 * Main graph view for the user to interact with, displays all verticies and edges in the
 * model. The user can also scroll on this view
 */
public class MainGraphView extends View implements MainGraphViewListener {
    Paint p;
    GraphModel graphModel;
    MainGraphViewController graphController;
    float visibleWidth, visibleHeight;
    float viewX, viewY;

    public MainGraphView(Context c){
        super(c);
        p = new Paint();
        this.setBackgroundColor(Color.parseColor("#263238"));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Edge e : graphModel.getEdges()){
            e.draw(canvas);
        }

        for (Vertex v : graphModel.getVertices()){
            v.draw(canvas);
        }
    }

    /**
     * sets the height and width of the canvas when the view is added to activity
     * @param w current width
     * @param h current height
     * @param oldw previous width
     * @param oldh previous height
     */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        Rect visibleSize = new Rect();
        getLocalVisibleRect(visibleSize);
        visibleWidth = visibleSize.width();
        visibleHeight = visibleSize.height();
        viewX = 0;
        viewY = 0;
    }

    /**
     * scrolls the main graph view port over the 3000 x 3000 dimensions
     * @param dx amount to move the x coord
     * @param dy amount to mvoe the y coord
     */
    public void scrollMainView(float dx, float dy){
        viewX += dx;
        viewY += dy;

        //check that graph stays in bounds (3000 x 3000)
        if(viewX >= 3000 - visibleWidth){
            viewX = 3000 - visibleWidth;
        }
        if(viewX <= 0){
            viewX = 0;
        }
        if(viewY >= 3000 - visibleHeight){
            viewY = 3000 - visibleHeight;
        }
        if(viewY <= 0){
            viewY = 0;
        }
        this.scrollTo((int) viewX, (int) viewY);
    }

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
     * sets the controller attribute and the ontouch and and longtouch
     * events
     * @param c
     */
    public void setGraphController(MainGraphViewController c){
        this.graphController = c;
        this.setOnTouchListener(graphController::onTouch);
        this.setOnLongClickListener(graphController::onLongClick);
    }


}
