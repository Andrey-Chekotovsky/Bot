package com.chekotovsky.Bot.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class GroupId {
    long id;
    private Long groupId;
    private Long chatId;
    private String groupName;
}
