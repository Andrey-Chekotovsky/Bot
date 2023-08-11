package com.chekotovsky.Bot.Dao;

import com.chekotovsky.Bot.Models.MessageForDelete;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageDeleteMapper implements RowMapper<MessageForDelete> {
    @Override
    public MessageForDelete mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MessageForDelete().toBuilder()
                .id(rs.getInt("id"))
                .messageId(rs.getInt("message_id"))
                .chatId(rs.getLong("chat_id"))
                .build();
    }
}
