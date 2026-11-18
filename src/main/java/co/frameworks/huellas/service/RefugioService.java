package co.frameworks.huellas.service;

import co.frameworks.huellas.dto.RefugioDTO;
import co.frameworks.huellas.exception.RecursoNoEncontradoException;
import co.frameworks.huellas.mapper.RefugioMapper;
import co.frameworks.huellas.model.Refugio;
import co.frameworks.huellas.repository.RefugioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RefugioService {

    private final RefugioRepository refugioRepository;
    private final RefugioMapper refugioMapper;

    @Transactional(readOnly = true)
    public List<RefugioDTO> listar() {
        return refugioRepository.findAllOrdenados().stream().map(refugioMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public RefugioDTO obtener(Long id) {
        return refugioMapper.toDTO(buscarEntidad(id));
    }

    @Transactional(readOnly = true)
    public Refugio buscarEntidad(Long id) {
        return refugioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Refugio no encontrado: " + id));
    }

    public RefugioDTO crear(RefugioDTO dto) {
        return refugioMapper.toDTO(refugioRepository.save(refugioMapper.toEntity(dto)));
    }

    public RefugioDTO actualizar(Long id, RefugioDTO dto) {
        Refugio entity = buscarEntidad(id);
        refugioMapper.updateEntity(entity, dto);
        return refugioMapper.toDTO(entity);
    }

    public void eliminar(Long id) {
        if (!refugioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Refugio no encontrado: " + id);
        }
        refugioRepository.deleteById(id);
    }
}
