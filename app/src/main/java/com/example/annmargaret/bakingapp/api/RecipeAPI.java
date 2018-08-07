package com.example.annmargaret.bakingapp.api;

import com.example.annmargaret.bakingapp.models.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeAPI {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}
