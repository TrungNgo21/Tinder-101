package com.DatingApp.tinder101.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamMember {
    private int photo;
    private String name;
    private String role;
    private String description;
}
