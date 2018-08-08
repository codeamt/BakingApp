package com.example.annmargaret.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.example.annmargaret.bakingapp.R;
import com.example.annmargaret.bakingapp.models.Recipe;
import com.example.annmargaret.bakingapp.models.Step;
import com.example.annmargaret.bakingapp.ui.adapters.RecipeDetailAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailAdapter.ListItemClickListener, RecipeDetailStepFragment.ListItemClickListener {

    static String SELECTED_RECIPES="Selected_Recipes";
    static String SELECTED_STEPS="Selected_Steps";
    static String SELECTED_INDEX="Selected_Index";
    static String STACK_RECIPE_DETAIL="STACK_RECIPE_DETAIL";
    static String STACK_RECIPE_STEP_DETAIL="STACK_RECIPE_STEP_DETAIL";

    ArrayList<Recipe> recipe;
    String recipeName;

    @BindView(R.id.toolbar) Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        if(savedInstanceState == null) {
            Bundle targetRecipeBundle = getIntent().getExtras();

            recipe = new ArrayList<>();
            if(targetRecipeBundle != null) {
                recipe = targetRecipeBundle.getParcelableArrayList(SELECTED_RECIPES);
            }
            if(recipe != null) {
                recipeName = recipe.get(0).getName();
            }
            final RecipeDetailFragment fragment = new RecipeDetailFragment();


            fragment.setArguments(targetRecipeBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack(STACK_RECIPE_DETAIL)
                    .commit();

            if(findViewById(R.id.recipe_linear_layout).getTag()!=null && findViewById(R.id.recipe_linear_layout).getTag().equals("tablet-land")) {
                final RecipeDetailStepFragment stepFragment = new RecipeDetailStepFragment();
                stepFragment.setArguments(targetRecipeBundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container2, stepFragment).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                        .commit();
            }
        } else {
            recipe = getIntent().getParcelableArrayListExtra(SELECTED_RECIPES);
            recipeName = savedInstanceState.getString("Title");
        }

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(recipeName);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if(findViewById(R.id.fragment_container2) == null) {
                    if(fragmentManager.getBackStackEntryCount() > 1) {
                        fragmentManager.popBackStack(STACK_RECIPE_DETAIL, 0);
                    } else if (fragmentManager.getBackStackEntryCount() > 0) {
                        finish();
                    }
                } else {
                    finish();
                }

            }
        });
    }

    @Override
    public void onListItemClick(List<Step> stepList, int targetItemIndex, String recipeName) {
        final RecipeDetailStepFragment stepFragment = new RecipeDetailStepFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(recipeName);
        }
        Bundle stepBundle = new Bundle();
        stepBundle.putParcelableArrayList(SELECTED_STEPS,(ArrayList<Step>) stepList);
        stepBundle.putInt(SELECTED_INDEX, targetItemIndex);
        stepBundle.putString("Title", recipeName);
        stepFragment.setArguments(stepBundle);

        /*if(findViewById(R.id.recipe_linear_layout).getTag() != null && findViewById(R.id.recipe_linear_layout).getTag().equals("tablet-land")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container2, stepFragment).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                    .commit();
        }
        else {*/
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, stepFragment).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                    .commit();
      //  }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Title", recipeName);
        outState.putParcelableArrayList("Recipe", recipe);
    }

    @Override
    public void onBackPressed() {
        recipe = getIntent().getParcelableArrayListExtra(SELECTED_RECIPES);
        super.onBackPressed();
    }

    @Override
    public void onRestoreInstanceState(Bundle state) {
        if(state != null) {
            recipe = state.getParcelableArrayList("Recipe");
            recipeName = state.getString("Title");
        }
        super.onRestoreInstanceState(state);
    }

}
