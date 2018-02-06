package com.yosri.mustafa.eng.bakingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.yosri.mustafa.eng.baking.R;
import com.yosri.mustafa.eng.bakingapp.Model.Ingredient;
import com.yosri.mustafa.eng.bakingapp.Model.Step;
import com.yosri.mustafa.eng.bakingapp.Ui.RecipeDetails;
import com.yosri.mustafa.eng.bakingapp.Ui.StepDetails;
import com.yosri.mustafa.eng.bakingapp.Ui.StepDetailsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsRecyclerAdapter extends RecyclerView.Adapter<StepsRecyclerAdapter.StepViewHolder> {

    private ArrayList<Step> stepListList;
    private Context context;
    private FragmentActivity activity;
    private boolean mTwoPane;

    public StepsRecyclerAdapter(Context context, ArrayList<Step> recipeList, FragmentActivity activity) {
        this.stepListList = recipeList;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layout = R.layout.step_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layout, viewGroup, shouldAttachToParentImmediately);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {

        String shortDescription = stepListList.get(position).getShortDescription();
        holder.shortDescription.setText(shortDescription);

    }

    @Override
    public int getItemCount() {
        if (null == stepListList) return 0;

        return stepListList.size();
    }


    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.short_description)
        TextView shortDescription;
        private ArrayList<Step> stepDetail;

        public StepViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mTwoPane = isTablet(context);
            view.setOnClickListener(this);
        }

        Toast mToast;

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION) {
                Step clickedDataItem = stepListList.get(position);

                stepDetail = stepListList;
                if (!mTwoPane) {
                    Intent intent = new Intent(context, StepDetails.class);



                    intent.putExtra("step", stepDetail);
                    intent.putExtra("position", position);


                    intent.putExtra("recipe", clickedDataItem);
                    intent.putExtras(intent);
//
//                    if (mToast != null) {
//                        mToast.cancel();
//                    }
//                    else {
//                        mToast = Toast.makeText(context, "You choose :" + isTablet(context), Toast.LENGTH_SHORT);
//                        mToast.show();
//                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    ((RecipeDetails)context).getSetpDetails(stepDetail,position);
//                    mToast = Toast.makeText(context, "getStep", Toast.LENGTH_SHORT);
//                    mToast.show();

//

                }

            }
                else {

                    mToast = Toast.makeText(context, "Recycle dont have posion", Toast.LENGTH_SHORT);

                }


            }
        }
    }
