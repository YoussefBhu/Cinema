package com.cinema.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private int numero ;
    private double longtitude , latitude , altitude ;
    @ManyToOne
    private Salle salle ;
    @OneToMany(mappedBy = "place")
    private Collection<Ticket> tickets ;

}
