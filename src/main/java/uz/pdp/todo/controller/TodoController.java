package uz.pdp.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.todo.enums.Priority;
import uz.pdp.todo.model.Todo;
import uz.pdp.todo.service.TodoService;

import java.util.List;

@Controller
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;
    private static final String REDIRECT_TODO = "redirect:/todos";

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // faqat current userning todolari
    @GetMapping
    public String get(Model model) {
        List<Todo> todos = todoService.getTodosForCurrentUser();
        model.addAttribute("todos", todos);
        return "todos";
    }

    @GetMapping("/new")
    public String newTodo(Model model) {
        model.addAttribute("todo", new Todo());
        model.addAttribute("actionUrl", "/todos");
        model.addAttribute("method", "post");
        return "todo_form";
    }

    // create
    @PostMapping
    public String create(@RequestParam String title,
                         @RequestParam Priority priority) {
        todoService.addForCurrentUser(title, priority);
        return REDIRECT_TODO;
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        Todo todo = todoService.findById(id);

        // faqat o‘zining todosini ochish
        if (todo == null || !todoService.isOwner(todo)) {
            return REDIRECT_TODO;
        }

        model.addAttribute("todo", todo);
        model.addAttribute("actionUrl", "/todos/" + id);
        model.addAttribute("method", "post");
        return "todo_form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Integer id,
                         @RequestParam String title,
                         @RequestParam Priority priority) {
        todoService.updateForCurrentUser(id, title, priority);
        return REDIRECT_TODO;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        todoService.deleteForCurrentUser(id);
        return REDIRECT_TODO;
    }
}
