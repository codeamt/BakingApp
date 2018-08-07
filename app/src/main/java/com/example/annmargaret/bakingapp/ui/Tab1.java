package com.example.annmargaret.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.annmargaret.bakingapp.R;
import com.example.annmargaret.bakingapp.models.Ingredient;
import com.example.annmargaret.bakingapp.models.Recipe;
import com.example.annmargaret.bakingapp.widget.UpdateBakingService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.annmargaret.bakingapp.ui.RecipeActivity.SELECTED_RECIPES;


public class Tab1 extends android.support.v4.app.Fragment {

    ArrayList<Recipe> recipe;
    String recipeName;
    ArrayList<String> recipeIngredientsForBinding;
    List<Ingredient> ingredients;

    @BindView(R.id.recipe_detail_text) TextView textView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recipe = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.tab_1, container, false);
        ButterKnife.bind(this, rootView);
        recipeIngredientsForBinding = new ArrayList<>();

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelableArrayList(SELECTED_RECIPES);
        }
        else {
            if (getActivity() != null) {
                recipe = getActivity().getIntent().getParcelableArrayListExtra(SELECTED_RECIPES);
            }
        }
        if(recipe != null) {
            ingredients = recipe.get(0).getIngredients();
            recipeName = recipe.get(0).getName();
        }


        for (Ingredient i : ingredients) {
            textView.append("\u2022 " + i.getIngredient() + "\n");
            textView.append("\t\t\t Quantity: " + i.getQuantity().toString() + "\n");
            textView.append("\t\t\t Measure: " + i.getMeasure() + "\n\n");

            recipeIngredientsForBinding.add(i.getIngredient() + "\n" +
                    "Quantity: " + i.getQuantity().toString() + "\n" +
                    "Measure: " + i.getMeasure() + "\n");
        }

        //for home widget
        if(recipeIngredientsForBinding != null) {
            UpdateBakingService.startBakingService(getActivity().getApplicationContext(), recipeIngredientsForBinding);
            Log.v("Tab 1 Ingredients", String.valueOf(recipeIngredientsForBinding));
        }
        return rootView;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SELECTED_RECIPES, recipe);
        currentState.putString("Title", recipeName);
    }


    @Override
    public void onResume() {
        super.onResume();
    }



    @Override
    public void onPause() {
        super.onPause();
    }

}
