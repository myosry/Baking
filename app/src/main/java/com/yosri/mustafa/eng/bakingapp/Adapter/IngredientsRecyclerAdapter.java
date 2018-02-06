package com.yosri.mustafa.eng.bakingapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yosri.mustafa.eng.bakingapp.Model.Ingredient;
import com.yosri.mustafa.eng.baking.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsRecyclerAdapter extends RecyclerView.Adapter<IngredientsRecyclerAdapter.IngredientViewHolder> {

    private ArrayList<Ingredient> ingredientList;
    private Context context;
    private String ingredient_text;
    private Double quantity_text;
    private String measure_text;

    public IngredientsRecyclerAdapter(Context context, ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
        this.context = context;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layout = R.layout.ingredient_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layout, viewGroup, shouldAttachToParentImmediately);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {

         ingredient_text = ingredientList.get(position).getIngredient();
        holder.ingredient.setText(ingredient_text);

         quantity_text =  ingredientList.get(position).getQuantity();
         measure_text =  ingredientList.get(position).getMeasure();
        holder.quantity_measure.setText(quantity_text +" "+ measure_text);
    }

    @Override
    public int getItemCount() {
        if (null == ingredientList) return 0;

        return ingredientList.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ingredient) TextView ingredient;
        @BindView(R.id.quantity_measure) TextView quantity_measure;

//        private TextView ingredient;
//        private TextView quantity_measure;

        public IngredientViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

//            ingredient       = view.findViewById(R.id.ingredient);
//            quantity_measure = view.findViewById(R.id.quantity_measure);

            view.setOnClickListener(this);
        }
        Toast mToast;

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

//            Intent intent = new Intent(context, DetailsActivity.class);

            if(position != RecyclerView.NO_POSITION) {

                Ingredient clickedDataItem = ingredientList.get(position);

//                intent.putExtra("recipe", clickedDataItem);

                if (mToast != null) {
                    mToast.cancel();}
                mToast = Toast.makeText(context,"You choose :"+ ingredient_text+" "+ quantity_text+" "+measure_text,Toast.LENGTH_SHORT);
                mToast.show();
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);

//            }
        }

        }
    }
}
