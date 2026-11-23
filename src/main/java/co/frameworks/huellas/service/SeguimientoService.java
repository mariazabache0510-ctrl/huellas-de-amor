package co.frameworks.huellas.service;

import co.frameworks.huellas.dto.SeguimientoDTO;
import co.frameworks.huellas.exception.NegocioException;
import co.frameworks.huellas.exception.RecursoNoEncontradoException;
import co.frameworks.huellas.model.Mascota;
import co.frameworks.huellas.model.Rol;
import co.frameworks.huellas.model.SeguimientoPostAdopcion;
import co.frameworks.huellas.model.Usuario;
import co.frameworks.huellas.repository.MascotaRepository;
import co.frameworks.huellas.repository.SeguimientoRepository;
import co.frameworks.huellas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeguimientoService {

    private final SeguimientoRepository seguimientoRepository;
    private final MascotaRepository mascotaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<SeguimientoDTO> listar() {
        return seguimientoRepository.findAllConDetalle().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<SeguimientoDTO> listarPorAdoptante(String username) {
        return seguimientoRepository.findByAdoptanteUsername(username).stream().map(this::toDTO).toList();
    }

    public SeguimientoDTO crear(SeguimientoDTO dto) {
        Mascota mascota = mascotaRepository.findById(dto.getMascotaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Mascota no encontrada"));
        Usuario adoptante = usuarioRepository.findById(dto.getAdoptanteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario adoptante no encontrado"));
        if (adoptante.getRol() != Rol.ADOPTANTE) {
            throw new NegocioException("El usuario seleccionado no tiene rol ADOPTANTE");
        }

        SeguimientoPostAdopcion entity = SeguimientoPostAdopcion.builder()
                .mascota(mascota)
                .adoptante(adoptante)
                .fechaVisita(dto.getFechaVisita())
                .comentarios(dto.getComentarios())
                .build();
        return toDTO(seguimientoRepository.save(entity));
    }

    private SeguimientoDTO toDTO(SeguimientoPostAdopcion s) {
        return SeguimientoDTO.builder()
                .id(s.getId())
                .mascotaId(s.getMascota().getId())
                .mascotaNombre(s.getMascota().getNombre())
                .adoptanteId(s.getAdoptante().getId())
                .adoptanteNombre(s.getAdoptante().getNombre())
                .fechaVisita(s.getFechaVisita())
                .comentarios(s.getComentarios())
                .build();
    }
}
