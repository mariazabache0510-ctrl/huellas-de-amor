package co.frameworks.huellas.service;

import co.frameworks.huellas.exception.NegocioException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Set;

@Service
public class FotoStorageService {

    private static final long MAX_BYTES = 2 * 1024 * 1024; // 2 MB
    private static final Set<String> TIPOS = Set.of(
            "image/jpeg", "image/jpg", "image/png", "image/webp", "image/gif"
    );

    /**
     * Guarda la imagen como data URL en BD para que sobreviva el filesystem efímero de Render.
     */
    public String procesarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }
        String contentType = archivo.getContentType() == null ? "" : archivo.getContentType().toLowerCase();
        if (!TIPOS.contains(contentType)) {
            throw new NegocioException("Solo se permiten imágenes JPG, PNG, WEBP o GIF");
        }
        if (archivo.getSize() > MAX_BYTES) {
            throw new NegocioException("La imagen no puede superar 2 MB");
        }
        try {
            String base64 = Base64.getEncoder().encodeToString(archivo.getBytes());
            String mime = contentType.equals("image/jpg") ? "image/jpeg" : contentType;
            return "data:" + mime + ";base64," + base64;
        } catch (IOException ex) {
            throw new NegocioException("No se pudo leer el archivo de imagen");
        }
    }

    public String resolverFoto(MultipartFile archivo, String fotoUrl) {
        String desdeArchivo = procesarArchivo(archivo);
        if (desdeArchivo != null) {
            return desdeArchivo;
        }
        if (fotoUrl != null && !fotoUrl.isBlank()) {
            return fotoUrl.trim();
        }
        return null;
    }
}
