package co.frameworks.huellas.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "seguimientos_postadopcion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeguimientoPostAdopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascota mascota;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adoptante", nullable = false)
    private Adoptante adoptante;

    @Column(name = "fecha_visita", nullable = false)
    private LocalDate fechaVisita;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comentarios;
}
