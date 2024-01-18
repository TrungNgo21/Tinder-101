package com.DatingApp.tinder101.Adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.Model.TeamMember;
import com.DatingApp.tinder101.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class TeamMemberAdapter extends RecyclerView.Adapter<TeamMemberAdapter.TeamMemberViewHolder> {

    private List<TeamMember> teamMembers;

    @NonNull
    @Override
    public TeamMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_member, parent, false);
        return new TeamMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMemberViewHolder holder, int position) {
        TeamMember member = teamMembers.get(position);
        holder.memberName.setText(member.getName());
        holder.memberRole.setText(member.getRole());
        holder.memberDescription.setText(member.getDescription());

        // If you have URLs for images, use a library like Glide or Picasso to load images
        holder.memberImage.setImageResource(member.getPhoto());
    }

    @Override
    public int getItemCount() {
        return teamMembers.size();
    }

    static class TeamMemberViewHolder extends RecyclerView.ViewHolder {
        ImageView memberImage;
        TextView memberName;
        TextView memberRole;
        TextView memberDescription;

        TeamMemberViewHolder(View itemView) {
            super(itemView);
            memberImage = itemView.findViewById(R.id.member_image);
            memberName = itemView.findViewById(R.id.member_name);
            memberRole = itemView.findViewById(R.id.member_role);
            memberDescription = itemView.findViewById(R.id.member_description);
        }
    }
}
