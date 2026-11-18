package co.frameworks.huellas.dto;

import co.frameworks.huellas.model.EstadoAdopcion;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdopcionDTO {

    private Long id;

    @NotNull(message = "La mascota es obligatoria")
    private Long mascotaId;

    private String mascotaNombre;

    private Long adoptanteId;

    private String adoptanteNombre;

    private Long voluntarioId;

    private String voluntarioNombre;

    private LocalDate fechaSolicitud;

    private EstadoAdopcion estado;

    @Size(max = 500)
    private String observaciones;
}
