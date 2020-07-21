package com.cinema;


import com.cinema.Service.Controllers;
import com.cinema.Service.ICinemaInitService;
import com.cinema.entities.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
    @Autowired
    private RepositoryRestConfiguration restConfiguration ;
    @Autowired
    private ICinemaInitService cinemaInitService ;
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
            restConfiguration.exposeIdsFor(Ticket.class);
            cinemaInitService.initVilles();
            cinemaInitService.initCinemas();
            cinemaInitService.initSalles();
            cinemaInitService.initPlaces();
            cinemaInitService.initCategories();
            cinemaInitService.initFilms();
            cinemaInitService.initSeances();
            cinemaInitService.initProjections();
            cinemaInitService.initTickets();
    }
}
