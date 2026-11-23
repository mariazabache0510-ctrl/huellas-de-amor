package co.frameworks.huellas.repository;

import co.frameworks.huellas.model.Adopcion;
import co.frameworks.huellas.model.EstadoAdopcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdopcionRepository extends JpaRepository<Adopcion, Long> {

    @Query("""
            SELECT a FROM Adopcion a
            JOIN FETCH a.mascota m
            JOIN FETCH m.especie
            JOIN FETCH m.refugio
            JOIN FETCH a.adoptante
            LEFT JOIN FETCH a.voluntario
            WHERE a.id = :id
            """)
    Optional<Adopcion> findDetalleById(@Param("id") Long id);

    @Query("""
            SELECT a FROM Adopcion a
            JOIN FETCH a.mascota m
            JOIN FETCH a.adoptante
            LEFT JOIN FETCH a.voluntario
            ORDER BY a.fechaSolicitud DESC
            """)
    List<Adopcion> findAllConDetalle();

    @Query("""
            SELECT a FROM Adopcion a
            JOIN FETCH a.mascota m
            JOIN FETCH a.adoptante u
            WHERE u.username = :username
            ORDER BY a.fechaSolicitud DESC
            """)
    List<Adopcion> findHistorialByAdoptanteUsername(@Param("username") String username);

    List<Adopcion> findByEstado(EstadoAdopcion estado);

    boolean existsByMascotaIdAndEstado(Long mascotaId, EstadoAdopcion estado);

    void deleteByMascotaId(Long mascotaId);
}
