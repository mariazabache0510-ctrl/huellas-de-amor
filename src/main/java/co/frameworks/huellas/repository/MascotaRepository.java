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
            SELECT DISTINCT m FROM Mascota m
            JOIN FETCH m.especie e
            JOIN FETCH m.refugio r
            WHERE m.estado = :estado
            ORDER BY m.nombre ASC
            """)
    List<Mascota> findDisponiblesConDetalle(@Param("estado") EstadoMascota estado);

    @Query("""
            SELECT DISTINCT m FROM Mascota m
            JOIN FETCH m.especie e
            JOIN FETCH m.refugio r
            ORDER BY m.nombre ASC
            """)
    List<Mascota> findAllConDetalle();

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

    @Query("""
            SELECT m.estado, COUNT(m)
            FROM Mascota m
            GROUP BY m.estado
            ORDER BY m.estado
            """)
    List<Object[]> contarPorEstado();
}
