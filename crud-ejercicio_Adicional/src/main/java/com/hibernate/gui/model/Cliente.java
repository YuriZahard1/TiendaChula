package com.hibernate.gui.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.*;


@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Solo usa lo que marquemos
public class Cliente {

    @Id
    @EqualsAndHashCode.Include // Este es el campo clave para comparar
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idCLiente")
    int codigo;

    @Column(name = "nombre")
    @NotBlank(message = "El nombre del cliente no puede estar vacío")
    @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    String nombre;

    @Column(name = "edad")
    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 120, message = "Edad no válida")
    int edad;
    
    @jakarta.persistence.Lob
    @Column(name = "foto", columnDefinition = "LONGBLOB") // LONGBLOB para MySQL
    private byte[] foto;
	
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Compra> compras = new HashSet<>(); // Cambiado de List a Set
}
