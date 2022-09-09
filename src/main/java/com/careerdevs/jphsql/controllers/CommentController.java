package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.CommentModel;
import com.careerdevs.jphsql.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private String JPH_API_URL = "https://jsonplaceholder.typicode.com/comments";

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllCommentsApi (RestTemplate restTemplate) {
        try {

            CommentModel[] allComments = restTemplate.getForObject(JPH_API_URL, CommentModel[].class);

            return ResponseEntity.ok(allComments);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("jph/all/id/{id}")
    public ResponseEntity<?> getJPHCommentById(RestTemplate restTemplate, @PathVariable String id) {
        try {
            Integer.parseInt(id);

            System.out.println("Getting Comment With ID: " + id);

            String url = JPH_API_URL + "/" + id;

            CommentModel response = restTemplate.getForObject(url, CommentModel.class);

            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("Comment Not Found With ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());

            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping ("/sql/all")
    public ResponseEntity<?> getAllCommentsSQL () {
        try {

            ArrayList<CommentModel> allComments = (ArrayList<CommentModel>) commentRepository.findAll();

            return ResponseEntity.ok(allComments);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/sql/all")
    public ResponseEntity<?> uploadAllCommentsToSQL (RestTemplate restTemplate) {
        try {
            CommentModel[] allComments = restTemplate.getForObject(JPH_API_URL, CommentModel[].class);
            assert allComments != null;
            for (CommentModel comment : allComments) {comment.removeId();}
            commentRepository.saveAll(Arrays.asList(allComments));
            return ResponseEntity.ok(allComments);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // MAX LENGTH

//    @PostMapping
//    public ResponseEntity<?> uploadOneComment (@RequestBody CommentModel newCommentData) {
//        try {
//
//            newCommentData.removeId();
//
//            //TODO: Data validation on the new user data (make sure fields are valid values)
//
//            CommentModel savedComment = commentRepository.save(newCommentData);
//
//            return ResponseEntity.ok(savedComment);
//
//        } catch (Exception e) {
//            System.out.println(e.getClass());
//            System.out.println(e.getMessage());
//            return ResponseEntity.internalServerError().body(e.getMessage());
//        }
//    }

    //    @GetMapping("sql/all/id/{id}")
//    public ResponseEntity<?> getSQLCommentById(RestTemplate restTemplate, @PathVariable String id) {
//        try {
//            Integer.parseInt(id);
//
//            System.out.println("Getting Comment With ID: " + id);
//
//            ArrayList<CommentModel> allComments = (ArrayList<CommentModel>) commentRepository.findAll();
//
//            return ResponseEntity.ok(allComments);
//
//            CommentModel response = restTemplate.getForObject(url, CommentModel.class);
//
//            return ResponseEntity.ok(response);
//
//        } catch (NumberFormatException e) {
//            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");
//
//        } catch (HttpClientErrorException.NotFound e) {
//            return ResponseEntity.status(404).body("Comment Not Found With ID: " + id);
//
//        } catch (Exception e) {
//            System.out.println(e.getClass());
//            System.out.println(e.getMessage());
//
//            return ResponseEntity.internalServerError().body(e.getMessage());
//        }
//    }
}
