package co.frameworks.huellas.controller;

import co.frameworks.huellas.service.MascotaService;
import co.frameworks.huellas.service.RefugioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PaginaController {

    private final MascotaService mascotaService;
    private final RefugioService refugioService;

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("mascotasDestacadas", mascotaService.listarDisponibles().stream().limit(8).toList());
        model.addAttribute("totalDisponibles", mascotaService.contarDisponibles());
        model.addAttribute("refugios", refugioService.listar());
        return "index";
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (estaAutenticado(authentication)) {
            return "redirect:/redirigir";
        }
        return "login";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado";
    }

    @GetMapping("/terminos")
    public String terminos() {
        return "legal/terminos";
    }

    @GetMapping("/privacidad")
    public String privacidad() {
        return "legal/privacidad";
    }

    @GetMapping("/como-adoptar")
    public String comoAdoptar() {
        return "como-adoptar";
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

    private boolean estaAutenticado(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
