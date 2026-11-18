package co.frameworks.huellas.mapper;

import co.frameworks.huellas.dto.RefugioDTO;
import co.frameworks.huellas.model.Refugio;
import org.springframework.stereotype.Component;

@Component
public class RefugioMapper {

    public RefugioDTO toDTO(Refugio entity) {
        if (entity == null) {
            return null;
        }
        return RefugioDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .direccion(entity.getDireccion())
                .contacto(entity.getContacto())
                .correo(entity.getCorreo())
                .telefono(entity.getTelefono())
                .descripcion(entity.getDescripcion())
                .build();
    }

    public Refugio toEntity(RefugioDTO dto) {
        return Refugio.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .direccion(dto.getDireccion())
                .contacto(dto.getContacto())
                .correo(dto.getCorreo())
                .telefono(dto.getTelefono())
                .descripcion(dto.getDescripcion())
                .build();
    }

    public void updateEntity(Refugio entity, RefugioDTO dto) {
        entity.setNombre(dto.getNombre());
        entity.setDireccion(dto.getDireccion());
        entity.setContacto(dto.getContacto());
        entity.setCorreo(dto.getCorreo());
        entity.setTelefono(dto.getTelefono());
        entity.setDescripcion(dto.getDescripcion());
    }
}
