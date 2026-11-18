package co.frameworks.huellas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "voluntarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voluntario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_refugio")
    private Refugio refugio;

    @Column(length = 150)
    private String especialidad;
}
