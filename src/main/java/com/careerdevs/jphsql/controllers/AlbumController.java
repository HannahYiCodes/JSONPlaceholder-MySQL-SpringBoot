package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.AlbumModel;
import com.careerdevs.jphsql.repositories.AlbumRepository;
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
@RequestMapping("/api/albums")
@CrossOrigin(origins = "http://localhost:4000")
public class AlbumController {
    private String JPH_API_URL = "https://jsonplaceholder.typicode.com/albums";

    @Autowired
    private AlbumRepository albumRepository;

    // getting all albums directly from JPH API
    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllAlbumsApi (RestTemplate restTemplate) {
        try {
            AlbumModel[] allAlbums = restTemplate.getForObject(JPH_API_URL, AlbumModel[].class);
            return ResponseEntity.ok(allAlbums);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // GET album from JPH API
    @GetMapping("/jph/all/{id}")
    public ResponseEntity<?> getJPHAlbumById(RestTemplate restTemplate, @PathVariable String id) {
        try {
            // throws NumberFormatException if id is not an int
            Integer.parseInt(id);

            System.out.println("Getting Album With ID: " + id);

            String url = JPH_API_URL + "/" + id;

            AlbumModel response = restTemplate.getForObject(url, AlbumModel.class);

            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("Album Not Found WIth ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());

            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //TODO: GET one album by ID (from SQL)
    @GetMapping("/sql/{id}")
    public ResponseEntity<?> getOneAlbumByID(@PathVariable String id) {
        try {
            // throws NumberFormatException if id is not an int
            int albumId = Integer.parseInt(id);

            System.out.println("Getting Album With ID: " + id);

            // GET DATA FROM SQL using repository
            Optional<AlbumModel> foundAlbum = albumRepository.findById(albumId);

            if (!foundAlbum.isPresent()) return ResponseEntity.status(404).body("Album not found with ID: " + id);
            return ResponseEntity.ok(foundAlbum.get());

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("Album Not Found WIth ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // getting all users stores in our local MySQL database
    @GetMapping("/sql/all")
    public ResponseEntity<?> getAllAlbumsSQL() {
        try {
            ArrayList<AlbumModel> allAlbums = (ArrayList<AlbumModel>) albumRepository.findAll();
            return ResponseEntity.ok(allAlbums);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // delete album by id
    @DeleteMapping("/sql/{id}")
    public ResponseEntity<?> deleteOneAlbumByID(@PathVariable String id) {
        try {
            // throws NumberFormatException if id is not an int
            int albumId = Integer.parseInt(id);

            System.out.println("Getting Album With ID: " + id);

            // GET DATA FROM SQL using repository
            Optional<AlbumModel> foundAlbum = albumRepository.findById(albumId);

            if (!foundAlbum.isPresent()) return ResponseEntity.status(404).body("Album not found with ID: " + id);

            albumRepository.deleteById(albumId);
            return ResponseEntity.ok(foundAlbum.get());

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("Album Not Found WIth ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // deletes all albums in SQL database
    @DeleteMapping("/sql/all")
    public ResponseEntity<?> deleteAllAlbumsSQL() {
        try {
            long count = albumRepository.count();
            albumRepository.deleteAll();
            return ResponseEntity.ok("Deleted Albums: " + count);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // getting all albums directly from the JPH API into the SQL database
    @PostMapping("/all")
    public ResponseEntity<?> uploadAllAlbumsToSQL (RestTemplate restTemplate) {
        try {

            AlbumModel[] allAlbums = restTemplate.getForObject(JPH_API_URL, AlbumModel[].class);

            //TODO: remove id from each user

            assert allAlbums != null;
            for (AlbumModel allAlbum : allAlbums) {
                allAlbum.removeId();
            }
            List<AlbumModel> savedAlbums = albumRepository.saveAll(Arrays.asList(allAlbums));

            return ResponseEntity.ok(savedAlbums);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // post one user to SQL database
    @PostMapping
    public ResponseEntity<?> uploadOneAlbum (@RequestBody AlbumModel newAlbumData) {
        try {

            newAlbumData.removeId();

            //TODO: Data validation on the new user data (make sure fields are valid values)

            AlbumModel savedAlbum = albumRepository.save(newAlbumData);

            return ResponseEntity.ok(savedAlbum);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    // put one album by id - id must exist
    @PutMapping("/sql/{id}")
    public ResponseEntity<?> updateOneAlbum (@PathVariable String id, @RequestBody AlbumModel updateAlbumData) {
        try {
            int albumId = Integer.parseInt(id);
            Optional<AlbumModel> foundAlbum = albumRepository.findById(albumId);

            if (foundAlbum.isEmpty()) return ResponseEntity.status(400).body("Album not found - ID: " + albumId);

            if (albumId != updateAlbumData.getId()) return ResponseEntity.status(400).body("Album Ids did not match");
            albumRepository.save(updateAlbumData);
            return ResponseEntity.ok(updateAlbumData);

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
