package com.example.mike.a381_a3;

//Mike Conly

/**
 * Interaction models, stores object data that the user is currently
 * working with
 */
public class InteractionModel {
    Vertex currVertex;
    Edge currEdge;
    MiniGraphView miniGraph;

    public InteractionModel(){
        currVertex = null;
        currEdge = null;
    }

    /**
     * adds a reference to the mini to access the mini dimensions
     * @param mini the mini view
     */
    public void setMiniGraphView(MiniGraphView mini){
        miniGraph = mini;
    }


    /**
     * sets the left x coord of the view finder in the mini view
     * @param x current left coord of main view port
     */
    public void setMiniViewPortX(float x){
        miniGraph.setViewPortX(x);
    }

    /**
     * sets the top y coord of the view finder in the mini view
     * @param y current top coord of main view port
     */
    public void setMiniViewPortY(float y){
        miniGraph.setViewPortY(y);
    }


    /**
     * sets the width of the view finder in the mini view
     * @param w the width of the main view port
     */
    public void setMiniViewPortW(float w){
        miniGraph.setViewPortW(w);
    }


    /**
     * sets the height of the view finder in the mini view
     * @param h the height of the main view port
     */
    public void setMiniViewPortH(float h){
        miniGraph.setViewPortH(h);
    }


    /**
     * sets the edge that the user is currently interacting with
     * @param e temp edge
     */
    public void setCurrEdge(Edge e){
        this.currEdge = e;
    }

    /**
     * gets the edge that the user is currently interacting with
     * @return temp edge
     */
    public Edge getCurrEdge(){
        return this.currEdge;
    }

    /**
     * gets the current vertex that the user is interacting with
     * @return the selected vertex
     */
    public Vertex getCurrVertex(){
        return this.currVertex;
    }


    /**
     * sets the vertex that the user has selected
     */
    public void setSelectedVertex(Vertex v){
        if (v != null) {
            v.isSelected = true;
            this.currVertex = v;
        }
    }

    /**
     * removes the selected vertex from the interaction model
     */
    public void unselectVertex(){
        this.currVertex.isSelected = false;
        this.currVertex.settingEdge = false;
        this.currVertex = null;
    }

    /**
     * gets the vertex that the user has selected
     */
    public Vertex getSelectedVertex(){
        return this.currVertex;
    }

    /**
     * sets a flag in the current vertex that the temporary edge
     * being drawn from it has connected to another vertex in the
     * graph
     */
    public void setVertexForEdge(){
        this.currVertex.settingEdge = true;
    }

    /**
     * removes the currently selected edge from the interaction model
     */
    public void unselectEdge(){
        this.currEdge = null;
    }

}

