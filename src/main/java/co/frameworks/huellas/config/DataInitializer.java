package co.frameworks.huellas.config;

import co.frameworks.huellas.model.*;
import co.frameworks.huellas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private static final Map<String, String> FOTOS_CORRECTAS = Map.ofEntries(
            Map.entry("max", "https://images.unsplash.com/photo-1552053831-71594a27632d?auto=format&fit=crop&w=800&q=80"),
            Map.entry("luna", "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?auto=format&fit=crop&w=800&q=80"),
            Map.entry("toby", "https://images.unsplash.com/photo-1587300003388-59208cc962cb?auto=format&fit=crop&w=800&q=80"),
            Map.entry("mía", "https://images.unsplash.com/photo-1574158622682-e40e69881006?auto=format&fit=crop&w=800&q=80"),
            Map.entry("mia", "https://images.unsplash.com/photo-1574158622682-e40e69881006?auto=format&fit=crop&w=800&q=80"),
            Map.entry("rocky", "https://images.unsplash.com/photo-1568572933382-74d440642117?auto=format&fit=crop&w=800&q=80"),
            Map.entry("nala", "https://images.unsplash.com/photo-1573865526739-10659fec78a5?auto=format&fit=crop&w=800&q=80"),
            Map.entry("bruno", "https://images.unsplash.com/photo-1530281700549-e82e7bf110d6?auto=format&fit=crop&w=800&q=80"),
            Map.entry("coco", "https://images.unsplash.com/photo-1596854407944-bf87f6fdd49e?auto=format&fit=crop&w=800&q=80"),
            Map.entry("kira", "https://images.unsplash.com/photo-1558788353-f76d92427f16?auto=format&fit=crop&w=800&q=80"),
            Map.entry("simba", "https://images.unsplash.com/photo-1574158622682-e40e69881006?auto=format&fit=crop&w=800&q=80"),
            Map.entry("daisy", "https://images.unsplash.com/photo-1537151625747-768eb6cf92b2?auto=format&fit=crop&w=800&q=80"),
            Map.entry("oliver", "https://images.unsplash.com/photo-1592194996308-7b43878e84a6?auto=format&fit=crop&w=800&q=80"),
            Map.entry("luna azul", "https://images.unsplash.com/photo-1495366691023-cc4eadcc2d7e?auto=format&fit=crop&w=800&q=80"),
            Map.entry("thor", "https://images.unsplash.com/photo-1548199973-03cce0bbc87b?auto=format&fit=crop&w=800&q=80"),
            Map.entry("misha", "https://images.unsplash.com/photo-1526336024174-e58f5cdd8e13?auto=format&fit=crop&w=800&q=80"),
            Map.entry("canela", "https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?auto=format&fit=crop&w=800&q=80"),
            Map.entry("felix", "https://images.unsplash.com/photo-1511044567668-c1e7da7e6af5?auto=format&fit=crop&w=800&q=80")
    );

    @Bean
    CommandLineRunner initDatos(UsuarioRepository usuarioRepository,
                                RefugioRepository refugioRepository,
                                EspecieRepository especieRepository,
                                MascotaRepository mascotaRepository,
                                AdoptanteRepository adoptanteRepository,
                                VoluntarioRepository voluntarioRepository,
                                PasswordEncoder encoder) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                seedBase(usuarioRepository, refugioRepository, especieRepository, mascotaRepository,
                        adoptanteRepository, voluntarioRepository, encoder);
            }
            if (mascotaRepository.count() < 16) {
                seedMascotasExtra(especieRepository, refugioRepository, mascotaRepository);
            }
            corregirFotos(mascotaRepository);
        };
    }

    private void corregirFotos(MascotaRepository mascotaRepository) {
        for (Mascota m : mascotaRepository.findAll()) {
            String key = m.getNombre() == null ? "" : m.getNombre().trim().toLowerCase();
            String correcta = FOTOS_CORRECTAS.get(key);
            if (correcta != null && (m.getFotoUrl() == null || !correcta.equals(m.getFotoUrl()))) {
                // Siempre alinear Coco/Kira y cualquier URL rota conocida
                boolean sospechosa = m.getFotoUrl() == null
                        || m.getFotoUrl().contains("photo-1495366691023")
                        || m.getFotoUrl().contains("photo-1633722715463")
                        || m.getFotoUrl().contains("photo-1505628346881")
                        || !m.getFotoUrl().startsWith("https://images.unsplash.com/");
                if (sospechosa || "coco".equals(key) || "kira".equals(key) || "bruno".equals(key)) {
                    m.setFotoUrl(correcta);
                    mascotaRepository.save(m);
                }
            }
        }
    }

    private void seedBase(UsuarioRepository usuarioRepository,
                          RefugioRepository refugioRepository,
                          EspecieRepository especieRepository,
                          MascotaRepository mascotaRepository,
                          AdoptanteRepository adoptanteRepository,
                          VoluntarioRepository voluntarioRepository,
                          PasswordEncoder encoder) {
        usuarioRepository.save(Usuario.builder()
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

        crearMascota(mascotaRepository, "Max", perro, "Labrador", 3,
                "Vacunado y desparasitado", FOTOS_CORRECTAS.get("max"),
                refugio1, "Juguetón, sociable y perfecto para familias activas.");
        crearMascota(mascotaRepository, "Luna", gato, "Siamés", 2,
                "Excelente", FOTOS_CORRECTAS.get("luna"),
                refugio2, "Cariñosa, tranquila y ideal para apartamento.");
        crearMascota(mascotaRepository, "Toby", perro, "Mestizo", 1,
                "En recuperación leve, listo para adoptar", FOTOS_CORRECTAS.get("toby"),
                refugio1, "Cachorro energético que busca una familia paciente.");
        crearMascota(mascotaRepository, "Mía", gato, "Común europeo", 4,
                "Esterilizada", FOTOS_CORRECTAS.get("mía"),
                refugio2, "Independiente y muy limpia, ama las siestas soleadas.");
    }

    private void seedMascotasExtra(EspecieRepository especieRepository,
                                   RefugioRepository refugioRepository,
                                   MascotaRepository mascotaRepository) {
        Especie perro = especieRepository.findByNombreIgnoreCase("Perro").orElse(null);
        Especie gato = especieRepository.findByNombreIgnoreCase("Gato").orElse(null);
        var refugios = refugioRepository.findAllOrdenados();
        if (perro == null || gato == null || refugios.isEmpty()) {
            return;
        }
        Refugio r1 = refugios.get(0);
        Refugio r2 = refugios.size() > 1 ? refugios.get(1) : r1;
        var existentes = mascotaRepository.findAll().stream()
                .map(m -> m.getNombre().toLowerCase())
                .collect(java.util.stream.Collectors.toSet());

        crearSiNoExiste(mascotaRepository, existentes, "Rocky", perro, "Pastor alemán", 5,
                "Vacunado", FOTOS_CORRECTAS.get("rocky"),
                r1, "Leal, protector y excelente compañero de caminatas.");
        crearSiNoExiste(mascotaRepository, existentes, "Nala", gato, "Persa", 3,
                "Excelente", FOTOS_CORRECTAS.get("nala"),
                r2, "Suave, silenciosa y muy afectuosa en casa.");
        crearSiNoExiste(mascotaRepository, existentes, "Bruno", perro, "Beagle", 2,
                "Desparasitado", FOTOS_CORRECTAS.get("bruno"),
                r1, "Curioso y alegre, ideal para niños.");
        crearSiNoExiste(mascotaRepository, existentes, "Coco", gato, "Maine Coon", 1,
                "Vacunado", FOTOS_CORRECTAS.get("coco"),
                r2, "Juguetón y sociable desde cachorro.");
        crearSiNoExiste(mascotaRepository, existentes, "Kira", perro, "Golden Retriever", 4,
                "Esterilizada", FOTOS_CORRECTAS.get("kira"),
                r1, "Dulce, paciente y perfecta para familia.");
        crearSiNoExiste(mascotaRepository, existentes, "Simba", gato, "Bengalí", 2,
                "Excelente", FOTOS_CORRECTAS.get("simba"),
                r2, "Activo, inteligente y con mucho carácter.");
        crearSiNoExiste(mascotaRepository, existentes, "Daisy", perro, "Poodle", 6,
                "Control veterinario al día", FOTOS_CORRECTAS.get("daisy"),
                r1, "Tranquila, limpia y muy compañera.");
        crearSiNoExiste(mascotaRepository, existentes, "Oliver", gato, "British Shorthair", 5,
                "Esterilizado", FOTOS_CORRECTAS.get("oliver"),
                r2, "Calmado, elegante y perfecto para espacios pequeños.");
        crearSiNoExiste(mascotaRepository, existentes, "Thor", perro, "Boxer", 3,
                "Vacunado", FOTOS_CORRECTAS.get("thor"),
                r1, "Fuerte, cariñoso y excelente guardián amigable.");
        crearSiNoExiste(mascotaRepository, existentes, "Misha", gato, "Angora", 2,
                "Excelente", FOTOS_CORRECTAS.get("misha"),
                r2, "Elegante, curiosa y muy apegada a su humano.");
        crearSiNoExiste(mascotaRepository, existentes, "Canela", perro, "Cocker Spaniel", 4,
                "Desparasitada", FOTOS_CORRECTAS.get("canela"),
                r1, "Dulce, sociable y feliz con paseos diarios.");
        crearSiNoExiste(mascotaRepository, existentes, "Felix", gato, "Común europeo", 3,
                "Esterilizado", FOTOS_CORRECTAS.get("felix"),
                r2, "Tranquilo, limpio y perfecto para primera adopción.");
    }

    private void crearSiNoExiste(MascotaRepository repo, java.util.Set<String> existentes,
                                 String nombre, Especie especie, String raza, int edad,
                                 String salud, String foto, Refugio refugio, String descripcion) {
        if (!existentes.contains(nombre.toLowerCase())) {
            crearMascota(repo, nombre, especie, raza, edad, salud, foto, refugio, descripcion);
            existentes.add(nombre.toLowerCase());
        }
    }

    private void crearMascota(MascotaRepository repo, String nombre, Especie especie, String raza, int edad,
                              String salud, String foto, Refugio refugio, String descripcion) {
        repo.save(Mascota.builder()
                .nombre(nombre)
                .especie(especie)
                .raza(raza)
                .edad(edad)
                .estadoSalud(salud)
                .fotoUrl(foto)
                .estado(EstadoMascota.DISPONIBLE)
                .refugio(refugio)
                .descripcion(descripcion)
                .build());
    }
}
