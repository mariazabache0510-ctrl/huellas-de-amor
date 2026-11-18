package co.frameworks.huellas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefugioDTO {

    private Long id;

    @NotBlank
    @Size(max = 150)
    private String nombre;

    @NotBlank
    @Size(max = 150)
    private String direccion;

    @Size(max = 150)
    private String contacto;

    @Email
    @Size(max = 150)
    private String correo;

    @Size(max = 100)
    private String telefono;

    @Size(max = 300)
    private String descripcion;
}
