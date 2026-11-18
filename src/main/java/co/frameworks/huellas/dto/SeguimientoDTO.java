package co.frameworks.huellas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeguimientoDTO {

    private Long id;

    @NotNull
    private Long mascotaId;

    private String mascotaNombre;

    @NotNull
    private Long adoptanteId;

    private String adoptanteNombre;

    @NotNull
    private LocalDate fechaVisita;

    @NotBlank
    private String comentarios;
}
