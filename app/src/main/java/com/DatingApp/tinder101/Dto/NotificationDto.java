package com.DatingApp.tinder101.Dto;

import org.checkerframework.checker.units.qual.A;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationDto {
  private String receiverId;
  private String content;
  private Date createdDate;
}
