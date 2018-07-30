package com.jimfo.baking_app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jimfo.baking_app.R;
import com.jimfo.baking_app.model.Step;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private ArrayList<Step> mSteps;
    private LayoutInflater mInflater;
    private ItemClickListener mListener;
    private Context mContext;

    public StepAdapter(Context context, ItemClickListener listener, ArrayList<Step> steps) {
        this.mSteps = steps;
        this.mListener = listener;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = mInflater.inflate(R.layout.step_card, parent, false);
        return new StepViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int i) {

        holder.stepItem.setText("");
        String step = buildText(i);
        holder.stepItem.setText(step);


        if (!mSteps.get(i).getmVideoUrl().equals("")) {
            holder.stepItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.play_arrow, 0);
        }

        if (!mSteps.get(i).getmThumbnailUrl().equals("")) {
            Picasso.with(mContext).load(mSteps.get(i).getmThumbnailUrl()).into(holder.stepIV);
            loadImage(mSteps.get(i).getmThumbnailUrl());
        }
    }

    private void loadImage(String path) {
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

                // Unused required method
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

                // Unused required method
            }
        };
        Picasso.with(mContext).load(path).into(target);
    }

    private String buildText(int i){
        StringBuilder str = new StringBuilder();

        str.append(mSteps.get(i).getmId());
        str.append(" ");
        str.append(mSteps.get(i).getmShortDescription());
        str.append(" ");
        return str.toString();
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView stepItem;
        private ImageView stepIV;

        StepViewHolder(View itemView) {
            super(itemView);

            stepItem = itemView.findViewById(R.id.step_tv);
            stepIV = itemView.findViewById(R.id.step_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mListener.onItemClickListener(position);
        }
    }
}
