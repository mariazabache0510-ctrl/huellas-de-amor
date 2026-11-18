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
public class RegistroAdoptanteDTO {

    @NotBlank
    @Size(max = 100)
    private String nombre;

    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Email
    @Size(max = 150)
    private String correo;

    @NotBlank
    @Size(min = 6, max = 60)
    private String password;

    @Size(max = 20)
    private String telefono;

    @Size(max = 150)
    private String direccion;

    @Size(max = 300)
    private String motivacion;

    private boolean tieneExperiencia;

    @Size(max = 100)
    private String vivienda;
}
