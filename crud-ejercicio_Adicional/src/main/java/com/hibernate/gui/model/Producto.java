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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Solo usa lo que marquemos
public class Producto {

    @Id
    @EqualsAndHashCode.Include // Este es el campo clave para comparar
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    
    private int codigo;

    @Column(name = "nombre")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Column(name = "stock")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private int stock;

    @Column(name = "precio")
    private int precio;
    
    @Column(name = "fotoUrl") // Ahora es una simple cadena de texto
    String fotoUrl;

    @ManyToMany(mappedBy = "productos", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default // Evita que Lombok cree el Set como null
    private Set<Distribuidor> distribuidores = new HashSet<>();
    
    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Compra> compras = new HashSet<>(); // Cambiado de List a Set
}
