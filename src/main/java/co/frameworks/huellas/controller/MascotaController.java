package co.frameworks.huellas.controller;

import co.frameworks.huellas.dto.MascotaDTO;
import co.frameworks.huellas.model.EstadoMascota;
import co.frameworks.huellas.repository.EspecieRepository;
import co.frameworks.huellas.service.MascotaService;
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
@RequestMapping("/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;
    private final RefugioService refugioService;
    private final EspecieRepository especieRepository;

    @GetMapping("/catalogo")
    public String catalogo(@RequestParam(required = false) Long refugioId,
                           @RequestParam(required = false) Long especieId,
                           Model model) {
        model.addAttribute("mascotas", mascotaService.buscar(EstadoMascota.DISPONIBLE, refugioId, especieId));
        model.addAttribute("refugios", refugioService.listar());
        model.addAttribute("especies", especieRepository.findAll());
        model.addAttribute("refugioId", refugioId);
        model.addAttribute("especieId", especieId);
        return "mascotas/catalogo";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("mascota", mascotaService.obtener(id));
        return "mascotas/detalle";
    }

    @GetMapping("/gestionar")
    public String gestionar(Model model) {
        model.addAttribute("mascotas", mascotaService.buscar(null, null, null));
        return "mascotas/gestionar";
    }

    @GetMapping("/gestionar/nueva")
    public String nueva(Model model) {
        cargarForm(model, MascotaDTO.builder().estado(EstadoMascota.DISPONIBLE).build());
        return "mascotas/form";
    }

    @PostMapping("/gestionar")
    public String crear(@Valid @ModelAttribute("mascota") MascotaDTO dto,
                        BindingResult result,
                        Model model,
                        RedirectAttributes ra) {
        if (result.hasErrors()) {
            cargarForm(model, dto);
            return "mascotas/form";
        }
        mascotaService.crear(dto);
        ra.addFlashAttribute("exito", "Mascota registrada correctamente");
        return "redirect:/mascotas/gestionar";
    }

    @GetMapping("/gestionar/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        cargarForm(model, mascotaService.obtener(id));
        return "mascotas/form";
    }

    @PostMapping("/gestionar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("mascota") MascotaDTO dto,
                             BindingResult result,
                             Model model,
                             RedirectAttributes ra) {
        if (result.hasErrors()) {
            cargarForm(model, dto);
            return "mascotas/form";
        }
        mascotaService.actualizar(id, dto);
        ra.addFlashAttribute("exito", "Mascota actualizada");
        return "redirect:/mascotas/gestionar";
    }

    @PostMapping("/gestionar/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        mascotaService.eliminar(id);
        ra.addFlashAttribute("exito", "Mascota eliminada");
        return "redirect:/mascotas/gestionar";
    }

    private void cargarForm(Model model, MascotaDTO dto) {
        model.addAttribute("mascota", dto);
        model.addAttribute("refugios", refugioService.listar());
        model.addAttribute("especies", especieRepository.findAll());
        model.addAttribute("estados", EstadoMascota.values());
    }
}
