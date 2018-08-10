package com.example.annmargaret.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.annmargaret.bakingapp.R;
import com.example.annmargaret.bakingapp.api.RecipeAPI;
import static com.example.annmargaret.bakingapp.ui.RecipeActivity.ALL_RECIPES;
import com.example.annmargaret.bakingapp.api.RecipeAPIClientBuilder;
import com.example.annmargaret.bakingapp.idlingResource.IdlingResource;
import com.example.annmargaret.bakingapp.models.Recipe;
import com.example.annmargaret.bakingapp.ui.adapters.RecipeAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeFragment extends Fragment {


    public RecipeFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_fragment_body_part, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recipe_recycler);
        final RecipeAdapter recipeAdapter = new RecipeAdapter((RecipeActivity)getActivity());
        recyclerView.setAdapter(recipeAdapter);

        if(rootView.getTag() != null && rootView.getTag().equals("phone-land")) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);

        }
        RecipeAPI recipeAPI = RecipeAPIClientBuilder.Retrieve();
        Call<ArrayList<Recipe>> recipe = recipeAPI.getRecipe();

        final IdlingResource idlingResource = (IdlingResource)((RecipeActivity) getActivity()).getMIdlingResource();

        idlingResource.setIdleState(false);

        recipe.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                Integer statusCode = response.code();
                Log.v("Status code: ", statusCode.toString());

                ArrayList<Recipe> recipes = response.body();

                Bundle recipesBundle = new Bundle();
                recipesBundle.putParcelableArrayList(ALL_RECIPES, recipes);

                recipeAdapter.setRecipeData(recipes, getContext());
                idlingResource.setIdleState(true);

            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.v("Http fail: ", t.getMessage());
            }
        });

        return rootView;
    }
}
