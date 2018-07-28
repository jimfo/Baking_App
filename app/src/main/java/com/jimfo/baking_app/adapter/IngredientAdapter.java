package com.jimfo.baking_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jimfo.baking_app.R;
import com.jimfo.baking_app.model.Ingredient;
import com.jimfo.baking_app.model.Step;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {


    private ArrayList<Ingredient> mIngredients;
    private LayoutInflater mInflater;

    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients) {
        this.mIngredients = ingredients;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = mInflater.inflate(R.layout.ingredient_card, parent, false);
        return new IngredientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int i) {

        String ingredient = buildText(i);
        holder.ingredientTV.setText(ingredient);
    }

    private String buildText(int i){
        StringBuilder str = new StringBuilder();

        str.append(mIngredients.get(i).getmQuantity());
        str.append(" ");
        str.append(mIngredients.get(i).getmMeasure());
        str.append(" ");
        str.append(mIngredients.get(i).getmIngredient());
        return str.toString();
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder{

        private TextView ingredientTV;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            ingredientTV = itemView.findViewById(R.id.ingredient_tv);
        }
    }
}
