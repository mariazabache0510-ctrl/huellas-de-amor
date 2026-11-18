package co.frameworks.huellas.service;

import co.frameworks.huellas.dto.AdopcionDTO;
import co.frameworks.huellas.exception.NegocioException;
import co.frameworks.huellas.exception.RecursoNoEncontradoException;
import co.frameworks.huellas.model.*;
import co.frameworks.huellas.repository.AdopcionRepository;
import co.frameworks.huellas.repository.AdoptanteRepository;
import co.frameworks.huellas.repository.MascotaRepository;
import co.frameworks.huellas.repository.VoluntarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdopcionService {

    private final AdopcionRepository adopcionRepository;
    private final MascotaRepository mascotaRepository;
    private final AdoptanteRepository adoptanteRepository;
    private final VoluntarioRepository voluntarioRepository;

    @Transactional(readOnly = true)
    public List<AdopcionDTO> listar() {
        return adopcionRepository.findAllConDetalle().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<AdopcionDTO> historialAdoptante(String username) {
        return adopcionRepository.findHistorialByAdoptanteUsername(username).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public AdopcionDTO obtener(Long id) {
        return toDTO(adopcionRepository.findDetalleById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Adopción no encontrada: " + id)));
    }

    public AdopcionDTO solicitar(Long mascotaId, String username, String observaciones) {
        Mascota mascota = mascotaRepository.findById(mascotaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Mascota no encontrada"));
        if (mascota.getEstado() != EstadoMascota.DISPONIBLE) {
            throw new NegocioException("La mascota no está disponible para adopción");
        }
        Adoptante adoptante = adoptanteRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Perfil de adoptante no encontrado"));

        if (adopcionRepository.existsByMascotaIdAndEstado(mascotaId, EstadoAdopcion.PENDIENTE)) {
            throw new NegocioException("Ya existe una solicitud pendiente para esta mascota");
        }

        mascota.setEstado(EstadoMascota.EN_PROCESO);
        Adopcion adopcion = Adopcion.builder()
                .mascota(mascota)
                .adoptante(adoptante)
                .fechaSolicitud(LocalDate.now())
                .estado(EstadoAdopcion.PENDIENTE)
                .observaciones(observaciones)
                .build();
        return toDTO(adopcionRepository.save(adopcion));
    }

    public AdopcionDTO resolver(Long id, EstadoAdopcion nuevoEstado, String usernameVoluntario, String observaciones) {
        Adopcion adopcion = adopcionRepository.findDetalleById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Adopción no encontrada"));

        if (adopcion.getEstado() != EstadoAdopcion.PENDIENTE) {
            throw new NegocioException("Solo se pueden resolver solicitudes pendientes");
        }

        Voluntario voluntario = voluntarioRepository.findByUsername(usernameVoluntario).orElse(null);
        adopcion.setVoluntario(voluntario);
        adopcion.setEstado(nuevoEstado);
        if (observaciones != null && !observaciones.isBlank()) {
            adopcion.setObservaciones(observaciones);
        }

        Mascota mascota = adopcion.getMascota();
        if (nuevoEstado == EstadoAdopcion.APROBADA) {
            mascota.setEstado(EstadoMascota.ADOPTADA);
        } else if (nuevoEstado == EstadoAdopcion.RECHAZADA) {
            mascota.setEstado(EstadoMascota.DISPONIBLE);
        }
        return toDTO(adopcion);
    }

    private AdopcionDTO toDTO(Adopcion a) {
        return AdopcionDTO.builder()
                .id(a.getId())
                .mascotaId(a.getMascota() != null ? a.getMascota().getId() : null)
                .mascotaNombre(a.getMascota() != null ? a.getMascota().getNombre() : null)
                .adoptanteId(a.getAdoptante() != null ? a.getAdoptante().getId() : null)
                .adoptanteNombre(a.getAdoptante() != null && a.getAdoptante().getUsuario() != null
                        ? a.getAdoptante().getUsuario().getNombre() : null)
                .voluntarioId(a.getVoluntario() != null ? a.getVoluntario().getId() : null)
                .voluntarioNombre(a.getVoluntario() != null && a.getVoluntario().getUsuario() != null
                        ? a.getVoluntario().getUsuario().getNombre() : null)
                .fechaSolicitud(a.getFechaSolicitud())
                .estado(a.getEstado())
                .observaciones(a.getObservaciones())
                .build();
    }
}
