package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.PostModel;
import com.careerdevs.jphsql.models.UserModel;
import com.careerdevs.jphsql.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:4000")
public class PostController {

    private String JPH_API_URL = "https://jsonplaceholder.typicode.com/posts";
    @Autowired
    private PostRepository postRepository;

    // getting all posts directly from JPH API
    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllPostsApi (RestTemplate restTemplate) {
        try {
            PostModel[] allPosts = restTemplate.getForObject(JPH_API_URL, PostModel[].class);
            return ResponseEntity.ok(allPosts);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // GET post from JPH API
    @GetMapping("/jph/all/{id}")
    public ResponseEntity<?> getJPHPostById(RestTemplate restTemplate, @PathVariable String id) {
        try {
            // throws NumberFormatException if id is not an int
            Integer.parseInt(id);

            System.out.println("Getting Post With ID: " + id);

            String url = JPH_API_URL + "/" + id;

            PostModel response = restTemplate.getForObject(url, PostModel.class);

            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("Post Not Found WIth ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());

            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //TODO: GET one post by ID (from SQL)
    @GetMapping("/sql/{id}")
    public ResponseEntity<?> getOnePostByID(@PathVariable String id) {
        try {
            // throws NumberFormatException if id is not an int
            int postId = Integer.parseInt(id);

            System.out.println("Getting Post With ID: " + id);

            // GET DATA FROM SQL using repository
            Optional<PostModel> foundPost = postRepository.findById(postId);

            if (!foundPost.isPresent()) return ResponseEntity.status(404).body("Post not found with ID: " + id);
            return ResponseEntity.ok(foundPost.get());

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("Post Not Found WIth ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // getting all users stored in our local mySQL database
    @GetMapping ("/sql/all")
    public ResponseEntity<?> getAllPostsSQL () {
        try {
            ArrayList<PostModel> allPosts = (ArrayList<PostModel>) postRepository.findAll();
            return ResponseEntity.ok(allPosts);
        } catch (Exception e) {
        System.out.println(e.getClass());
        System.out.println(e.getMessage());
        return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // delete post by id
    @DeleteMapping("/sql/{id}")
    public ResponseEntity<?> deleteOnePostByID(@PathVariable String id) {
        try {
            // throws NumberFormatException if id is not an int
            int postId = Integer.parseInt(id);

            System.out.println("Getting Post With ID: " + id);

            // GET DATA FROM SQL using repository
            Optional<PostModel> foundPost = postRepository.findById(postId);

            if (!foundPost.isPresent()) return ResponseEntity.status(404).body("Post not found with ID: " + id);
            postRepository.deleteById(postId);
            return ResponseEntity.ok(foundPost.get());

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("Post Not Found WIth ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // deletes all posts in SQL database
    @DeleteMapping("/sql/all")
    public ResponseEntity<?> deleteAllPostsSQL() {
        try {
            long count = postRepository.count();
            postRepository.deleteAll();
            return ResponseEntity.ok("Deleted Posts: " + count);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // getting all posts directly from JPH API into the SQL database
    @PostMapping("/all")
    public ResponseEntity<?> uploadAllPostsToSQL (RestTemplate restTemplate) {
        try {
            PostModel[] allPosts = restTemplate.getForObject(JPH_API_URL, PostModel[].class);
            assert allPosts != null;

            for (PostModel allPost : allPosts) {
                allPost.removeId();
            }

            postRepository.saveAll(Arrays.asList(allPosts));
            return ResponseEntity.ok(allPosts);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
            }
        }

    // post one post to SQL database
    @PostMapping
    public ResponseEntity<?> uploadOnePost (@RequestBody PostModel newPostData) {
        try {
            newPostData.removeId();
            PostModel savedPost = postRepository.save(newPostData);
            return ResponseEntity.ok(savedPost);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
                }
            }

    // put one post by id - id must exist
    @PutMapping("/sql/{id}")
    public ResponseEntity<?> updateOnePost(@PathVariable String id, @RequestBody PostModel updatePostData) {
        try {

            int postId = Integer.parseInt(id);
            Optional<PostModel> foundPost = postRepository.findById(postId);

            if (foundPost.isEmpty()) return ResponseEntity.status(400).body("Post not found - ID: " + postId);

            if (postId != updatePostData.getId()) return ResponseEntity.status(400).body("Post Ids did not match");
            postRepository.save(updatePostData);
            return ResponseEntity.ok(updatePostData);

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
