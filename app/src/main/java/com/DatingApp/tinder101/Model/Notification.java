package com.DatingApp.tinder101.Model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notification {
  private String receiverId;
  private String notiContent;
  private String mode;
  private Date createdDate;
}
