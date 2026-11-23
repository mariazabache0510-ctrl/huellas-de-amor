package co.frameworks.huellas.service;

import co.frameworks.huellas.dto.MascotaDTO;
import co.frameworks.huellas.exception.RecursoNoEncontradoException;
import co.frameworks.huellas.mapper.MascotaMapper;
import co.frameworks.huellas.model.EstadoMascota;
import co.frameworks.huellas.model.Especie;
import co.frameworks.huellas.model.Mascota;
import co.frameworks.huellas.model.Refugio;
import co.frameworks.huellas.repository.AdopcionRepository;
import co.frameworks.huellas.repository.EspecieRepository;
import co.frameworks.huellas.repository.MascotaRepository;
import co.frameworks.huellas.repository.RefugioRepository;
import co.frameworks.huellas.repository.SeguimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final EspecieRepository especieRepository;
    private final RefugioRepository refugioRepository;
    private final AdopcionRepository adopcionRepository;
    private final SeguimientoRepository seguimientoRepository;
    private final MascotaMapper mascotaMapper;

    @Transactional(readOnly = true)
    public List<MascotaDTO> listarDisponibles() {
        return mascotaRepository.findDisponiblesConDetalle(EstadoMascota.DISPONIBLE)
                .stream()
                .map(mascotaMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MascotaDTO> buscar(EstadoMascota estado, Long refugioId, Long especieId) {
        return buscar(estado, refugioId, especieId, null, null, null);
    }

    @Transactional(readOnly = true)
    public List<MascotaDTO> buscar(EstadoMascota estado,
                                   Long refugioId,
                                   Long especieId,
                                   String raza,
                                   Integer edadMin,
                                   Integer edadMax) {
        String razaFiltro = (raza == null || raza.isBlank()) ? null : raza.trim().toLowerCase(Locale.ROOT);
        List<Mascota> base = estado == EstadoMascota.DISPONIBLE
                ? mascotaRepository.findDisponiblesConDetalle(EstadoMascota.DISPONIBLE)
                : mascotaRepository.findAllConDetalle();

        return base.stream()
                .filter(m -> estado == null || m.getEstado() == estado)
                .filter(m -> refugioId == null || (m.getRefugio() != null && refugioId.equals(m.getRefugio().getId())))
                .filter(m -> especieId == null || (m.getEspecie() != null && especieId.equals(m.getEspecie().getId())))
                .filter(m -> razaFiltro == null || (m.getRaza() != null
                        && m.getRaza().toLowerCase(Locale.ROOT).contains(razaFiltro)))
                .filter(m -> edadMin == null || m.getEdad() >= edadMin)
                .filter(m -> edadMax == null || m.getEdad() <= edadMax)
                .map(mascotaMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> listarRazas() {
        return mascotaRepository.findRazasDistintas();
    }

    @Transactional(readOnly = true)
    public MascotaDTO obtener(Long id) {
        return mascotaMapper.toDTO(buscarEntidad(id));
    }

    @Transactional(readOnly = true)
    public Mascota buscarEntidad(Long id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Mascota no encontrada: " + id));
    }

    public MascotaDTO crear(MascotaDTO dto) {
        Especie especie = especieRepository.findById(dto.getEspecieId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Especie no encontrada"));
        Refugio refugio = refugioRepository.findById(dto.getRefugioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Refugio no encontrado"));
        Mascota guardada = mascotaRepository.save(mascotaMapper.toEntity(dto, especie, refugio));
        return mascotaMapper.toDTO(guardada);
    }

    public MascotaDTO actualizar(Long id, MascotaDTO dto) {
        Mascota entity = buscarEntidad(id);
        Especie especie = especieRepository.findById(dto.getEspecieId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Especie no encontrada"));
        Refugio refugio = refugioRepository.findById(dto.getRefugioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Refugio no encontrado"));
        mascotaMapper.updateEntity(entity, dto, especie, refugio);
        return mascotaMapper.toDTO(entity);
    }

    public void eliminar(Long id) {
        if (!mascotaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Mascota no encontrada: " + id);
        }
        seguimientoRepository.deleteByMascotaId(id);
        adopcionRepository.deleteByMascotaId(id);
        mascotaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long contarDisponibles() {
        return mascotaRepository.contarDisponibles();
    }
}
