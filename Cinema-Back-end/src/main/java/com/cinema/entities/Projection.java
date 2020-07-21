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
public class Projection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private Date dateProjection ;
    private double prix ;
    @ManyToOne
    private Salle salle ;
    @ManyToOne
    private Film film ;
    @OneToMany(mappedBy = "projection")
    private Collection<Ticket> tickets ;
    @ManyToOne
    private Seance seance ;
}
