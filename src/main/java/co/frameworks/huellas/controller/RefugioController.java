package co.frameworks.huellas.controller;

import co.frameworks.huellas.dto.RefugioDTO;
import co.frameworks.huellas.service.RefugioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/refugios")
public class RefugioController {

    private final RefugioService refugioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("refugios", refugioService.listar());
        return "refugios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("refugio", new RefugioDTO());
        return "refugios/form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("refugio") RefugioDTO dto,
                        BindingResult result,
                        RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "refugios/form";
        }
        refugioService.crear(dto);
        ra.addFlashAttribute("exito", "Refugio registrado");
        return "redirect:/refugios";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("refugio", refugioService.obtener(id));
        return "refugios/form";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("refugio") RefugioDTO dto,
                             BindingResult result,
                             RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "refugios/form";
        }
        refugioService.actualizar(id, dto);
        ra.addFlashAttribute("exito", "Refugio actualizado");
        return "redirect:/refugios";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        refugioService.eliminar(id);
        ra.addFlashAttribute("exito", "Refugio eliminado");
        return "redirect:/refugios";
    }
}
