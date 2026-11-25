package co.frameworks.huellas.controller;

import co.frameworks.huellas.dto.RegistroAdoptanteDTO;
import co.frameworks.huellas.exception.NegocioException;
import co.frameworks.huellas.service.RegistroService;
import co.frameworks.huellas.service.UsuarioDetailsService;
import co.frameworks.huellas.validation.RegistroValidators;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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
    private final UsuarioDetailsService usuarioDetailsService;

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
                            Authentication authentication,
                            HttpServletRequest request) {
        if (estaAutenticado(authentication)) {
            return "redirect:/redirigir";
        }
        RegistroValidators.validar(dto, result);
        if (!dto.isAceptaTerminos()) {
            result.rejectValue("aceptaTerminos", "required", "Debes aceptar términos y privacidad");
        }
        if (result.hasErrors()) {
            model.addAttribute("error",
                    "No se pudo crear la cuenta. Revisa los campos en rojo e inténtalo de nuevo.");
            return "registro";
        }
        try {
            registroService.registrarAdoptante(dto);
            iniciarSesion(dto.getUsername().trim(), request);

            if (dto.isTieneExperiencia()) {
                redirectAttributes.addFlashAttribute("exito",
                        "¡Cuenta creada! Ya iniciaste sesión. Puedes explorar mascotas y solicitar adopción.");
            } else {
                redirectAttributes.addFlashAttribute("exito",
                        "¡Cuenta creada! Ya iniciaste sesión. Revisa la guía de Cómo adoptar antes de solicitar.");
                redirectAttributes.addFlashAttribute("mostrarGuia", true);
            }
            return "redirect:/adoptante";
        } catch (NegocioException ex) {
            model.addAttribute("error", ex.getMessage());
            return "registro";
        }
    }

    private void iniciarSesion(String username, HttpServletRequest request) {
        UserDetails user = usuarioDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }

    private boolean estaAutenticado(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
