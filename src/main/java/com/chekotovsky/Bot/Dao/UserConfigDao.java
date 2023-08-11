package com.chekotovsky.Bot.Dao;
import com.chekotovsky.Bot.Models.UserConfig;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserConfigDao {
    private final JdbcTemplate jdbcTemplate;
    public UserConfig selectByUserId(long id) throws NotFoundException
    {
        return jdbcTemplate.query("SELECT * FROM user_configs WHERE user_id = ?;",  new Object[]{id},
                new UserConfigMapper()).stream().findAny().orElseThrow(NotFoundException::new);
    }
    public void updateAuthorities(UserConfig userConfig)
    {
        jdbcTemplate.update("UPDATE user_configs SET authorities = ? WHERE user_id = ?;",
                userConfig.getAuthorities(), userConfig.getUserId());
    }
    public void insert(UserConfig message) throws DuplicateKeyException
    {
        jdbcTemplate.update("INSERT INTO user_configs(user_id, authorities) VALUES (?, ?);",
                message.getUserId(), message.getAuthorities());
    }
}
