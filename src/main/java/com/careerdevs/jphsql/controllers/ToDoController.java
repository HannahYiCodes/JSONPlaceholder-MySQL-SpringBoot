package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.ToDoModel;
import com.careerdevs.jphsql.repositories.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class ToDoController {
    private String JPH_API_URL = "https://jsonplaceholder.typicode.com/todos";

    @Autowired
    private ToDoRepository toDoRepository;

    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllToDosApi(RestTemplate restTemplate) {
        try {
            ToDoModel[] allToDos = restTemplate.getForObject(JPH_API_URL, ToDoModel[].class);
            return ResponseEntity.ok(allToDos);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/sql/all")
    public ResponseEntity getAllToDosSQL() {
        try {
            ArrayList<ToDoModel> allToDos = (ArrayList<ToDoModel>) toDoRepository.findAll();
            return ResponseEntity.ok(allToDos);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/all")
    public ResponseEntity<?> uploadAllToDosToSQL (RestTemplate restTemplate) {
        try {

            ToDoModel[] allToDos = restTemplate.getForObject(JPH_API_URL, ToDoModel[].class);

            //TODO: remove id from each user

            assert allToDos != null;
            List<ToDoModel> savedToDos = toDoRepository.saveAll(Arrays.asList(allToDos));

            return ResponseEntity.ok(savedToDos);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> uploadOneToDo (@RequestBody ToDoModel newToDoData) {
        try {

            newToDoData.removeId();

            //TODO: Data validation on the new user data (make sure fields are valid values)

            ToDoModel savedToDo = toDoRepository.save(newToDoData);

            return ResponseEntity.ok(savedToDo);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
}