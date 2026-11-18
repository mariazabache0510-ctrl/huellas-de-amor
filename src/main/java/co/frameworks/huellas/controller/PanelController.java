package co.frameworks.huellas.controller;

import co.frameworks.huellas.model.EstadoAdopcion;
import co.frameworks.huellas.repository.MascotaRepository;
import co.frameworks.huellas.repository.UsuarioRepository;
import co.frameworks.huellas.service.AdopcionService;
import co.frameworks.huellas.service.MascotaService;
import co.frameworks.huellas.service.RefugioService;
import co.frameworks.huellas.service.SeguimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PanelController {

    private final MascotaService mascotaService;
    private final AdopcionService adopcionService;
    private final SeguimientoService seguimientoService;
    private final RefugioService refugioService;
    private final MascotaRepository mascotaRepository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/admin")
    public String admin(@RequestParam(defaultValue = "resumen") String tab, Model model) {
        var solicitudes = adopcionService.listar();
        long pendientes = solicitudes.stream().filter(a -> a.getEstado() == EstadoAdopcion.PENDIENTE).count();
        long aprobadas = solicitudes.stream().filter(a -> a.getEstado() == EstadoAdopcion.APROBADA).count();
        long rechazadas = solicitudes.stream().filter(a -> a.getEstado() == EstadoAdopcion.RECHAZADA).count();
        List<Object[]> porEspecie = mascotaRepository.contarDisponiblesPorEspecie();
        List<Object[]> porRefugio = mascotaRepository.contarDisponiblesPorRefugio();

        model.addAttribute("tab", tab);
        model.addAttribute("totalMascotas", mascotaRepository.count());
        model.addAttribute("disponibles", mascotaService.contarDisponibles());
        model.addAttribute("totalUsuarios", usuarioRepository.count());
        model.addAttribute("totalRefugios", refugioService.listar().size());
        model.addAttribute("porEspecie", porEspecie);
        model.addAttribute("porRefugio", porRefugio);
        model.addAttribute("porEstado", mascotaRepository.contarPorEstado());
        model.addAttribute("especieLabels", porEspecie.stream().map(r -> String.valueOf(r[0])).toList());
        model.addAttribute("especieValues", porEspecie.stream().map(r -> ((Number) r[1]).longValue()).toList());
        model.addAttribute("refugioLabels", porRefugio.stream().map(r -> String.valueOf(r[0])).toList());
        model.addAttribute("refugioValues", porRefugio.stream().map(r -> ((Number) r[1]).longValue()).toList());
        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("pendientesCount", pendientes);
        model.addAttribute("aprobadasCount", aprobadas);
        model.addAttribute("rechazadasCount", rechazadas);
        model.addAttribute("mascotas", mascotaService.buscar(null, null, null));
        model.addAttribute("refugios", refugioService.listar());
        model.addAttribute("seguimientos", seguimientoService.listar());
        return "admin/panel";
    }

    @GetMapping("/voluntario")
    public String voluntario(@RequestParam(defaultValue = "solicitudes") String tab, Model model) {
        model.addAttribute("tab", tab);
        model.addAttribute("pendientes", adopcionService.listar().stream()
                .filter(a -> a.getEstado() == EstadoAdopcion.PENDIENTE)
                .toList());
        model.addAttribute("adopciones", adopcionService.listar());
        model.addAttribute("mascotas", mascotaService.buscar(null, null, null));
        model.addAttribute("seguimientos", seguimientoService.listar());
        return "voluntario/panel";
    }

    @GetMapping("/adoptante")
    public String adoptante(Authentication auth, Model model) {
        model.addAttribute("disponibles", mascotaService.listarDisponibles());
        model.addAttribute("historial", adopcionService.historialAdoptante(auth.getName()));
        model.addAttribute("seguimientos", seguimientoService.listarPorAdoptante(auth.getName()));
        return "adoptante/panel";
    }
}
