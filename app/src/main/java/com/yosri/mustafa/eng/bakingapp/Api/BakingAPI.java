package com.yosri.mustafa.eng.bakingapp.Api;

import com.yosri.mustafa.eng.bakingapp.Model.Recipe;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;


public interface BakingAPI {
    @GET("baking.json")
    public Call<List<Recipe>> getRecipes();
}
