package com.example.mike.a381_a3;

//Mike Conly

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Model storing all edges and verticies as well as references to the
 * mini and main view (subscribers)
 */
public class GraphModel {
    List<Vertex> vertices;
    List<Edge> edges;
    List<MainGraphViewListener> subscribers;


    /**
     * initializes a new model instance with vertex, edge and subscriber arrays
     * initialized
     */
    public GraphModel(){
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        subscribers = new ArrayList<>();
    }


    /**
     * adds a vertex to the list of vertecies
     * @param v a new vertex to be added to the model
     */
    public void addVertex(Vertex v){
        this.vertices.add(v);
    }


    /**
     * creates a new instance of a vertex with the x and y coordinates from the
     * scrollable view port adjusted so that they are set relative to the
     * main graph (3000 * 3000)
     * @param x an x corrdinate from the main graph view port
     * @param y a y coordinate from the main graph view port
     */
    public void createVertex(float x, float y){
        float vX = getMainViewX();
        float vY = getMainViewY();
        float vW = getMainVisibleW();
        float vH = getMainVisibleH();
        float mX = (x + vX) / 3000;
        float mY = (y + vY) / 3000;


        x = (mX * 3000) - vX;
        y = (mY * 3000) - vY;


        Vertex newVertex = new Vertex(x, y, vertices.size());
        addVertex(newVertex);
        notifySubscribers();
    }


    /**
     * get all vertices in the model
     * @return a list of all vertices
     */
    public List<Vertex> getVertices(){
        return this.vertices;
    }


    /**
     * adds a modelListener (View) to the list of subscribers
     * @param s a view implementing MainGraphViewListener
     */
    public void addSubscribers(MainGraphViewListener s){
        subscribers.add(s);
    }


    /**
     * Calls the onchange() method for each subscriber
     */
    public void notifySubscribers(){
        for (MainGraphViewListener sub : subscribers){
            sub.onChange();
        }
    }


    /**
     * indicates if the user has selected an existing vertex on a touch down
     * @param x coordinate of user touch
     * @param y coordinate of user touch
     * @return the vertex that contains the x,y coordinate or null
     */
    public Vertex getVertexOnHit(float x, float y){
        for (Vertex v : vertices){
            if (v.checkSelect(x, y)){
                notifySubscribers();
                return v;
            }
        }
        return null;
    }

    /**
     * if a temporary edge has been connected to a vertex, the vertex will add
     * the edge to itself
     * @param e a temporary edge being drawn by the user
     */
    public void isEdgeToVertex(Edge e){
        for (Vertex v : vertices){
            if (e.checkConnect(v)){
                //set the final x,y to match vertex
                e.setEdge(v);
                //add this edge to current vertex
                v.addEdgeToVertex(e);
                return;
            }
        }
        removeTempEdge(e);
        notifySubscribers();
    }


    /**
     * adds an edge to a vertex, only called after an edge is confirmed to
     * be connected
     * @param e and edge being drawn
     * @param v a vertex in the model
     */
    public void addEdgeFrom(Edge e, Vertex v){
        if (e.isSet){
            v.addEdgeFromVertex(e);
            notifySubscribers();
        }
    }


    /**
     * get the last edge added to the list of edges, this will be the temporary edge
     * @return the temporary edge
     */
    public Edge getLastEdge(){
        return edges.get(edges.size() - 1);
    }


    /**
     * create a new temp edge starting from the vertex's x,y selected by the user
     * and ending at the users current x,y position
     * @param v the selected vertex
     * @param x current x of user's touch
     * @param y current y of user's touch
     */
    public void createEdge(Vertex v, float x, float y){
        Edge e = new Edge(v.myX, v.myY, x, y);
        addEdge(e);
    }


    /**
     * adds and edge to the list of edges
     * @param e
     */
    public void addEdge(Edge e){
        this.edges.add(e);
        notifySubscribers();
    }


    /**
     * removes the temporary edge from the model
     * @param e
     */
    public void removeTempEdge(Edge e){
        this.edges.remove(e);
    }

    /**
     * get all edges in the model
     * @return a list of edges
     */
    public List<Edge> getEdges(){
        return this.edges;
    }

    /**
     * moves the selected vertex when the user drags it, as well as all edges connected to the
     * vertex
     * @param v the selected vertex
     * @param x the users current x coord
     * @param y the users current y coord
     */
    public void moveVertex(Vertex v, float x, float y){
        float tmpX = v.myX;
        float tmpY = v.myY;

        v.move(x, y);
        for (Edge e : v.myEdges){
            if (e.x1 == tmpX && e.y1 == tmpY){
                e.moveFirstCoords(x, y);
            }
            else {
                e.moveSecondCoords(x, y);
            }
        }
        notifySubscribers();
    }

    /**
     * moves the edge when a vertex is being dragged
     * @param e the edge being dragged by vertex movement
     * @param x users current x coord
     * @param y users current y coord
     */
    public void moveEdge(Edge e, float x, float y){
        e.move(x, y);
        notifySubscribers();
    }

    /**
     * moves the current view port of the main view to the coordinates
     * indicated by user movement
     * @param x users x coord when scrolling
     * @param y users y coord when scrolling
     */
    public void scrollMainView(float x, float y){
        MainGraphView mainView = (MainGraphView) subscribers.get(0);
        mainView.scrollMainView(x, y);
        notifySubscribers();
    }

    /**
     * get left x coord of main view
     * @return left x coord of main
     */
    public float getMainViewX(){
        MainGraphView mainView = (MainGraphView) subscribers.get(0);
        return mainView.viewX;
    }

    /**
     * get top y coord of main view
     * @return top y coord of main
     */
    public float getMainViewY(){
        MainGraphView mainView = (MainGraphView) subscribers.get(0);
        return mainView.viewY;
    }

    /**
     * get visible width of the main view
     * @return width of main view
     */
    public float getMainVisibleW(){
        MainGraphView mainView = (MainGraphView) subscribers.get(0);
        return mainView.visibleWidth;
    }

    /**
     * get visible height of main view
     * @return height of main view
     */
    public float getMainVisibleH(){
        MainGraphView mainView = (MainGraphView) subscribers.get(0);
        return mainView.visibleHeight;
    }

    /**
     * all vertex have the same radius, get the radius of the first one
     * @return radius of all vertex
     */
    public float getVertexRadius(){
        return vertices.get(0).myRadius;
    }

    /**
     * clears the data in the model
     */
    public void clearModelData(){
        vertices.clear();
        edges.clear();
        notifySubscribers();
    }

}
