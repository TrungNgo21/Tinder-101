package com.DatingApp.tinder101.Model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Conversation {
  private String sentUserId;
  private String receivedUserId;
  private String messageContent;
  private String conversionUrl;
  private String conversionId;
  private String conversionName;
  private Date createDate;
}
