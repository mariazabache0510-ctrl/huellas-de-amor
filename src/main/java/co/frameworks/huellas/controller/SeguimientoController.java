package co.frameworks.huellas.controller;

import co.frameworks.huellas.dto.SeguimientoDTO;
import co.frameworks.huellas.repository.AdoptanteRepository;
import co.frameworks.huellas.repository.MascotaRepository;
import co.frameworks.huellas.service.SeguimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seguimientos")
public class SeguimientoController {

    private final SeguimientoService seguimientoService;
    private final MascotaRepository mascotaRepository;
    private final AdoptanteRepository adoptanteRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("seguimientos", seguimientoService.listar());
        return "seguimientos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("seguimiento", new SeguimientoDTO());
        model.addAttribute("mascotas", mascotaRepository.findAll());
        model.addAttribute("adoptantes", adoptanteRepository.findAllConUsuario());
        return "seguimientos/form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("seguimiento") SeguimientoDTO dto,
                        BindingResult result,
                        Model model,
                        RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("mascotas", mascotaRepository.findAll());
            model.addAttribute("adoptantes", adoptanteRepository.findAllConUsuario());
            return "seguimientos/form";
        }
        seguimientoService.crear(dto);
        ra.addFlashAttribute("exito", "Seguimiento registrado");
        return "redirect:/seguimientos";
    }
}
