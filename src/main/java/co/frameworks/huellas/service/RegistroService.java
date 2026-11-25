package co.frameworks.huellas.service;

import co.frameworks.huellas.dto.RegistroAdoptanteDTO;
import co.frameworks.huellas.exception.NegocioException;
import co.frameworks.huellas.model.Rol;
import co.frameworks.huellas.model.Usuario;
import co.frameworks.huellas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistroService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public void registrarAdoptante(RegistroAdoptanteDTO dto) {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new NegocioException("El usuario ya existe");
        }
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new NegocioException("El correo ya está registrado");
        }

        String username = dto.getUsername().trim();
        String correo = dto.getCorreo().trim().toLowerCase();
        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre().trim())
                .username(username)
                .correo(correo)
                .password(passwordEncoder.encode(dto.getPassword()))
                .rol(Rol.ADOPTANTE)
                .telefono(dto.getTelefono() != null ? dto.getTelefono().trim() : null)
                .direccion(dto.getDireccion().trim())
                .motivacion(dto.getMotivacion().trim())
                .tieneExperiencia(dto.isTieneExperiencia())
                .vivienda(dto.getVivienda().trim())
                .activo(true)
                .build();
        usuarioRepository.save(usuario);
    }
}
