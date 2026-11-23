package co.frameworks.huellas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(unique = true, nullable = false, length = 150)
    private String username;

    @Column(unique = true, nullable = false, length = 150)
    private String correo;

    @Column(nullable = false, length = 150)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    @Column(length = 20)
    private String telefono;

    @Column(length = 150)
    private String direccion;

    /** Datos de perfil cuando el rol es ADOPTANTE */
    @Column(length = 300)
    private String motivacion;

    @Column(name = "tiene_experiencia")
    private Boolean tieneExperiencia;

    @Column(length = 100)
    private String vivienda;

    /** Datos de perfil cuando el rol es VOLUNTARIO */
    @Column(length = 150)
    private String especialidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_refugio")
    private Refugio refugio;

    @Builder.Default
    @Column(nullable = false)
    private boolean activo = true;
}
