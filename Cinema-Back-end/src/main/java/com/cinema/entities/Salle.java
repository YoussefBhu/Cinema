package com.cinema.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Salle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String name ;
    private int nombrePlace ;
    @ManyToOne  @JoinColumn(name = "id_cinema")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Cinema cinema ;
    @OneToMany(mappedBy = "salle")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Collection<Place> places ;
    @OneToMany(mappedBy = "salle")
    private Collection<Projection> projection ;
}
