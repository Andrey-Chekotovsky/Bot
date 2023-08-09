package com.chekotovsky.Bot.Dao;

import com.chekotovsky.Bot.Models.GroupId;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupIdMapper implements RowMapper<GroupId> {
    @Override
    public GroupId mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new GroupId().toBuilder()
                .id(rs.getLong("id"))
                .groupId(rs.getLong("group_id"))
                .chatId(rs.getLong("chat_id"))
                .groupName(rs.getString("group_name"))
                .build();
    }
}
