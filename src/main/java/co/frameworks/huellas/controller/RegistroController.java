package co.frameworks.huellas.controller;

import co.frameworks.huellas.dto.RegistroAdoptanteDTO;
import co.frameworks.huellas.exception.NegocioException;
import co.frameworks.huellas.service.RegistroService;
import co.frameworks.huellas.validation.RegistroValidators;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class RegistroController {

    private final RegistroService registroService;

    @GetMapping("/registro")
    public String formulario(Model model, Authentication authentication) {
        if (estaAutenticado(authentication)) {
            return "redirect:/redirigir";
        }
        model.addAttribute("registro", new RegistroAdoptanteDTO());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("registro") RegistroAdoptanteDTO dto,
                            BindingResult result,
                            RedirectAttributes redirectAttributes,
                            Model model,
                            Authentication authentication) {
        if (estaAutenticado(authentication)) {
            return "redirect:/redirigir";
        }
        RegistroValidators.validar(dto, result);
        if (!dto.isAceptaTerminos()) {
            result.rejectValue("aceptaTerminos", "required", "Debes aceptar términos y privacidad");
        }
        if (result.hasErrors()) {
            return "registro";
        }
        try {
            registroService.registrarAdoptante(dto);
            if (dto.isTieneExperiencia()) {
                redirectAttributes.addFlashAttribute("exito",
                        "Cuenta creada. Ya puedes iniciar sesión y solicitar adopciones.");
            } else {
                redirectAttributes.addFlashAttribute("exito",
                        "Cuenta creada. Te dejamos una guía breve para primeros adoptantes: revisa Cómo adoptar.");
                redirectAttributes.addFlashAttribute("mostrarGuia", true);
            }
            return "redirect:/login";
        } catch (NegocioException ex) {
            model.addAttribute("error", ex.getMessage());
            return "registro";
        }
    }

    private boolean estaAutenticado(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
