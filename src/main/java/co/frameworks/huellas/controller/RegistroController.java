package co.frameworks.huellas.controller;

import co.frameworks.huellas.dto.RegistroAdoptanteDTO;
import co.frameworks.huellas.exception.NegocioException;
import co.frameworks.huellas.service.RegistroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public String formulario(Model model) {
        model.addAttribute("registro", new RegistroAdoptanteDTO());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("registro") RegistroAdoptanteDTO dto,
                            BindingResult result,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (result.hasErrors()) {
            return "registro";
        }
        try {
            registroService.registrarAdoptante(dto);
            redirectAttributes.addFlashAttribute("exito", "Cuenta creada. Ya puedes iniciar sesión.");
            return "redirect:/login";
        } catch (NegocioException ex) {
            model.addAttribute("error", ex.getMessage());
            return "registro";
        }
    }
}
