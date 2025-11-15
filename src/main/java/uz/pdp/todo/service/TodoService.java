package uz.pdp.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.todo.dao.AuthUserDao;
import uz.pdp.todo.dao.TodoDao;
import uz.pdp.todo.enums.Priority;
import uz.pdp.todo.model.AuthUser;
import uz.pdp.todo.model.Todo;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TodoService {

    private final TodoDao todoDao;
    private final AuthUserDao authUserDao;

    @Autowired
    public TodoService(TodoDao dao, AuthUserDao authUserDao) {
        this.todoDao = dao;
        this.authUserDao = authUserDao;
    }

    public List<Todo> getAll() {
        return todoDao.getAllJdbcTemp();
    }

    public Todo findById(Integer id) {
        return todoDao.getByIdBeanPropertyRowMapper(id);
    }

    public List<Todo> getTodosForCurrentUser() {
        AuthUser authUser = getCurrentUser();
        return todoDao.getTodosByUserId(authUser.getId());
    }

    public void addForCurrentUser(String title, Priority priority) {
        AuthUser authUser = getCurrentUser();

        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setPriority(priority);
        todo.setCreated_at(LocalDateTime.now());
        todo.setUser_id(authUser.getId()); // muhim!

        todoDao.saveJdbcTemp(todo);
    }

    public void updateForCurrentUser(Integer id, String title, Priority priority) {
        Todo existing = findById(id);
        if (existing != null && isOwner(existing)) {
            todoDao.updateJdbcTemp(id, title, priority.name());
        }
    }

    public void deleteForCurrentUser(Integer id) {
        Todo existing = findById(id);
        if (existing != null && isOwner(existing)) {
            todoDao.deleteJdbcTemp(id);
        }
    }

    public boolean isOwner(Todo todo) {
        AuthUser authUser = getCurrentUser();
        return Objects.equals(todo.getUser_id(), authUser.getId());
    }

    private AuthUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return authUserDao.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}
