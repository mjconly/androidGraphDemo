package com.example.mike.a381_a3;

//Mike Conly

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Controller class for the view and model. Recieves user input from the view and manipulates
 * data in the model and interaction model to provide updates to the views
 */
public class MainGraphViewController implements View.OnTouchListener, View.OnLongClickListener,
        View.OnClickListener {

    GraphModel graphModel;
    InteractionModel iModel;

    //not sure if this is right
    boolean longPress;
    boolean isTempEdge;
    boolean consumed;
    float dx, dy;
    float iDownX, iDownY;
    private State currState;


    /**
     * enumerate controller states
     */
    private enum State {
        READY, CREATE_V, CREATE_E, SELECTED, SCROLL
    }


    /**
     * creates a new instance of the controller
     */
    public MainGraphViewController(){
        currState = State.READY;
        longPress = false;
        isTempEdge = false;
        consumed = true;
    }

    /**
     * sets the model attribute
     * @param m an instance of the model
     */
    public void setModel(GraphModel m){
        this.graphModel = m;
    }

    /**
     * sets the interaction model attribute
     * @param m an intance of the interaction model
     */
    public void setIModel(InteractionModel m){ this.iModel = m;}


    //for clear button
    @Override
    public void onClick(View v) {
        graphModel.clearModelData();
    }


    /**
     * used to consume a "long click" event. If an event is not consumed in the
     * onTouch method below, it will be passed to this method and used. If the event
     * is a "long click" this method will select the vertex that the user has "long clicked"
     * on. This method will consume the event even if it is not a true "long click"
     * @param v the main view
     * @return true to indicated event was consumed
     */
    @Override
    public boolean onLongClick(View v) {
        if (longPress && currState == State.SELECTED){
                longPress = false;
                iModel.setVertexForEdge();
                graphModel.notifySubscribers();
                currState = State.CREATE_E;
                return true;
            }
        return true;
    }

    /**
     * state machine to process the users touch input and update the model accordingly. This method
     * will handle all action performed by the user and update the controller state based off of this
     * input. This allows for objects in the models to be selected, dragged and created
     * @param v main graph view
     * @param event
     * @return true if consumed, false if not
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //set touch x,y with view offsets
        float currX = event.getX() + graphModel.getMainViewX();
        float currY = event.getY() + graphModel.getMainViewY();

        updateMiniFinder();


        switch(currState){
            case READY:
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //set Interaction vertex
                        iModel.setSelectedVertex(graphModel.getVertexOnHit(currX, currY));
                        //Check if background was touched or vertex
                        iDownX = event.getX();
                        iDownY = event.getY();
                        initialDownEvent(event);
                        break;
                    default :
                }
                break;
            case CREATE_V:
                switch(event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //create vertex from action UP
                        graphModel.createVertex(currX, currY);
                        currState = State.READY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        currState = State.SCROLL;
                    default :
                }
                break;
            case SCROLL:
                switch(event.getAction()){
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(iDownX - event.getX()) < 10 && Math.abs(iDownY - event.getY()) < 10){
                            graphModel.createVertex(currX, currY);
                        }
                        currState = State.READY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //scroll the main view
                        scrollMove(event);
                }
                break;
            case SELECTED:
                switch(event.getAction()){
                    case MotionEvent.ACTION_UP:
                        //prevent long click and un-select vertex
                        endVertexMove(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(iDownX - event.getX()) < 10 && Math.abs(iDownY - event.getY()) < 10){
                            longPress = true;
                        }
                        else {
                            //prevent long click and move vertex
                            vertexMove(event);
                        }
                        break;
                }
                break;
            case CREATE_E:
                switch(event.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        //start temp edge
                        dragEdge(currX, currY);
                        break;
                    case MotionEvent.ACTION_UP:
                        //keep edge if connected, drop otherwise
                        endEdgeDrag();
                        break;
                }
                break;
            default :
        }

        return consumed;
    }


    /**
     * updates the mini view finder based on the current view port of the main graph
     */
    public void updateMiniFinder(){
        iModel.miniGraph.setViewPortX(graphModel.getMainViewX());
        iModel.miniGraph.setViewPortY(graphModel.getMainViewY());
        iModel.miniGraph.setViewPortW(graphModel.getMainVisibleW());
        iModel.miniGraph.setViewPortH(graphModel.getMainVisibleH());
    }

    /**
     * helper method to handle user touch down events
     * @param event DOWN
     */
    private void initialDownEvent(MotionEvent event){
        if (iModel.getSelectedVertex() == null){
            currState = State.CREATE_V;
            dx = event.getX();
            dy = event.getY();
        }
        else {
            currState = State.SELECTED;
            //don't consume yet
            consumed = false;
            longPress = true;
            dx = iModel.getCurrVertex().myX - event.getX();
            dy = iModel.getCurrVertex().myY - event.getY();
        }
    }


    /**
     * helper method to scroll the main view when the user touches
     * down on the background and drags
     * @param event MOVE
     */
    private void scrollMove(MotionEvent event){
        dx -= event.getX();
        dy -= event.getY();
        graphModel.scrollMainView(dx , dy);
        dx = event.getX();
        dy = event.getY();

    }


    /**
     * helper method to unselect the vertex that has been dragged
     * @param event UP
     */
    private void endVertexMove(MotionEvent event){
        longPress = false;
        iModel.unselectVertex();
        graphModel.notifySubscribers();
        currState = State.READY;
    }

    /**
     * helper method to move the vertex a user has selected when
     * it is being dragged
     * @param event MOVE
     */
    private void vertexMove(MotionEvent event){
        longPress = false;
        float rawX = event.getX();
        float rawY = event.getY();
        float minBoundByRadius = graphModel.getVertexRadius();
        float maxYBoundByVisibleH = graphModel.getMainVisibleH();
        float maxXBoundByVisibleW = graphModel.getMainVisibleW();

        //keep vertex in visible width
        if (rawX < minBoundByRadius){
            rawX = minBoundByRadius;
        }
        else if (rawX + minBoundByRadius > maxXBoundByVisibleW){
            rawX = maxXBoundByVisibleW - minBoundByRadius;
        }

        //keep vertex in visible height
        if (rawY < minBoundByRadius){
            rawY = minBoundByRadius;
        }
        else if (rawY + minBoundByRadius > maxYBoundByVisibleH){
            rawY = maxYBoundByVisibleH - minBoundByRadius;
        }

        graphModel.moveVertex(iModel.getCurrVertex(), rawX + dx, rawY + dy);
    }

    /**
     * helper method to set the temp edge if it is connected or drop if not
     */
    private void endEdgeDrag(){
        if (iModel.getCurrEdge() == null){
            iModel.unselectVertex();
            graphModel.notifySubscribers();
            currState = State.READY;
        }
        else {
            graphModel.isEdgeToVertex(iModel.currEdge);
            graphModel.addEdgeFrom(iModel.getCurrEdge(), iModel.getCurrVertex());
            iModel.unselectVertex();
            iModel.unselectEdge();
            currState = State.READY;
        }
    }

    /**
     * helper method to drag a temporary edge that the user is drawing
     * @param toX user x coord
     * @param toY user y coord
     */
    private void dragEdge(float toX, float toY){
        if (iModel.getCurrEdge() == null){
            graphModel.createEdge(iModel.getCurrVertex(), toX, toY);
            iModel.setCurrEdge(graphModel.getLastEdge());
        }
        else{
            graphModel.moveEdge(iModel.currEdge, toX, toY);
        }
    }

}

