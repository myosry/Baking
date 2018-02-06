package com.yosri.mustafa.eng.bakingapp.data;

import android.net.Uri;

import de.triplet.simpleprovider.AbstractProvider;
import de.triplet.simpleprovider.Column;
import de.triplet.simpleprovider.Table;

public class RecipeProvider extends AbstractProvider{

    public static final String AUTHORITY = "com.yosri.mustafa.eng.bakingapp";

    public  static  final  String PATH_INGREDINT= "ingredients";
    public  static  final Uri BASE_CONTENT_URI= Uri.parse("content://"+AUTHORITY);
    public  static  final Uri CONTENT_URI= BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDINT).build();


    @Override
    protected String getAuthority() {
        return AUTHORITY;
    }

    @Table
    public class Ingredients{

        @Column(value = Column.FieldType.INTEGER, primaryKey = true ,unique = true)
        public static final String RECIPE_ID = "_id";

        @Column(Column.FieldType.TEXT)
        public static final String RECIPE_NAME = "name";

        @Column(Column.FieldType.TEXT)
        public static final String RECIPE_QUANTITY = "quantity";

        @Column(Column.FieldType.TEXT)
        public static final String RECIPE_MESSURE = "measure";

        @Column(Column.FieldType.TEXT)
        public static final String RECIPE_INGREDIENTS = "ingredients";

    }

}
