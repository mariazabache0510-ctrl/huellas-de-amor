package co.frameworks.huellas.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "adopciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascota mascota;

    /** Usuario con rol ADOPTANTE */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_adoptante", nullable = false)
    private Usuario adoptante;

    /** Usuario con rol VOLUNTARIO o ADMIN que resuelve la solicitud */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_voluntario")
    private Usuario voluntario;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fechaSolicitud;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoAdopcion estado;

    @Column(length = 500)
    private String observaciones;
}
