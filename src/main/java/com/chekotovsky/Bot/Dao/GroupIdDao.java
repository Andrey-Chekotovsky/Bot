package com.chekotovsky.Bot.Dao;

import com.chekotovsky.Bot.Models.GroupId;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GroupIdDao {
    private final JdbcTemplate jdbcTemplate;
    public List<GroupId> selectByChatId(Long chatId)
    {
        return jdbcTemplate.query("SELECT * FROM groups WHERE chat_id = ?;", new Object[]{chatId},
                new GroupIdMapper());
    }

    public void insert(GroupId groupId) throws DuplicateKeyException
    {
        jdbcTemplate.update("INSERT INTO groups(chat_id, group_id, group_name) VALUES (?, ?, ?);",
                groupId.getChatId(), groupId.getGroupId(), groupId.getGroupName());
    }
    public void deleteByLinkedGroup(Long chatId, Long groupId) throws DuplicateKeyException
    {
        jdbcTemplate.update("DELETE FROM groups WHERE chat_id = ? AND group_id = ?;",
                chatId, groupId);
    }
}
