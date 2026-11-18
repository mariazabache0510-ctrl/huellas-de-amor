package co.frameworks.huellas.repository;

import co.frameworks.huellas.model.Refugio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RefugioRepository extends JpaRepository<Refugio, Long> {

    Optional<Refugio> findByNombreIgnoreCase(String nombre);

    @Query("SELECT r FROM Refugio r ORDER BY r.nombre ASC")
    List<Refugio> findAllOrdenados();
}
