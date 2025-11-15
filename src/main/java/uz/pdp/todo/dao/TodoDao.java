package uz.pdp.todo.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import uz.pdp.todo.enums.Priority;
import uz.pdp.todo.model.Todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TodoDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private static final String TITLE = "title";
    private static final String PRIORITY = "priority";

    public TodoDao(JdbcTemplate jdbcTemplate) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }
//    I have to add completed button and small info about update_at
//     high-medium-low ---> ⁕⁕⁕⁕⁕ with star or ɵɵɵɵɵ

    public void saveJdbcTemp(Todo todo) {
        String sql = "insert into todos(title, priority, user_id) values(?, ?::priority_type, ?)";
        jdbcTemplate.update(sql, todo.getTitle(), todo.getPriority().name(), todo.getUser_id());
    }


    public void saveSimpleJdbcTemp(Todo todo) {
        simpleJdbcInsert.withTableName("todos")
                .usingColumns(TITLE, PRIORITY)
                .execute(new BeanPropertySqlParameterSource(todo));
    }

    public void saveNamedJdbcTemp(Todo todo) {
        String sql = "insert into todos(title, priority) values(:title, cast(:priority as priority_type))";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(TITLE, todo.getTitle())
                .addValue(PRIORITY, todo.getPriority().name());
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public void saveSimpleJdbcCall(Todo todo) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("save_todo");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_title", todo.getTitle())
                .addValue("p_priority", todo.getPriority().name());
        jdbcCall.execute(params);
    }

    public Todo getByIdBeanPropertyRowMapper(Integer id) {
        String sql = "select * from todos where id = ?";
        return jdbcTemplate.queryForObject(sql,
                BeanPropertyRowMapper.newInstance(Todo.class), id);
    }

    public Todo getByIdRowMapper(Integer id) {
        String sql = "select * from todos where id = ?";

        RowMapper<Todo> rowMapper = (rs, rowNum) -> {
            Todo todo = new Todo();
            todo.setId(rs.getInt("id"));
            todo.setTitle(rs.getString(TITLE));
            return todo;
        };
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Todo getByIdNamedJdbcTemp(Integer id) {
        String sql = "select * from todos where id = :id";
        MapSqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue("id", id);
        return namedParameterJdbcTemplate.queryForObject(
                sql,
                paramSource,
                BeanPropertyRowMapper.newInstance(Todo.class));
    }

    public void deleteJdbcTemp(Integer id) {
        String sql = "delete from todos where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteNamedJdbcTemp(Integer id) {
        String sql = "delete from todos where id = :id";
        MapSqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue("id", id);
        namedParameterJdbcTemplate.update(sql, paramSource);
    }

    public void deleteSimpleJdbcTemp(Integer id) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("delete_todo");
        MapSqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue("id", id);
        simpleJdbcCall.execute(paramSource);
    }

    public void updateJdbcTemp(Integer id, String title, String priority) {
        String sql = "update todos " +
                "set title = ?, " +
                "priority = ?::priority_type, " +
                "updated_at = NOW() " +
                "where id = ?";
        jdbcTemplate.update(sql, title, priority, id);
    }

    public void updateNamedJdbcTemp(Integer id, String title, String priority) {
        String sql = "update todos set title = :title, priority = :priority where id = :id";
        MapSqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue(TITLE, title)
                .addValue(PRIORITY, priority)
                .addValue("id", id);
        namedParameterJdbcTemplate.update(sql, paramSource);
    }

    public void updateSimpleJdbcCall(Integer id, String title, String priority) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("update_todo");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue(TITLE, title)
                .addValue(PRIORITY, priority);
        jdbcCall.execute(params);
    }

    public List<Todo> getAllJdbcTempBeanPropertyRowMapper() {
        String sql = "select * from todos order by created_at desc";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Todo.class));
    }

    public List<Todo> getAllJdbcTemp() {
        String sql = "select * from todos order by created_at desc";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Todo todo = new Todo();
            todo.setId(rs.getInt("id"));
            todo.setTitle(rs.getString(TITLE));
            todo.setPriority(Priority.valueOf(rs.getString(PRIORITY)));

            Timestamp createdTs = rs.getTimestamp("created_at");
            if (createdTs != null) {
                todo.setCreated_at(createdTs.toLocalDateTime());
            }

            Timestamp updatedTs = rs.getTimestamp("updated_at");
            if (updatedTs != null) {
                todo.setUpdated_at(updatedTs.toLocalDateTime());
            }

            return todo;
        });
    }

    public List<Todo> getAllSimpleJdbcCall() {
        String sql = "select * from get_all_todos_func()";
        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Todo.class));
    }

    public List<Todo> getTodosByUserId(Long id) {
        if (id == null) return new ArrayList<>();

        String sql = "select * from todos where user_id = :id";
        MapSqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue("id", id);
        return namedParameterJdbcTemplate.query(
                sql,
                paramSource,
                new BeanPropertyRowMapper<>(Todo.class));
    }

}
