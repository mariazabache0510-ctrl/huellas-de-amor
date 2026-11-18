package co.frameworks.huellas.mapper;

import co.frameworks.huellas.dto.MascotaDTO;
import co.frameworks.huellas.model.Especie;
import co.frameworks.huellas.model.Mascota;
import co.frameworks.huellas.model.Refugio;
import org.springframework.stereotype.Component;

@Component
public class MascotaMapper {

    public MascotaDTO toDTO(Mascota entity) {
        if (entity == null) {
            return null;
        }
        return MascotaDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .especieId(entity.getEspecie() != null ? entity.getEspecie().getId() : null)
                .especieNombre(entity.getEspecie() != null ? entity.getEspecie().getNombre() : null)
                .raza(entity.getRaza())
                .edad(entity.getEdad())
                .estadoSalud(entity.getEstadoSalud())
                .fotoUrl(entity.getFotoUrl())
                .estado(entity.getEstado())
                .refugioId(entity.getRefugio() != null ? entity.getRefugio().getId() : null)
                .refugioNombre(entity.getRefugio() != null ? entity.getRefugio().getNombre() : null)
                .descripcion(entity.getDescripcion())
                .build();
    }

    public Mascota toEntity(MascotaDTO dto, Especie especie, Refugio refugio) {
        return Mascota.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .especie(especie)
                .raza(dto.getRaza())
                .edad(dto.getEdad())
                .estadoSalud(dto.getEstadoSalud())
                .fotoUrl(dto.getFotoUrl())
                .estado(dto.getEstado())
                .refugio(refugio)
                .descripcion(dto.getDescripcion())
                .build();
    }

    public void updateEntity(Mascota entity, MascotaDTO dto, Especie especie, Refugio refugio) {
        entity.setNombre(dto.getNombre());
        entity.setEspecie(especie);
        entity.setRaza(dto.getRaza());
        entity.setEdad(dto.getEdad());
        entity.setEstadoSalud(dto.getEstadoSalud());
        entity.setFotoUrl(dto.getFotoUrl());
        entity.setEstado(dto.getEstado());
        entity.setRefugio(refugio);
        entity.setDescripcion(dto.getDescripcion());
    }
}
