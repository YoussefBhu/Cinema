package com.cinema.Service;

import com.cinema.Repository.*;
import com.cinema.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Stream;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService {
    @Autowired
    private VilleRepository villeRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private TicketRepository ticketRepository;

    public int i = 0;

    @Override
    public void initVilles() {
        Stream.of("Csablanca", "Marrakech", "Rabat", "Tanger").forEach(nameVille -> {

            Ville ville = new Ville();
            ville.setLongtitude(Math.random());
            ville.setLatitude(Math.random());
            ville.setAltitude(Math.random());
            ville.setName(nameVille);
            villeRepository.save(ville);
        });
    }

    @Override
    public void initCinemas() {
        villeRepository.findAll().forEach(ville -> {
            Stream.of("MegaRama", "IMAX", "FOUNOUN", "CHAHRAZAD", "DAOULIZ")
                    .forEach(nameCinema -> {
                        Cinema cinema = new Cinema();
                        cinema.setName(nameCinema);
                        cinema.setLongtitude(Math.random());
                        cinema.setLatitude(Math.random());
                        cinema.setAltitude(Math.random());
                        cinema.setNombreSalles(3 + (int) (Math.random() * 7));
                        cinema.setVille(ville);
                        cinemaRepository.save(cinema);
                    });
        });
    }

    @Override
    public void initSalles() {
        cinemaRepository.findAll().forEach(cinema -> {
            for (int i = 0; i < cinema.getNombreSalles(); i++) {
                Salle salle = new Salle();
                salle.setName("Salle" + (i + 1));
                salle.setNombrePlace(15 + (int) (Math.random() * 20));
                salle.setCinema(cinema);
                salleRepository.save(salle);
            }
        });
    }

    @Override
    public void initPlaces() {
        salleRepository.findAll().forEach(salle -> {
            for (int i = 0; i < salle.getNombrePlace(); i++) {
                Place place = new Place();
                place.setNumero(i + 1);
                place.setSalle(salle);
                placeRepository.save(place);
            }
        });
    }

    //Les sceances sont deja initialisée

    @Override
    public void initCategories() {
        Stream.of("Action", "Drama", "Comedy", "Horror", "science fiction").forEach(categories -> {
            Categorie c = new Categorie();
            c.setName(categories);
            categorieRepository.save(c);
        });
    }

    @Override
    public void initFilms() {
        List<Categorie> categories = categorieRepository.findAll();
        Stream.of("Spider-Man Far from home", "The green mile", "Why Him", "The Prodigy", "Star wars The last jedi").forEach(film -> {
            Film f = new Film();
            f.setTitre(film);
            f.setDescription("description");
            f.setRealisateur("realisateur");
            f.setDuree(2.5);
            f.setPhoto(film + ".jpg");
            f.setCategorie(categories.get(this.i));
            filmRepository.save(f);
            this.i++;
        });
        this.i = 0;
    }

    @Override
    public void initSeances() {
        int tab[] = {12, 14, 20, 22};
        for (int i = 0; i < 4; i++) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, tab[i]);
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            Date d = cal.getTime();
            Seance s = new Seance();
            s.setHeureDebut(d);
            seanceRepository.save(s);
        }
    }

    @Override
    public void initProjections() {
        System.out.println("Creation des projections ");
        List<Salle> Salles = salleRepository.findAll();
        List<Film> Films = filmRepository.findAll();
        List<Seance> Seances = seanceRepository.findAll();
        for (int x = 0; x < Salles.size(); x++) {
            for (int i = 0; i < Seances.size(); i++) {
                Projection p = new Projection();
                p.setPrix(50);
                p.setSalle(Salles.get(x));
                Random rand = new Random();
                p.setFilm(Films.get(rand.nextInt(Films.size())));
                p.setSeance(Seances.get(i));
                projectionRepository.save(p);
            }
        }
    }

    @Override
    public void initTickets() {
        List<Salle> salles = salleRepository.findAll();
        salleRepository.findAll().forEach(salle -> {
            salle.getProjection().forEach(projection -> {
                salle.getPlaces().forEach(place -> {
                    System.out.println("id : "+salle.getId()+" projections Id : "+projection.getId()+" place Id :"+place.getId());
                    Ticket t = new Ticket();
                    t.setPrix(projection.getPrix()) ;
                    t.setReserve(false) ;
                    t.setPlace(place);
                    t.setProjection(projection);
                    ticketRepository.save(t);
                });
            });
        });

        }
    }
