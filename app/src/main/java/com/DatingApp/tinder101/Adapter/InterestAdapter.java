package com.DatingApp.tinder101.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.R;

import java.util.List;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.InterestViewHolder> {
    private Context mContext;
    private List<String> interestList;

    public InterestAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void setData(List<String> list){
        this.interestList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interest_card, parent, false);
        return new InterestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestViewHolder holder, int position) {
        String interest = interestList.get(position);
        if(interest == null){
            return;
        }

        holder.interest_text.setText(interest);
    }

    @Override
    public int getItemCount() {
        if(interestList != null){
            return interestList.size();
        }
        return 0;
    }

    public class InterestViewHolder extends RecyclerView.ViewHolder{
        private TextView interest_text;

        public InterestViewHolder(@NonNull View itemView) {
            super(itemView);
            interest_text = itemView.findViewById(R.id.interest_text);
        }
    }
}
