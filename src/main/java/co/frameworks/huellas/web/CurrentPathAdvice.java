package co.frameworks.huellas.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CurrentPathAdvice {

    @ModelAttribute("currentPath")
    public String currentPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri == null) {
            return "/";
        }
        int semicolon = uri.indexOf(';');
        return semicolon >= 0 ? uri.substring(0, semicolon) : uri;
    }
}
