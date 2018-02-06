package com.yosri.mustafa.eng.bakingapp.Ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yosri.mustafa.eng.bakingapp.Adapter.IngredientsRecyclerAdapter;
import com.yosri.mustafa.eng.bakingapp.Adapter.StepsRecyclerAdapter;
import com.yosri.mustafa.eng.bakingapp.Model.Ingredient;
import com.yosri.mustafa.eng.bakingapp.Model.Recipe;
import com.yosri.mustafa.eng.bakingapp.Model.Step;
import com.yosri.mustafa.eng.baking.R;
import com.yosri.mustafa.eng.bakingapp.Utils.NetworkCheck;
import com.yosri.mustafa.eng.bakingapp.data.RecipeDataBaseHelper;
import com.yosri.mustafa.eng.bakingapp.data.RecipeProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeDetails extends AppCompatActivity {
    private IngredientsRecyclerAdapter mIngredientsRecyclerAdapter ;
    private StepsRecyclerAdapter mStepsRecyclerAdapter;
    private ArrayList<Ingredient> ingredientsList = new ArrayList<Ingredient>();
    private ArrayList<Step> stepsList = new ArrayList<Step>();
    private NetworkCheck networkCheck;

    private Toast mToast;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager mlayoutManager;
    private Bundle getSavedInstance;
    private static final String LIST_STATE_KEY = "state";
    private String LOG_TAG = RecipeDetails.class.getSimpleName();
    private Intent intent;
    private Recipe recipe;
    private String title;
    public static  boolean mTwoPane = false;

    @BindView(R.id.toolbar_details) Toolbar toolbar;
boolean m;
    private Intent bundle;
    private int position;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);
        toolbar.setTitle("");
         m =isTablet(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSavedInstance = savedInstanceState;

        if(findViewById(R.id.left)!=null){
            mTwoPane = true;
        }else{
            mTwoPane=false;
        }

        initviews();

    }

    public void getSetpDetails(ArrayList<Step> stepdetails ,int postion) {

        Toast.makeText(this,"getStep Position "+postion+"stap Arry :"+stepdetails.size(),Toast.LENGTH_SHORT)
                .show();

        getSupportFragmentManager().beginTransaction().replace(R.id.right, StepDetailsFragment.
                getInstance(stepdetails,postion)).commit();

    }



    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }



    private void initviews() {
        try {
            bundle = getIntent();
            Log.e("Intent","intent bundle is "+bundle.toString());

            ingredientsList = (ArrayList<Ingredient>) bundle.getSerializableExtra("b");
            Log.e("Intent","intent ing is "+ingredientsList.get(1).getIngredient());

            stepsList = (ArrayList<Step>) bundle.getSerializableExtra("c");
            Log.e("Intent","intent step is "+stepsList.get(1).getShortDescription());

            title = bundle.getExtras().getString("name");

            position = bundle.getExtras().getInt("position");
            Log.e("Intent","intent name is "+title);
            toolbar.setTitle(title);

        } catch (Exception e){
            Log.d(LOG_TAG,"check intent data"+e.getMessage());

        }


        if(!mTwoPane) {
            getSupportFragmentManager().beginTransaction().replace(R.id.ingContainer, RecipeDetailsFragment.
                    getInstance(ingredientsList, stepsList,title)).commit();

        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.left, RecipeDetailsFragment.
                    getInstance(ingredientsList, stepsList,title)).commit();

            getSupportFragmentManager().beginTransaction().replace(R.id.right, StepDetailsFragment.
                    getInstance(stepsList,position)).commit();
        }


    }

}
