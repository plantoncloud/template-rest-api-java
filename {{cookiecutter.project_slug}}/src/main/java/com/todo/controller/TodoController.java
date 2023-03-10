package com.todo.controller;

import com.todo.dto.Todo;
import com.todo.exception.InvalidTodoException;
import com.todo.exception.TodoNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
public class TodoController {

    // this is for demo purposes. remove it when using for production use.
    private static HashMap<String, String> defaultRoutes;
    static {
        defaultRoutes = new HashMap<String, String>();
        defaultRoutes.put("GET", "/todos");
        defaultRoutes.put("GET", "/todos/{id}");
        defaultRoutes.put("POST", "/todos");
        defaultRoutes.put("PUT", "/todos/{id}");
        defaultRoutes.put("DELETE", "/todos/{id}");
    }
    // This is an in-memory store for the purposes of this example.
    // You would typically use a database like MySQL or MongoDB in a real app.
    private static List<Todo> todos = new ArrayList<>();

    static {
        todos.add(new Todo(1, "Learn Java", false));
        todos.add(new Todo(2, "Learn Spring Boot", false));
    }

    @GetMapping("/")
    public Map<String, String> getDefaultRoutes() {
        return defaultRoutes;
    }

    @GetMapping("/todos")
    public List<Todo> getTodos() {
        return todos;
    }

    @GetMapping("/todos/{id}")
    public Todo getTodo(@PathVariable int id) throws TodoNotFoundException {
        Optional<Todo> todo = todos.stream().filter(t -> t.getId() == id).findFirst();
        if (todo.isPresent()) {
            return todo.get();
        } else {
            throw new TodoNotFoundException();
        }
    }

    @PostMapping("/todos")
    public Todo createTodo(@RequestBody Todo todo) throws InvalidTodoException {
        if (todo.getTask().length() < 3) {
            throw new InvalidTodoException();
        }
        todo.setId(todos.size() + 1);
        todos.add(todo);
        return todo;
    }

    @PutMapping("/todos/{id}")
    public Todo updateTodo(@PathVariable int id, @RequestBody Todo todo)
            throws TodoNotFoundException, InvalidTodoException {
        Optional<Todo> existingTodo = todos.stream().filter(t -> t.getId() == id).findFirst();
        if (!existingTodo.isPresent()) {
            throw new TodoNotFoundException();
        }
        if (todo.getTask().length() < 3) {
            throw new InvalidTodoException();
        }
        existingTodo.get().setTask(todo.getTask());
        return existingTodo.get();
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable long id) {
        todos.removeIf(t -> t.getId() == id);
    }
}
