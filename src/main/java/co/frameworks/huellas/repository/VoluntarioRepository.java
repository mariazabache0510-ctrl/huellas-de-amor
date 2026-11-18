package co.frameworks.huellas.repository;

import co.frameworks.huellas.model.Voluntario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {

    @Query("""
            SELECT v FROM Voluntario v
            JOIN FETCH v.usuario u
            WHERE u.username = :username
            """)
    Optional<Voluntario> findByUsername(@Param("username") String username);

    @Query("""
            SELECT v FROM Voluntario v
            JOIN FETCH v.usuario
            LEFT JOIN FETCH v.refugio
            ORDER BY v.usuario.nombre ASC
            """)
    List<Voluntario> findAllConDetalle();
}
