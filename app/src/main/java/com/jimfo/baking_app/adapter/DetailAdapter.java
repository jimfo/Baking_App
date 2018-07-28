package com.jimfo.baking_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jimfo.baking_app.R;
import com.jimfo.baking_app.model.Recipe;
import com.jimfo.baking_app.model.Step;

import java.util.ArrayList;
import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder>{

    private final List<Step> mSteps;
    private ItemClickListener mListener;
    private LayoutInflater mInflater;

    public DetailAdapter(Context context, ItemClickListener listener, ArrayList<Step>steps){
        this.mSteps = steps;
        this.mListener = listener;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.step_card, parent, false);
        return new DetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int i) {
        holder.stepTv.append(mSteps.get(i).getmId() + " ");
        holder.stepTv.append(mSteps.get(i).getmShortDescription());

        if (!mSteps.get(i).getmVideoUrl().equals("")) {
            holder.stepTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.play_arrow, 0);
        }
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    class DetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView stepTv;

        public DetailViewHolder(View itemView) {
            super(itemView);
            stepTv = itemView.findViewById(R.id.step_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mListener.onItemClickListener(position);
        }
    }
}
