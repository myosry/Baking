package com.yosri.mustafa.eng.bakingapp.Ui;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.yosri.mustafa.eng.baking.R;
import com.yosri.mustafa.eng.bakingapp.Adapter.IngredientsRecyclerAdapter;
import com.yosri.mustafa.eng.bakingapp.Adapter.StepsRecyclerAdapter;
import com.yosri.mustafa.eng.bakingapp.Model.Ingredient;
import com.yosri.mustafa.eng.bakingapp.Model.Step;
import com.yosri.mustafa.eng.bakingapp.Utils.NetworkCheck;
import com.yosri.mustafa.eng.bakingapp.data.RecipeDataBaseHelper;
import com.yosri.mustafa.eng.bakingapp.data.RecipeProvider;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailsFragment extends Fragment {
    private static Bundle bundle;
    SQLiteDatabase db;
    private ArrayList<Ingredient> ingredientsList;
    private ArrayList<Step> stepsList;
    private NetworkCheck networkCheck;
    private IngredientsRecyclerAdapter mIngredientsRecyclerAdapter;
    private LinearLayoutManager layoutManager;
    private StepsRecyclerAdapter mStepsRecyclerAdapter;

    @BindView(R.id.rv_ingredient) RecyclerView ingredientRecyclerView;
    @BindView(R.id.rv_steps)RecyclerView stepRecyclerView;
    @BindView(R.id.add_widget)
    Button addWidget;

    private Cursor mCursor;

    private ContentValues cv;
    private Uri uri = null;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private boolean m;
    private String name;

    @OnClick(R.id.add_widget)
    public void addWiget() {

        Log.e("test btn","test btn");

        addIngredientsToDataBase();

    }

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_recipe_details, container, false);
        RecipeDataBaseHelper helper = new RecipeDataBaseHelper(getActivity());
        db = helper.getWritableDatabase();

        ButterKnife.bind(this,v);

        pref = getActivity().getSharedPreferences("name", 0); // 0 - for private mode

        ingredientsList = new ArrayList<Ingredient>();
        stepsList = new ArrayList<Step>();
        networkCheck = new NetworkCheck();

        setupRecyclerView();
        return v;
    }
    private void addIngredientsToDataBase(){

        if(bundle!=null) {
            ingredientsList = (ArrayList<Ingredient>) bundle.getSerializable("ingredients");
            Log.e("test bundle", "test bundle" + ingredientsList.size());
             name = bundle.getString("name");
            Log.e("test bundle", "test bundle" + name);

        }

        editor = pref.edit();
        editor.clear();
        editor.putString("name",name);
        editor.apply();

        Log.e("test btn","test btn"+name);
        if (queryCheckRecipeInDataBase() == false) {

            for (int i = 0; i < ingredientsList.size(); i++) {

                cv = new ContentValues();

                cv.put(RecipeProvider.Ingredients.RECIPE_INGREDIENTS, ingredientsList.get(i).getIngredient());

                cv.put(RecipeProvider.Ingredients.RECIPE_NAME, name);

                cv.put(RecipeProvider.Ingredients.RECIPE_QUANTITY, ingredientsList.get(i).getQuantity());

                cv.put(RecipeProvider.Ingredients.RECIPE_MESSURE, ingredientsList.get(i).getMeasure());

                uri = getActivity().getContentResolver().insert(RecipeProvider.CONTENT_URI, cv);

                if (uri != null) {
                    Toast.makeText(getActivity(), uri.toString(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Toast.makeText(getActivity(),"This Recipe is saved before",Toast.LENGTH_SHORT).show();
        }
    }

    public  boolean queryCheckRecipeInDataBase(){
        final String args[]= {name} ;
        final String[] CHECK_Recipe = {
                RecipeProvider.Ingredients.RECIPE_NAME};

        mCursor= getActivity().getContentResolver().query(RecipeProvider.CONTENT_URI ,CHECK_Recipe,"name=?",args ,null);

        if (mCursor != null && mCursor.getCount() > 0) {
            Log.e("check",""+CHECK_Recipe);
            return true;
        }
        else
            return  false;
    }
    public static RecipeDetailsFragment getInstance(ArrayList<Ingredient> ingredients , ArrayList<Step> steps ,String name) {

        RecipeDetailsFragment recipeStepDetailFragment = new RecipeDetailsFragment();
        bundle = new Bundle();
        bundle.putSerializable("step", steps);
        bundle.putSerializable("ingredients", ingredients);
        bundle.putString("name", name);
        recipeStepDetailFragment.setArguments(bundle);
        return recipeStepDetailFragment;
    }


    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        if(bundle!=null){
              ingredientsList = (ArrayList<Ingredient>) bundle.getSerializable("ingredients");
              stepsList = (ArrayList<Step>) bundle.getSerializable("step");
            setupRecyclerView();
        }
    }

    private void setupRecyclerView() {

        // Setup Recycle Ingredient
//        ingredientRecyclerView = findViewById(R.id.rv_ingredient);
        ingredientRecyclerView.setHasFixedSize(true);

        mIngredientsRecyclerAdapter = new IngredientsRecyclerAdapter(getActivity(), ingredientsList);
        ingredientRecyclerView.setAdapter(mIngredientsRecyclerAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        ingredientRecyclerView.setLayoutManager(layoutManager);

        ingredientRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mIngredientsRecyclerAdapter.notifyDataSetChanged();

        // Setup Recycle Steps
//        stepRecyclerView = findViewById(R.id.rv_steps);
        stepRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());

        mStepsRecyclerAdapter = new StepsRecyclerAdapter(getActivity(), stepsList , getActivity());
        stepRecyclerView.setAdapter(mStepsRecyclerAdapter);
        stepRecyclerView.setLayoutManager(layoutManager);

        stepRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mStepsRecyclerAdapter.notifyDataSetChanged();
    }


}
