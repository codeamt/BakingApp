package com.example.annmargaret.bakingapp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.annmargaret.bakingapp.R;
import com.example.annmargaret.bakingapp.models.Ingredient;
import com.example.annmargaret.bakingapp.models.Recipe;
import com.example.annmargaret.bakingapp.ui.adapters.PagerAdapter;
import com.example.annmargaret.bakingapp.ui.adapters.RecipeDetailAdapter;
import com.example.annmargaret.bakingapp.widget.UpdateBakingService;

import static com.example.annmargaret.bakingapp.ui.RecipeActivity.SELECTED_RECIPES;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment {

    ArrayList<Recipe> recipe;
    String recipeName;
    List<Ingredient> ingredients;
    TextView textView;
    RecyclerView recyclerView;
    RecipeDetailAdapter recipeDetailAdapter;
    LinearLayoutManager layoutManager;
    ArrayList<String> recipeIngredientsForBinding;
    Parcelable mStepState;



    public RecipeDetailFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        recipe = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.recipe_detail_fragment_body_part, container, false);

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelableArrayList(SELECTED_RECIPES);
            if (recipe != null) {
                recipeName = recipe.get(0).getName();
                ingredients = recipe.get(0).getIngredients();
            }

            if(rootView.findViewById(R.id.tab_layout) != null && !isInLandscapeMode(getContext())) {
                inflateTabs(rootView);
            } else if (rootView.findViewById(R.id.tab_layout) == null && isInLandscapeMode(getContext())){
                bindViews(rootView, ingredients);
                mStepState = savedInstanceState.getParcelable("Recycler_State");
                if(mStepState != null) {
                    layoutManager.onRestoreInstanceState(mStepState);
                }
            }

        } else {
            if (getArguments() != null) {
                recipe = getArguments().getParcelableArrayList(SELECTED_RECIPES);
                if (recipe != null) {
                    recipeName = recipe.get(0).getName();
                    ingredients = recipe.get(0).getIngredients();
                }

                if (rootView.findViewById(R.id.tab_layout) != null) {
                    inflateTabs(rootView);
                } else {
                    textView = (TextView) rootView.findViewById(R.id.recipe_detail_text);
                    recipeIngredientsForBinding = new ArrayList<>();
                    recyclerView = (RecyclerView) rootView.findViewById(R.id.recipe_detail_recycler);
                    layoutManager = new LinearLayoutManager(getContext());

                    for (Ingredient i : ingredients) {
                        textView.append("\u2022 " + i.getIngredient() + "\n");
                        textView.append("\t\t\t Quantity: " + i.getQuantity().toString() + "\n");
                        textView.append("\t\t\t Measure: " + i.getMeasure() + "\n\n");

                        recipeIngredientsForBinding.add(i.getIngredient() + "\n" +
                                "Quantity: " + i.getQuantity().toString() + "\n" +
                                "Measure: " + i.getMeasure() + "\n");
                    }

                    recyclerView.setLayoutManager(layoutManager);
                    recipeDetailAdapter = new RecipeDetailAdapter((RecipeDetailActivity) getActivity());
                    recyclerView.setAdapter(recipeDetailAdapter);
                    recipeDetailAdapter.setRecipeData(recipe, getContext());
                    //for home widget
                    if(recipeIngredientsForBinding != null && getActivity() != null) {
                        UpdateBakingService.startBakingService(getActivity().getApplicationContext(), recipeIngredientsForBinding);
                        Log.v("Parent Frag Ingredients", String.valueOf(recipeIngredientsForBinding));
                    }
                }
            }
        }
        return rootView;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SELECTED_RECIPES, recipe);
        if(isInLandscapeMode(getContext()) && layoutManager != null) {
            mStepState = layoutManager.onSaveInstanceState();
            currentState.putParcelable("Recycler_State", mStepState);
        }
        currentState.putString("Title", recipeName);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void inflateTabs(View root) {
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tab_layout);
        if(tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab().setText("Ingredients"));
            tabLayout.addTab(tabLayout.newTab().setText("Instructions"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            ViewPager viewPager = (ViewPager) root.findViewById(R.id.pager);
            PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        }
    }

    private void bindViews(View root, List<Ingredient> ingredientList ) {
        textView = (TextView) root.findViewById(R.id.recipe_detail_text);
        recipeIngredientsForBinding = new ArrayList<>();
        recyclerView = (RecyclerView) root.findViewById(R.id.recipe_detail_recycler);
        layoutManager = new LinearLayoutManager(getContext());

        for (Ingredient i : ingredientList) {
            textView.append("\u2022 " + i.getIngredient() + "\n");
            textView.append("\t\t\t Quantity: " + i.getQuantity().toString() + "\n");
            textView.append("\t\t\t Measure: " + i.getMeasure() + "\n\n");

            recipeIngredientsForBinding.add(i.getIngredient() + "\n" +
                    "Quantity: " + i.getQuantity().toString() + "\n" +
                    "Measure: " + i.getMeasure() + "\n");
        }

        recyclerView.setLayoutManager(layoutManager);
        recipeDetailAdapter = new RecipeDetailAdapter((RecipeDetailActivity) getActivity());
        recyclerView.setAdapter(recipeDetailAdapter);
        recipeDetailAdapter.setRecipeData(recipe, getContext());
    }

    public boolean isInLandscapeMode( Context context ) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }


}
