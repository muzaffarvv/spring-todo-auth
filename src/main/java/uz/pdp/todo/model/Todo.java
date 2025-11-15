package uz.pdp.todo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.todo.enums.Priority;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Todo {
    private Integer id;
    private String title;
    private Priority priority = Priority.MEDIUM;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;
    private Long user_id;

    @JsonCreator
    public static Priority from(String value) {
        return Priority.valueOf(value.toUpperCase());
    }

}
