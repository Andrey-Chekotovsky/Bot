package com.chekotovsky.Bot.Dao;

import com.chekotovsky.Bot.Models.UserConfig;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserConfigMapper implements RowMapper<UserConfig> {
    @Override
    public UserConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
          UserConfig userConfig = new UserConfig().toBuilder()
                .id(rs.getInt("id"))
                .userId(rs.getInt("user_id"))
                .build();
          userConfig.setAuthorities(rs.getString("authorities"));
          return userConfig;
    }
}
