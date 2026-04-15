package com.hibernate.gui.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "distribuidor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Solo usa lo que marquemos
public class Distribuidor {

    @Id
    @EqualsAndHashCode.Include // Este es el campo clave para comparar
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idDis")
    
    int codigo;

    @Column(name = "nombre")
    String nombre;

    @Column(name = "anyo_inicio")
    int anyo_inicio;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "PxD",
        joinColumns = @JoinColumn(name = "idDist"),
        inverseJoinColumns = @JoinColumn(name = "idProd")
    )
    @ToString.Exclude
    @Builder.Default // Evita que Lombok cree el Set como null al usar Builder
    private Set<Producto> productos = new HashSet<>();
	
}
