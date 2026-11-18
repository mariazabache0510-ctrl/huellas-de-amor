package co.frameworks.huellas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "adoptantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adoptante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(length = 300)
    private String motivacion;

    @Column(name = "tiene_experiencia")
    private boolean tieneExperiencia;

    @Column(name = "vivienda", length = 100)
    private String vivienda;
}
