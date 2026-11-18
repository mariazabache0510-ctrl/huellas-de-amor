package co.frameworks.huellas.controller;

import co.frameworks.huellas.model.EstadoAdopcion;
import co.frameworks.huellas.repository.MascotaRepository;
import co.frameworks.huellas.service.AdopcionService;
import co.frameworks.huellas.service.MascotaService;
import co.frameworks.huellas.service.SeguimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PanelController {

    private final MascotaService mascotaService;
    private final AdopcionService adopcionService;
    private final SeguimientoService seguimientoService;
    private final MascotaRepository mascotaRepository;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("totalMascotas", mascotaRepository.count());
        model.addAttribute("disponibles", mascotaService.contarDisponibles());
        model.addAttribute("porEspecie", mascotaRepository.contarDisponiblesPorEspecie());
        model.addAttribute("porRefugio", mascotaRepository.contarDisponiblesPorRefugio());
        model.addAttribute("solicitudes", adopcionService.listar());
        return "admin/panel";
    }

    @GetMapping("/voluntario")
    public String voluntario(Model model) {
        model.addAttribute("pendientes", adopcionService.listar().stream()
                .filter(a -> a.getEstado() == EstadoAdopcion.PENDIENTE)
                .toList());
        model.addAttribute("mascotas", mascotaService.buscar(null, null, null));
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
