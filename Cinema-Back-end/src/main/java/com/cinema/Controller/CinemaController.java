package com.cinema.Controller;

import com.cinema.Repository.CinemaRepository;
import com.cinema.entities.Cinema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CinemaController  {
    @Autowired
    private CinemaRepository cinemaRepository;
    @GetMapping("/cines")
    public List<Cinema> cinemas(){
        return cinemaRepository.findAll();
    }
    @GetMapping("/cines/{id}")
    public Cinema cinema(@PathVariable Long id ){
        return cinemaRepository.findById(id).get();
    }
    @DeleteMapping("/cines/{id}")
    public void delete(@PathVariable Long id ){
        cinemaRepository.deleteById(id);
    }
    @PutMapping("/cines/{id}")
    public void update(@RequestBody Cinema cinema , @PathVariable Long id ){
        cinema.setId(id);
        cinemaRepository.save(cinema);
    }

}
