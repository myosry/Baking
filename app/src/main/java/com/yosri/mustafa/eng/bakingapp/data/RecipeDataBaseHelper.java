package com.yosri.mustafa.eng.bakingapp.data;


import android.content.Context;

import de.triplet.simpleprovider.SimpleSQLHelper;

public class RecipeDataBaseHelper extends SimpleSQLHelper{

    public static final String DB_NAME ="ingredients.db";
    public static final int DB_VERSION =3;

    public RecipeDataBaseHelper(Context context) {

        super(context,DB_NAME,DB_VERSION);
}

}
