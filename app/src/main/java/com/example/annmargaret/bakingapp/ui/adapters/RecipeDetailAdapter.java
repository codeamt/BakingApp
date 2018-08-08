package com.example.annmargaret.bakingapp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.annmargaret.bakingapp.R;
import com.example.annmargaret.bakingapp.models.Recipe;
import com.example.annmargaret.bakingapp.models.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecipeDetailViewHolder> {

    private List<Step> steps;
    private String recipeName;

    final private ListItemClickListener detailItemClickListener;

    public interface ListItemClickListener {
        void onListItemClick(List<Step> nextSteps, int targetItemIndex, String recipeName);
    }


    public RecipeDetailAdapter(ListItemClickListener listener) {
        detailItemClickListener = listener;
    }

    public void setRecipeData(List<Recipe> recipeData, Context context) {
        steps = recipeData.get(0).getSteps();
        recipeName = recipeData.get(0).getName();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int listItemId = R.layout.recipe_detail_cardview_items;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(listItemId, viewGroup, false);

        RecipeDetailViewHolder viewHolder = new RecipeDetailViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecipeDetailViewHolder viewHolder, int position) {
        String text = steps.get(position).getId() + ". " + steps.get(position).getShortDescription();
        viewHolder.textVH.setText(text);
    }

    @Override
    public int getItemCount() {
        return steps != null ? steps.size() : 0;
    }

    class RecipeDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textVH;
        private RecipeDetailViewHolder(View item) {
            super(item);
            textVH = (TextView) item.findViewById(R.id.shortDescription);
            item.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int targetPosition = getAdapterPosition();
            detailItemClickListener.onListItemClick(steps, targetPosition, recipeName);
        }
    }
}
