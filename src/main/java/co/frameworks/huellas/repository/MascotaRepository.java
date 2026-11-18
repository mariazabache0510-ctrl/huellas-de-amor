package co.frameworks.huellas.repository;

import co.frameworks.huellas.model.EstadoMascota;
import co.frameworks.huellas.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    List<Mascota> findByEstado(EstadoMascota estado);

    @Query("""
            SELECT m FROM Mascota m
            JOIN FETCH m.especie e
            JOIN FETCH m.refugio r
            WHERE m.estado = :estado
            ORDER BY m.nombre ASC
            """)
    List<Mascota> findDisponiblesConDetalle(@Param("estado") EstadoMascota estado);

    @Query("""
            SELECT m FROM Mascota m
            JOIN FETCH m.especie e
            JOIN FETCH m.refugio r
            WHERE (:estado IS NULL OR m.estado = :estado)
              AND (:refugioId IS NULL OR r.id = :refugioId)
              AND (:especieId IS NULL OR e.id = :especieId)
              AND (:raza IS NULL OR LOWER(m.raza) LIKE LOWER(CONCAT('%', :raza, '%')))
              AND (:edadMin IS NULL OR m.edad >= :edadMin)
              AND (:edadMax IS NULL OR m.edad <= :edadMax)
            ORDER BY m.nombre ASC
            """)
    List<Mascota> buscarConFiltros(@Param("estado") EstadoMascota estado,
                                   @Param("refugioId") Long refugioId,
                                   @Param("especieId") Long especieId,
                                   @Param("raza") String raza,
                                   @Param("edadMin") Integer edadMin,
                                   @Param("edadMax") Integer edadMax);

    @Query("""
            SELECT DISTINCT m.raza FROM Mascota m
            WHERE m.raza IS NOT NULL AND m.raza <> ''
            ORDER BY m.raza ASC
            """)
    List<String> findRazasDistintas();

    @Query("""
            SELECT COUNT(m) FROM Mascota m
            WHERE m.estado = co.frameworks.huellas.model.EstadoMascota.DISPONIBLE
            """)
    long contarDisponibles();

    @Query("""
            SELECT e.nombre, COUNT(m)
            FROM Mascota m JOIN m.especie e
            WHERE m.estado = co.frameworks.huellas.model.EstadoMascota.DISPONIBLE
            GROUP BY e.nombre
            ORDER BY e.nombre
            """)
    List<Object[]> contarDisponiblesPorEspecie();

    @Query("""
            SELECT r.nombre, COUNT(m)
            FROM Mascota m JOIN m.refugio r
            WHERE m.estado = co.frameworks.huellas.model.EstadoMascota.DISPONIBLE
            GROUP BY r.nombre
            ORDER BY r.nombre
            """)
    List<Object[]> contarDisponiblesPorRefugio();
}
