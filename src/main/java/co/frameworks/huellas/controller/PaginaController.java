package co.frameworks.huellas.controller;

import co.frameworks.huellas.service.MascotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PaginaController {

    private final MascotaService mascotaService;

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("mascotasDestacadas", mascotaService.listarDisponibles().stream().limit(6).toList());
        model.addAttribute("totalDisponibles", mascotaService.contarDisponibles());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado";
    }

    @GetMapping("/redirigir")
    public String redirigir(Authentication auth) {
        var roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        if (roles.contains("ROLE_ADMIN")) {
            return "redirect:/admin";
        }
        if (roles.contains("ROLE_VOLUNTARIO")) {
            return "redirect:/voluntario";
        }
        return "redirect:/adoptante";
    }
}
