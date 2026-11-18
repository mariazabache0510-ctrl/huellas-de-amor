package co.frameworks.huellas.repository;

import co.frameworks.huellas.model.SeguimientoPostAdopcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeguimientoRepository extends JpaRepository<SeguimientoPostAdopcion, Long> {

    @Query("""
            SELECT s FROM SeguimientoPostAdopcion s
            JOIN FETCH s.mascota m
            JOIN FETCH s.adoptante a
            JOIN FETCH a.usuario
            ORDER BY s.fechaVisita DESC
            """)
    List<SeguimientoPostAdopcion> findAllConDetalle();

    @Query("""
            SELECT s FROM SeguimientoPostAdopcion s
            JOIN FETCH s.mascota m
            JOIN FETCH s.adoptante a
            JOIN FETCH a.usuario u
            WHERE u.username = :username
            ORDER BY s.fechaVisita DESC
            """)
    List<SeguimientoPostAdopcion> findByAdoptanteUsername(@Param("username") String username);
}
