

package com.yosri.mustafa.eng.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recipe implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private ArrayList<Ingredient> ingredients = null;
    @SerializedName("steps")
    @Expose
    private ArrayList<Step> steps = null;
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @SerializedName("image")
    @Expose
    private String image;

    private List<Recipe> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Recipe> getResults() {
        return results;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {

        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeList(this.ingredients);
        dest.writeList(this.steps);
        dest.writeValue(this.servings);

    }

        protected Recipe(Parcel in) {
            this.id = (Integer) in.readValue(Integer.class.getClassLoader());
            this.servings = (Integer) in.readValue(Integer.class.getClassLoader());
            this.ingredients=in.readArrayList(Ingredient.class.getClassLoader());
            this.steps=in.readArrayList(Step.class.getClassLoader());
            this.name = in.readString();
            this.image = in.readString();
        }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}