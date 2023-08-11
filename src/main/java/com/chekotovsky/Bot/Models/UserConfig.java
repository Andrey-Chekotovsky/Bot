package com.chekotovsky.Bot.Models;

import lombok.*;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserConfig {
    @Getter
    @Setter
    int id;
    @Getter
    @Setter
    long userId;
    @Getter
    @Setter
    Boolean autoSendForAll;
    String authorities;
    public UserConfig(long userId)
    {
        this.userId = userId;
        autoSendForAll = false;
    }
    public String getAuthorities()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(autoSendForAll ? 1 : 0);
        return sb.toString();
    }
    public void setAuthorities(String authorities)
    {
        StringBuilder sb = new StringBuilder();
        autoSendForAll = authorities.charAt(0) == '1';
    }
}
