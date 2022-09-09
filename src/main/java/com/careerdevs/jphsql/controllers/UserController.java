package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.UserModel;
import com.careerdevs.jphsql.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4000")
public class UserController {
    private String JPH_API_URL = "https://jsonplaceholder.typicode.com/users";

    @Autowired
    private UserRepository userRepository;

    // getting all users directly from JPH API
    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllUsersApi (RestTemplate restTemplate) {
        try {
            UserModel[] allUsers = restTemplate.getForObject(JPH_API_URL, UserModel[].class);
            return ResponseEntity.ok(allUsers);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // GET user from JPH API
    @GetMapping("/jph/all/{id}")
    public ResponseEntity<?> getJPHUserById(RestTemplate restTemplate, @PathVariable String id) {
        try {
            // throws NumberFormatException if id is not an int
            Integer.parseInt(id);

            System.out.println("Getting User With ID: " + id);

            String url = JPH_API_URL + "/" + id;

            UserModel response = restTemplate.getForObject(url, UserModel.class);

            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("User Not Found WIth ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());

            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //TODO: GET one user by ID (from SQL)
    @GetMapping("/sql/{id}")
    public ResponseEntity<?> getOneUserByID(@PathVariable String id) {
        try {
            // throws NumberFormatException if id is not an int
            int userId = Integer.parseInt(id);

            System.out.println("Getting User With ID: " + id);

            // GET DATA FROM SQL using repository
            Optional<UserModel> foundUser = userRepository.findById(userId);

            if (!foundUser.isPresent()) return ResponseEntity.status(404).body("User not found with ID: " + id);
//            if (foundUser.isEmpty()) throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            return ResponseEntity.ok(foundUser.get());

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("User Not Found WIth ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // getting all users stored in our local MySQL database
    @GetMapping("/sql/all")
    public ResponseEntity<?> getAllUsersSQL() {
        try {

            ArrayList<UserModel> allUsers = (ArrayList<UserModel>) userRepository.findAll();

            return ResponseEntity.ok(allUsers);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // delete user by id
    @DeleteMapping("/sql/{id}")
    public ResponseEntity<?> deleteOneUserByID(@PathVariable String id) {
        try {
            // throws NumberFormatException if id is not an int
            int userId = Integer.parseInt(id);

            System.out.println("Getting User With ID: " + id);

            // GET DATA FROM SQL using repository
            Optional<UserModel> foundUser = userRepository.findById(userId);

            if (!foundUser.isPresent()) return ResponseEntity.status(404).body("User not found with ID: " + id);
//            if (foundUser.isEmpty()) throw new HttpClientErrorException(HttpStatus.NOT_FOUND);

            userRepository.deleteById(userId);
            return ResponseEntity.ok(foundUser.get());

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("User Not Found WIth ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // deletes all users in SQL database
    @DeleteMapping("/sql/all")
    public ResponseEntity<?> deleteAllUsersSQL() {
        try {
            long count = userRepository.count();
            userRepository.deleteAll();
            return ResponseEntity.ok("Deleted Users: " + count);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // getting all users directly from JPH API into the SQL database
    @PostMapping("/sql/all")
    public ResponseEntity<?> uploadAllUsersToSQL(RestTemplate restTemplate) {
        try {

            // retrieve data from JPH API and save to array of UserModels
            UserModel[] allUsers = restTemplate.getForObject(JPH_API_URL, UserModel[].class);

            // checks if allUsers is present, otherwise exception will be thrown
            assert allUsers != null;

            // remove id from each user
            for (UserModel allUser : allUsers) {
                allUser.removeId();
            }

            // saves users to database and updates each user's id field to the saved database ID
            userRepository.saveAll(Arrays.asList(allUsers));

            // response with data that was just saved to the database
            return ResponseEntity.ok(allUsers);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    // post one user to SQL database
    @PostMapping
    public ResponseEntity<?> uploadOneUser(@RequestBody UserModel newUserData) {
        try {
            newUserData.removeId();

            //TODO: Data validation on the new user data (make sure fields are valid values)

            userRepository.save(newUserData);
            return ResponseEntity.ok(newUserData);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // put one user by id - id must exist
    @PutMapping("/sql/{id}")
    public ResponseEntity<?> updateOneUser(@PathVariable String id, @RequestBody UserModel updateUserData) {
        try {

            int userId = Integer.parseInt(id);
            Optional<UserModel> foundUser = userRepository.findById(userId);

            if (foundUser.isEmpty()) return ResponseEntity.status(404).body("User not found - ID: " + userId);

            if (foundUser.get().getId() != updateUserData.getId()) return ResponseEntity.status(400).body("User Ids did not match");
            userRepository.save(updateUserData);
            return ResponseEntity.ok(updateUserData);

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}