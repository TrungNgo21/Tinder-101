package com.DatingApp.tinder101.Activity;

import android.graphics.text.LineBreaker;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.Adapter.TeamMemberAdapter;
import com.DatingApp.tinder101.Model.TeamMember;
import com.DatingApp.tinder101.R;

import java.util.ArrayList;
import java.util.List;

public class AboutUsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TeamMemberAdapter adapter;
    private List<TeamMember> teamMembers; // Your data source

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);


        TextView titleTextView = findViewById(R.id.heading_about_us); // Make sure you have set the id in your layout

        String fullText = "About JellyBeans team";
        SpannableString spannableString = new SpannableString(fullText);

        int start = fullText.indexOf("JellyBean");
        int end = start + "JellyBean".length();

        spannableString.setSpan(
                new ForegroundColorSpan(getResources().getColor(R.color.primary_purple_700)),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        titleTextView.setText(spannableString);

        recyclerView = findViewById(R.id.team_members_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize your team members data here
        teamMembers = getTeamMembers();

        adapter = new TeamMemberAdapter(teamMembers);
        recyclerView.setAdapter(adapter);
    }

    // Mock data for example
    private List<TeamMember> getTeamMembers() {
        List<TeamMember> members = new ArrayList<>();
        members.add(new TeamMember(R.drawable.trung, "Ngo Lam Bao Trung", "Back-end Developer", "\"Quis aute aute enim veniam aliquip consectetur nisi esse magna ea ex irure excepteur ea irure non aliqua officia aliqua.\""));
        members.add(new TeamMember(R.drawable.khoi , "Nguyen Hoang Minh Khoi", "Back-end Developer", "\"Quis aute aute enim veniam aliquip consectetur nisi esse magna ea ex irure excepteur ea irure non aliqua officia aliqua.\""));
        members.add(new TeamMember(R.drawable.kien, "Vo Nguyen Kien", "Front-end Developer", "\"Quis aute aute enim veniam aliquip consectetur nisi esse magna ea ex irure excepteur ea irure non aliqua officia aliqua.\""));
        members.add(new TeamMember(R.drawable.loi, "Nguyen Phuc Loi", "Front-end Developer", "\"Quis aute aute enim veniam aliquip consectetur nisi esse magna ea ex irure excepteur ea irure non aliqua officia aliqua.\""));
        // Add more team members as needed
        return members;
    }
}
