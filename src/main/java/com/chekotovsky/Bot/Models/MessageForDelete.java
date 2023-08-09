package com.chekotovsky.Bot.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MessageForDelete {
    long id;
    private int messageId;
    private Long chatId;
}
