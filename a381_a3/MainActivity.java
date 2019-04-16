package com.example.mike.a381_a3;

//Mike Conly

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    MainGraphView viewBig;
    MainGraphViewController controller;
    GraphModel model;
    InteractionModel iModel;
    MiniGraphView viewMini;
    Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //Context cont = this.getApplicationContext();

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);

        LinearLayout topContainer = new LinearLayout(this);

        //layout for button
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);


        //create all objects
        viewBig = new MainGraphView(this);
        controller = new MainGraphViewController();
        model = new GraphModel();
        iModel = new InteractionModel();
        viewMini = new MiniGraphView(this);


        //add references to objects
        viewBig.setGraphController(controller);
        viewBig.setModel(model);
        controller.setModel(model);
        controller.setIModel(iModel);
        model.addSubscribers(viewBig);
        viewMini.setGraphController(controller);
        viewMini.setModel(model);
        model.addSubscribers(viewMini);
        iModel.setMiniGraphView(viewMini);


        //play around with button
        topContainer.addView(viewMini, new LinearLayout.LayoutParams(400,400));
        clear = new Button(this);
        clear.setText("CLEAR");
        clear.setTextSize(20);
        clear.setLayoutParams(lp);
        topContainer.addView(clear);
        clear.setOnClickListener(controller);


//        container.addView(viewMini, new LinearLayout.LayoutParams(400, 400));
        //add views to main container
        container.addView(topContainer);
        container.addView(viewBig, new LinearLayout.LayoutParams(3000, 3000));
        setContentView(container);

    }
}

