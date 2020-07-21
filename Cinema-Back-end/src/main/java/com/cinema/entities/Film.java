package com.cinema.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String titre ;
    private String description ;
    private String realisateur ;
    private Date dateSortie ;
    private double duree ;
    private String photo ;
    @OneToMany(mappedBy = "film")
    private Collection<Projection> projections ;
    @ManyToOne
    private Categorie categorie ;
}
