package com.yosri.mustafa.eng.bakingapp.Service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.yosri.mustafa.eng.baking.R;
import com.yosri.mustafa.eng.bakingapp.Model.Ingredient;
import com.yosri.mustafa.eng.bakingapp.Widget.RecipeWidgetProvider;
import com.yosri.mustafa.eng.bakingapp.data.RecipeDataBaseHelper;
import com.yosri.mustafa.eng.bakingapp.data.RecipeProvider;

import java.util.ArrayList;

/**
 * Created by DELL on 1/25/2018.
 */

public class StackWidgetService extends RemoteViewsService {
    private String name_txt;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);    }

    class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private  int mCount =0;
        private ArrayList<Ingredient> mWidgetItems = new ArrayList<Ingredient>();
        private Context mContext;
        private int mAppWidgetId;
        private  SharedPreferences pref;
        private Cursor mCursor;
        RecipeDataBaseHelper helper ;

        public StackRemoteViewsFactory(Context context, Intent intent) {

            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);


        }

        public ArrayList<Ingredient> getIngredientsList(){
         mWidgetItems.clear();

            Log.e("Widget","getIngredientsList ");

            String name = pref.getString("name","");
            final String selectargs[]= {name} ;

            mCursor= mContext.getContentResolver().query(RecipeProvider.CONTENT_URI,
                    null,
                    "name=?",
                    selectargs ,
                    null);

            if (mCursor.moveToFirst()){
                do {
                    Ingredient ingredient ;

                    String ingredient_text = mCursor.getString(mCursor.getColumnIndex(RecipeProvider.Ingredients.RECIPE_INGREDIENTS));
                    Double quantity_txt = Double.parseDouble (mCursor.getString(mCursor.getColumnIndex(RecipeProvider.Ingredients.RECIPE_QUANTITY)));
                    String messure_txt= mCursor.getString(mCursor.getColumnIndex(RecipeProvider.Ingredients.RECIPE_MESSURE));
                     name_txt= mCursor.getString(mCursor.getColumnIndex(RecipeProvider.Ingredients.RECIPE_NAME));

                    ingredient = new Ingredient(quantity_txt,messure_txt,ingredient_text);
                    mWidgetItems.add(ingredient);

                }while(mCursor.moveToNext());
            }
            mCursor.close();
//            for (int i=0; i<mWidgetItems.size();i++) {
//                Log.e("Widget", "get" + mWidgetItems.get(i).getIngredient() + " " + mWidgetItems.get(i).getMeasure() + " " + mWidgetItems.get(i).getQuantity());
//            }
            return mWidgetItems;
        }
        @Override
        public void onCreate() {

            pref = mContext.getSharedPreferences("name", 0); // 0 - for private mode

            helper = new RecipeDataBaseHelper(mContext);
            SQLiteDatabase db = helper.getWritableDatabase();
            getIngredientsList();
            mCount =getIngredientsList().size();
             mCount = getIngredientsList().size();
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putString("name",name_txt);
//            editor.apply();
        }

        @Override
        public void onDataSetChanged() {
            getIngredientsList();
            mCount =getIngredientsList().size();
            Log.e("Widget","count: "+mCount);

            for (int i=0; i<mWidgetItems.size();i++) {
                Log.e("Widget", "" + mWidgetItems.get(i).getIngredient() +
                        " " + mWidgetItems.get(i).getMeasure() + "" +
                        " " + mWidgetItems.get(i).getQuantity());
            }


        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return getIngredientsList().size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_item);

            double q = mWidgetItems.get(position).getQuantity();
            String m = mWidgetItems.get(position).getMeasure();
            String ingredient= mWidgetItems.get(position).getIngredient();
//            String m_q = ingredient+"            " +q +" "+m;
            String m_q = q +" "+m;

//            rv.setTextViewText(android.R.id.text1, m_q);
            Log.d("TESTBUG", Integer.toString(position));

            rv.setTextViewText(R.id.ingredient, ingredient);
//            rv.setTextViewText(R.id.name, name_txt);
            rv.setTextViewText(R.id.quantity_measure, m_q);
            rv.setTextColor(R.id.ingredient, Color.BLACK);
//
            Bundle extras = new Bundle();
            extras.putInt(RecipeWidgetProvider.EXTRA_ITEM, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
//             Make it possible to distinguish the individual on-click
//             action of a given item
            rv.setOnClickFillInIntent(R.id.ingredient, fillInIntent);
            rv.setOnClickFillInIntent(R.id.quantity_measure, fillInIntent);

            // Return the remote views object.
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
