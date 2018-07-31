package com.jimfo.baking_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jimfo.baking_app.R;
import com.jimfo.baking_app.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final List<Recipe> mRecipes;
    private ItemClickListener mListener;
    private LayoutInflater mInflater;
    private Context mContext;

    public RecipeAdapter(Context context, ItemClickListener listener, ArrayList<Recipe> recipes) {
        this.mRecipes = recipes;
        this.mListener = listener;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = mInflater.inflate(R.layout.recipe_card, parent, false);
        return new RecipeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int i) {

        holder.recipeTitle.setText(mRecipes.get(i).getmName());
        holder.servingSize.append(mRecipes.get(i).getmServings());

        switch (i) {
            case 0:
                holder.recipeImage.setBackgroundResource(R.drawable.nutella_pie);
                break;
            case 1:
                holder.recipeImage.setBackgroundResource(R.drawable.brownie);
                break;
            case 2:
                holder.recipeImage.setBackgroundResource(R.drawable.yellow_cake);
                break;
            case 3:
                holder.recipeImage.setBackgroundResource(R.drawable.cheese_cake);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }


    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView recipeImage;
        private TextView recipeTitle;
        private TextView servingSize;

        RecipeViewHolder(View itemView) {
            super(itemView);

            recipeImage = itemView.findViewById(R.id.recipe_icon_iv);
            recipeTitle = itemView.findViewById(R.id.recipe_title_tv);
            servingSize = itemView.findViewById(R.id.serving_size_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mListener.onItemClickListener(position);
        }
    }
}
