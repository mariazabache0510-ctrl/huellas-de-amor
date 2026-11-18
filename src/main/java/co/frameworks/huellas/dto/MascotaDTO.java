package co.frameworks.huellas.dto;

import co.frameworks.huellas.model.EstadoMascota;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MascotaDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotNull(message = "La especie es obligatoria")
    private Long especieId;

    private String especieNombre;

    @Size(max = 100)
    private String raza;

    @Min(0)
    @Max(40)
    private Integer edad;

    @Size(max = 200)
    private String estadoSalud;

    @Size(max = 250)
    private String fotoUrl;

    @NotNull(message = "El estado es obligatorio")
    private EstadoMascota estado;

    @NotNull(message = "El refugio es obligatorio")
    private Long refugioId;

    private String refugioNombre;

    @Size(max = 500)
    private String descripcion;
}
