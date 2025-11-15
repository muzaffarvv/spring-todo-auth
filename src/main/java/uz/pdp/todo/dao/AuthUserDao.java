package uz.pdp.todo.dao;

import org.jetbrains.annotations.NotNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import uz.pdp.todo.enums.Role;
import uz.pdp.todo.model.AuthUser;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class AuthUserDao {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String IS_BLOCKED = "is_blocked";

    public AuthUserDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    private final RowMapper<AuthUser> rowMapper = (rs, rowNum) -> AuthUser.builder()
            .id(rs.getLong("id"))
            .username(rs.getString(USERNAME))
            .password(rs.getString(PASSWORD))
            .role(Role.valueOf(rs.getString("role")))
            .isBlocked(rs.getBoolean(IS_BLOCKED))
            .build();

    public Long save(@NotNull AuthUser authUser) {
        String sql = """
                insert into auth_user(username, password, role, is_blocked)
                values(:username, :password, cast(:role as role_type), :is_blocked)
                """;
        MapSqlParameterSource mapSqlParamSource =
                new MapSqlParameterSource()
                        .addValue(USERNAME, authUser.getUsername())
                        .addValue(PASSWORD, authUser.getPassword())
                        .addValue("role", authUser.getRole().name())
                        .addValue(IS_BLOCKED, authUser.isBlocked());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(sql, mapSqlParamSource, keyHolder, new String[]{"id"});
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<AuthUser> findByUsername(String username) {
        String sql = "select * from auth_user where username = :username";
        try {
            AuthUser user = namedJdbcTemplate.queryForObject(
                    sql,
                    new MapSqlParameterSource().addValue(USERNAME, username),
                    rowMapper
            );
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<AuthUser> getAll() {
        String sql = """
                select *
                from auth_user
                order by
                    is_blocked asc,
                    case when role = 'ADMIN' then 1 else 2 end
                """;
        return namedJdbcTemplate.query(sql, rowMapper);
    }


    public void delete(Long id) {
        String sql = "delete from auth_user where id = :id";
        namedJdbcTemplate.update(sql, paramsById(id));
    }

    public void changeRole(Long id, Role newRole) {
        String sql = "update auth_user set role = cast(:role as role_type) where id = :id";
        namedJdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("role", newRole.name()));
    }

    public void toggleBlockStatus(Long id) {
        String sql = "update auth_user set is_blocked = not is_blocked where id = :id";
        namedJdbcTemplate.update(sql, paramsById(id));
    }

    public int update(Long id, AuthUser authUser) {
        String sql = """
                update auth_user set username = :username,
                password = :password,
                role = cast(:role as role_type),
                is_blocked = :is_blocked
                where id = :id
                """;
        return namedJdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue(USERNAME, authUser.getUsername())
                        .addValue(PASSWORD, authUser.getPassword())
                        .addValue("role", authUser.getRole().name())
                        .addValue(IS_BLOCKED, authUser.isBlocked()));
    }

    private MapSqlParameterSource paramsById(Long id) {
        return new MapSqlParameterSource().addValue("id", id);
    }
}