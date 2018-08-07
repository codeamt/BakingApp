package com.example.annmargaret.bakingapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.annmargaret.bakingapp.R;
import com.example.annmargaret.bakingapp.idlingResource.IdlingResource;
import com.example.annmargaret.bakingapp.models.Recipe;
import com.example.annmargaret.bakingapp.ui.adapters.RecipeAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity implements RecipeAdapter.RecipeItemClickListener {

    static String ALL_RECIPES = "All_Recipes";
    static String SELECTED_RECIPES = "Selected_Recipes";
    static String SELECTED_STEPS = "Selected_Steps";
    static String SELECTED_INDEX = "Selected_Index";

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Nullable
    private IdlingResource idlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getMIdlingResource() {
        if(idlingResource == null) {
            idlingResource = new IdlingResource();
        }
        return idlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("Baking App");
        }
        getMIdlingResource();
    }

    @Override
    public void onListItemClick(Recipe targetRecipeIndex) {
        Bundle targetRecipeBundle = new Bundle();
        ArrayList<Recipe> targetRecipe = new ArrayList<>();
        targetRecipe.add(targetRecipeIndex);
        targetRecipeBundle.putParcelableArrayList(SELECTED_RECIPES, targetRecipe);

        final Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtras(targetRecipeBundle);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
