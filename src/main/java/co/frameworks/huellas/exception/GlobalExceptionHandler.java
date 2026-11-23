package co.frameworks.huellas.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(RecursoNoEncontradoException ex, Model model) {
        model.addAttribute("titulo", "No encontrado");
        model.addAttribute("mensaje", ex.getMessage());
        return "error/general";
    }

    @ExceptionHandler(NegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNegocio(NegocioException ex, Model model) {
        model.addAttribute("titulo", "Operación no permitida");
        model.addAttribute("mensaje", ex.getMessage());
        return "error/general";
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class, MultipartException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUpload(Exception ex, Model model) {
        log.warn("Error de carga de archivo", ex);
        model.addAttribute("titulo", "Archivo no válido");
        model.addAttribute("mensaje", "La imagen es demasiado grande o el formato no es válido. Usa JPG/PNG de máximo 5 MB.");
        return "error/general";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneral(Exception ex, Model model) {
        log.error("Error no controlado", ex);
        model.addAttribute("titulo", "Error inesperado");
        model.addAttribute("mensaje", "Ocurrió un problema al procesar la solicitud. Si subías una foto, prueba con JPG/PNG más liviano (menos de 5 MB).");
        return "error/general";
    }
}
