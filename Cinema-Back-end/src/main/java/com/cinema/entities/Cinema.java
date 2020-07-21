package com.cinema.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
@Setter
@Getter
public class Cinema implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String name ;
    private int nombreSalles;
    private double longtitude , latitude , altitude ;
    @OneToMany(mappedBy = "cinema")
    @JsonBackReference //infinite-recursion
    private Collection <Salle> salles ;
    @ManyToOne
    @JsonManagedReference
    private Ville ville ;
}
