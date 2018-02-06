package com.yosri.mustafa.eng.bakingapp.Ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yosri.mustafa.eng.bakingapp.Model.Ingredient;
import com.yosri.mustafa.eng.bakingapp.Model.Step;
import com.yosri.mustafa.eng.baking.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetails extends AppCompatActivity {

    private String title;

    private int position;
    private Intent intent;
    private ArrayList<Step> steps;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        intent = getIntent();

        if (intent.hasExtra("step")) {
            steps = (ArrayList<Step>) intent.getSerializableExtra("step");
            position = intent.getExtras().getInt("position");

            title = steps.get(position).getShortDescription();
            Log.e("title", title);
            toolbar.setTitle(title);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.container, StepDetailsFragment.getInstance(steps, position)).commit();
        }
    }



}



