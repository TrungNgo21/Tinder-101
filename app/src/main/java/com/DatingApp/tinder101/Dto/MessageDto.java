package com.DatingApp.tinder101.Dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageDto {
  private String id;
  private String sentUserId;
  private String receivedUserId;
  private String messageContent;
  private Date createDate;
}
