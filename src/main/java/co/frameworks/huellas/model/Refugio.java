package co.frameworks.huellas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refugios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refugio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String direccion;

    @Column(length = 150)
    private String contacto;

    @Column(length = 150)
    private String correo;

    @Column(length = 100)
    private String telefono;

    @Column(length = 300)
    private String descripcion;
}
