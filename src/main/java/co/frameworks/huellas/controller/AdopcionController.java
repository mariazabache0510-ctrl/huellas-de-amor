package co.frameworks.huellas.controller;

import co.frameworks.huellas.exception.NegocioException;
import co.frameworks.huellas.model.EstadoAdopcion;
import co.frameworks.huellas.service.AdopcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adopciones")
public class AdopcionController {

    private final AdopcionService adopcionService;

    @PostMapping("/solicitar/{mascotaId}")
    public String solicitar(@PathVariable Long mascotaId,
                            @RequestParam(required = false) String observaciones,
                            Authentication auth,
                            RedirectAttributes ra) {
        try {
            adopcionService.solicitar(mascotaId, auth.getName(), observaciones);
            ra.addFlashAttribute("exito", "Solicitud enviada correctamente");
            return "redirect:/adoptante";
        } catch (NegocioException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/mascotas/detalle/" + mascotaId;
        }
    }

    @GetMapping("/gestionar")
    public String gestionar(Model model) {
        model.addAttribute("adopciones", adopcionService.listar());
        return "adopciones/gestionar";
    }

    @PostMapping("/gestionar/{id}/aprobar")
    public String aprobar(@PathVariable Long id,
                          @RequestParam(required = false) String observaciones,
                          Authentication auth,
                          RedirectAttributes ra) {
        try {
            adopcionService.resolver(id, EstadoAdopcion.APROBADA, auth.getName(), observaciones);
            ra.addFlashAttribute("exito", "Adopción aprobada");
        } catch (NegocioException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/adopciones/gestionar";
    }

    @PostMapping("/gestionar/{id}/rechazar")
    public String rechazar(@PathVariable Long id,
                           @RequestParam(required = false) String observaciones,
                           Authentication auth,
                           RedirectAttributes ra) {
        try {
            adopcionService.resolver(id, EstadoAdopcion.RECHAZADA, auth.getName(), observaciones);
            ra.addFlashAttribute("exito", "Solicitud rechazada");
        } catch (NegocioException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/adopciones/gestionar";
    }
}
