package com.DatingApp.tinder101.Model;

import com.DatingApp.tinder101.Dto.MessageDto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message {
  private String sentUserId;
  private String receivedUserId;
  private String messageContent;
  private Date createDate;

  public MessageDto toDto() {
    return MessageDto.builder()
        .sentUserId(sentUserId)
        .receivedUserId(receivedUserId)
        .messageContent(messageContent)
        .createDate(createDate)
        .build();
  }
}
