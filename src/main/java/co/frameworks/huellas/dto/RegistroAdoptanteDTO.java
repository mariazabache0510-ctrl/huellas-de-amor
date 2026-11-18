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

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 4, max = 50, message = "El usuario debe tener entre 4 y 50 caracteres")
    private String username;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Correo no válido")
    @Size(max = 150)
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 60, message = "La contraseña debe tener entre 8 y 60 caracteres")
    private String password;

    @Size(max = 20)
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(min = 5, max = 150, message = "Indica una dirección válida")
    private String direccion;

    @NotBlank(message = "Cuéntanos tu motivación")
    @Size(min = 10, max = 300, message = "La motivación debe tener entre 10 y 300 caracteres")
    private String motivacion;

    private boolean tieneExperiencia;

    @NotBlank(message = "Indica el tipo de vivienda")
    @Size(max = 100)
    private String vivienda;

    private boolean aceptaTerminos;
}
