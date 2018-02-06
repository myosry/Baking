package com.yosri.mustafa.eng.bakingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yosri.mustafa.eng.bakingapp.Model.Ingredient;
import com.yosri.mustafa.eng.bakingapp.Model.Recipe;
import com.yosri.mustafa.eng.bakingapp.Model.Step;
import com.yosri.mustafa.eng.baking.R;
import com.yosri.mustafa.eng.bakingapp.Ui.RecipeDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BakingRecyclerAdapter extends RecyclerView.Adapter<BakingRecyclerAdapter.BakingViewHolder> {

    private List<Recipe> recipeList ;
    private Context context;

    public BakingRecyclerAdapter( Context context,List<Recipe> recipeList) {
        this.recipeList = recipeList;
        this.context = context;
    }

    @Override
    public BakingViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layout = R.layout.recipe_card;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layout, viewGroup, shouldAttachToParentImmediately);
        return new BakingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BakingViewHolder holder, int position) {

        String recipe_title = recipeList.get(position).getName();
        holder.recipe_name.setText(recipe_title);

        String imgUrl = recipeList.get(position).getImage();

        if (imgUrl.isEmpty()) {
            holder.img.setImageResource(R.drawable.food);
        } else {

            Picasso.with(context)
                    .load(recipeList.get(position).getImage())
                    .placeholder(R.drawable.food)
                    .into(holder.img);
        }

        String servings = "Serving : "+recipeList.get(position).getServings();
        holder.servings.setText(servings);
    }

    @Override

    public int getItemCount() {
        if (null == recipeList) return 0;

        return recipeList.size();
    }

    public class BakingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.title) TextView recipe_name;
        @BindView(R.id.serving) TextView servings;
        @BindView(R.id.img_thumbnail) ImageView img;

        public BakingViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }
        Toast mToast;

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(context, RecipeDetails.class);
            if(position != RecyclerView.NO_POSITION) {

                Recipe clickedDataItem = recipeList.get(position);
                String name = clickedDataItem.getName();
                ArrayList< Ingredient> m = clickedDataItem.getIngredients();
                ArrayList<Step> s = clickedDataItem.getSteps();

                Bundle bundle = new Bundle();
                intent.putExtra("b",m );
                intent.putExtra("c", s);
                intent.putExtra("name", name);
                intent.putExtra("position", position);

                intent.putExtras(intent);

                if (mToast != null) {
                    mToast.cancel();}
                mToast = Toast.makeText(context,"You choose :"+recipeList.get(position).getImage(),Toast.LENGTH_SHORT);
                mToast.show();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        }

        }
    }

