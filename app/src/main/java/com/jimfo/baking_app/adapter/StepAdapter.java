package com.jimfo.baking_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jimfo.baking_app.R;
import com.jimfo.baking_app.model.Step;

import java.util.ArrayList;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private ArrayList<Step> mSteps;
    private LayoutInflater mInflater;
    private ItemClickListener mListener;

    public StepAdapter(Context context, ItemClickListener listener, ArrayList<Step> steps) {
        this.mSteps = steps;
        this.mListener = listener;
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
        //holder = new StepViewHolder();
        String step = buildText(i);
        holder.stepItem.setText(step);


        if (!mSteps.get(i).getmVideoUrl().equals("")) {
            holder.stepItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.play_arrow, 0);
        }

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

        StepViewHolder(View itemView) {
            super(itemView);

            stepItem = itemView.findViewById(R.id.step_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mListener.onItemClickListener(position);
        }
    }
}
