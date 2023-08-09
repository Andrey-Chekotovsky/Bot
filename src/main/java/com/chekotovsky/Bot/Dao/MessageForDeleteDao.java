package com.chekotovsky.Bot.Dao;

import com.chekotovsky.Bot.Models.GroupId;
import com.chekotovsky.Bot.Models.MessageForDelete;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Component
@RequiredArgsConstructor
public class MessageForDeleteDao {
    private final JdbcTemplate jdbcTemplate;
    public List<MessageForDelete> selectAll()
    {
        return jdbcTemplate.query("SELECT * FROM delete_messages;", new MessageDeleteMapper());
    }
    public void insert(MessageForDelete message) throws DuplicateKeyException
    {
        jdbcTemplate.update("INSERT INTO delete_messages(chat_id, message_id) VALUES (?, ?);",
                message.getChatId(), message.getMessageId());
    }
    public void deleteAll()
    {
        jdbcTemplate.update("DELETE FROM delete_messages WHERE id > 0;");
    }
}
