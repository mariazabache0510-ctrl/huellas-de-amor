package co.frameworks.huellas.repository;

import co.frameworks.huellas.model.Adoptante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdoptanteRepository extends JpaRepository<Adoptante, Long> {

    @Query("""
            SELECT a FROM Adoptante a
            JOIN FETCH a.usuario u
            WHERE u.username = :username
            """)
    Optional<Adoptante> findByUsername(@Param("username") String username);

    Optional<Adoptante> findByUsuarioId(Long usuarioId);

    @Query("""
            SELECT a FROM Adoptante a
            JOIN FETCH a.usuario
            ORDER BY a.usuario.nombre ASC
            """)
    java.util.List<Adoptante> findAllConUsuario();
}
