package com.example.annmargaret.bakingapp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.annmargaret.bakingapp.R;
import com.example.annmargaret.bakingapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    ArrayList<Recipe> recipes;
    Context mContext;
    final private RecipeItemClickListener recipeClickListener;

    public interface RecipeItemClickListener {
        void onListItemClick(Recipe targetRecipeInex);

    }

    public RecipeAdapter(RecipeItemClickListener listener) {
        recipeClickListener = listener;
    }

    public void setRecipeData(ArrayList<Recipe> recipeData, Context context) {
        recipes = recipeData;
        mContext = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int listItemId = R.layout.recipe_cardview_items;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(listItemId, viewGroup, false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder viewHolder, int position) {
        viewHolder.recipeTextRV.setText(recipes.get(position).getName());
        int imageUrl = R.drawable.sweets;
        Picasso.with(mContext).load(imageUrl).into(viewHolder.recipeImageRV);
    }

    @Override
    public int getItemCount() {
        return recipes != null ? recipes.size() : 0;
    }


    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipeTextRV;
        ImageView recipeImageRV;

        public RecipeViewHolder(View item) {
            super(item);
            recipeTextRV = (TextView) item.findViewById(R.id.title);
            recipeImageRV = (ImageView) item.findViewById(R.id.recipeImage);

            item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int targetPosition = getAdapterPosition();
            recipeClickListener.onListItemClick(recipes.get(targetPosition));
        }
    }
}
