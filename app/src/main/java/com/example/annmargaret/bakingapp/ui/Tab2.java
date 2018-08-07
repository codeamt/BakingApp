package com.example.annmargaret.bakingapp.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.annmargaret.bakingapp.R;
import com.example.annmargaret.bakingapp.models.Recipe;
import com.example.annmargaret.bakingapp.ui.adapters.RecipeDetailAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.annmargaret.bakingapp.ui.RecipeActivity.SELECTED_RECIPES;

public class Tab2 extends android.support.v4.app.Fragment {

    ArrayList<Recipe> recipe;
    String recipeName;
    RecipeDetailAdapter recipeDetailAdapter;
    LinearLayoutManager layoutManager;
    public static Parcelable mStepState;

    @BindView(R.id.recipe_detail_recycler) RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recipe = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.tab_2, container, false);
        ButterKnife.bind(this, rootView);

        layoutManager = new LinearLayoutManager(getContext());

        if(savedInstanceState != null) {
            recipe = savedInstanceState.getParcelableArrayList(SELECTED_RECIPES);
        }
        else {
            if (getActivity() != null) {
                recipe = getActivity().getIntent().getParcelableArrayListExtra(SELECTED_RECIPES);
            }
        }

        if (recipe != null && recyclerView != null) {

            recipeName = recipe.get(0).getName();
            recyclerView.setLayoutManager(layoutManager);
            recipeDetailAdapter = new RecipeDetailAdapter((RecipeDetailActivity) getActivity());
            recyclerView.setAdapter(recipeDetailAdapter);
            recipeDetailAdapter.setRecipeData(recipe, getContext());
            if (savedInstanceState != null) {
                mStepState = savedInstanceState.getParcelable("Recycler_State");

                if (mStepState != null && recyclerView != null && layoutManager != null) {
                    layoutManager.onRestoreInstanceState(mStepState);
                }
            }

        }

        return rootView;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        super.onSaveInstanceState(currentState);
        mStepState = layoutManager.onSaveInstanceState();
        currentState.putParcelableArrayList(SELECTED_RECIPES, recipe);
        currentState.putParcelable("Recycler_State", mStepState);
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
