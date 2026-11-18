package co.frameworks.huellas.config;

import co.frameworks.huellas.model.*;
import co.frameworks.huellas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    CommandLineRunner initDatos(UsuarioRepository usuarioRepository,
                                RefugioRepository refugioRepository,
                                EspecieRepository especieRepository,
                                MascotaRepository mascotaRepository,
                                AdoptanteRepository adoptanteRepository,
                                VoluntarioRepository voluntarioRepository,
                                PasswordEncoder encoder) {
        return args -> {
            if (usuarioRepository.count() > 0) {
                return;
            }

            Usuario admin = usuarioRepository.save(Usuario.builder()
                    .nombre("Administrador Huellas")
                    .username("admin")
                    .correo("admin@huellasdeamor.com")
                    .password(encoder.encode("admin123"))
                    .rol(Rol.ADMIN)
                    .telefono("3100000001")
                    .direccion("Villavicencio")
                    .activo(true)
                    .build());

            Refugio refugio1 = refugioRepository.save(Refugio.builder()
                    .nombre("Refugio Orinoquia")
                    .direccion("Calle 35 # 28-10, Villavicencio")
                    .contacto("Lina Quiroga")
                    .correo("orinoquia@huellasdeamor.com")
                    .telefono("3201112233")
                    .descripcion("Casa de acogida enfocada en perros y gatos rescatados.")
                    .build());

            Refugio refugio2 = refugioRepository.save(Refugio.builder()
                    .nombre("Huellas del Meta")
                    .direccion("Carrera 40 # 15-22, Villavicencio")
                    .contacto("Andrés Rodríguez")
                    .correo("meta@huellasdeamor.com")
                    .telefono("3204445566")
                    .descripcion("Fundación comunitaria para adopción responsable.")
                    .build());

            Especie perro = especieRepository.save(Especie.builder()
                    .nombre("Perro")
                    .descripcion("Caninos en busca de hogar")
                    .build());
            Especie gato = especieRepository.save(Especie.builder()
                    .nombre("Gato")
                    .descripcion("Felinos listos para adoptar")
                    .build());

            Usuario volUser = usuarioRepository.save(Usuario.builder()
                    .nombre("Mariana Voluntaria")
                    .username("voluntario")
                    .correo("voluntario@huellasdeamor.com")
                    .password(encoder.encode("vol123"))
                    .rol(Rol.VOLUNTARIO)
                    .telefono("3112223344")
                    .direccion("Villavicencio")
                    .activo(true)
                    .build());

            voluntarioRepository.save(Voluntario.builder()
                    .usuario(volUser)
                    .refugio(refugio1)
                    .especialidad("Acompañamiento de adopciones")
                    .build());

            Usuario adopUser = usuarioRepository.save(Usuario.builder()
                    .nombre("Sofía Adoptante")
                    .username("adoptante")
                    .correo("adoptante@huellasdeamor.com")
                    .password(encoder.encode("ado123"))
                    .rol(Rol.ADOPTANTE)
                    .telefono("3005556677")
                    .direccion("Barrio La Esperanza")
                    .activo(true)
                    .build());

            adoptanteRepository.save(Adoptante.builder()
                    .usuario(adopUser)
                    .motivacion("Busco un compañero leal para mi familia")
                    .tieneExperiencia(true)
                    .vivienda("Casa con patio")
                    .build());

            mascotaRepository.save(Mascota.builder()
                    .nombre("Max")
                    .especie(perro)
                    .raza("Labrador")
                    .edad(3)
                    .estadoSalud("Vacunado y desparasitado")
                    .fotoUrl("https://images.unsplash.com/photo-1552053831-71594a27632d?auto=format&fit=crop&w=800&q=80")
                    .estado(EstadoMascota.DISPONIBLE)
                    .refugio(refugio1)
                    .descripcion("Juguetón, sociable y perfecto para familias activas.")
                    .build());

            mascotaRepository.save(Mascota.builder()
                    .nombre("Luna")
                    .especie(gato)
                    .raza("Siamés")
                    .edad(2)
                    .estadoSalud("Excelente")
                    .fotoUrl("https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?auto=format&fit=crop&w=800&q=80")
                    .estado(EstadoMascota.DISPONIBLE)
                    .refugio(refugio2)
                    .descripcion("Cariñosa, tranquila y ideal para apartamento.")
                    .build());

            mascotaRepository.save(Mascota.builder()
                    .nombre("Toby")
                    .especie(perro)
                    .raza("Mestizo")
                    .edad(1)
                    .estadoSalud("En recuperación leve, listo para adoptar")
                    .fotoUrl("https://images.unsplash.com/photo-1587300003388-59208cc962cb?auto=format&fit=crop&w=800&q=80")
                    .estado(EstadoMascota.DISPONIBLE)
                    .refugio(refugio1)
                    .descripcion("Cachorro energético que busca una familia paciente.")
                    .build());

            mascotaRepository.save(Mascota.builder()
                    .nombre("Mía")
                    .especie(gato)
                    .raza("Común europeo")
                    .edad(4)
                    .estadoSalud("Esterilizada")
                    .fotoUrl("https://images.unsplash.com/photo-1574158622682-e40e69881006?auto=format&fit=crop&w=800&q=80")
                    .estado(EstadoMascota.DISPONIBLE)
                    .refugio(refugio2)
                    .descripcion("Independiente y muy limpia, ama las siestas soleadas.")
                    .build());

            // avoid unused warning in some IDEs
            admin.getId();
        };
    }
}
