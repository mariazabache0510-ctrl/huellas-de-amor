package co.frameworks.huellas.service;

import co.frameworks.huellas.exception.NegocioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class FotoStorageService {

    private static final long MAX_BYTES = 5 * 1024 * 1024;
    private static final Set<String> EXTENSIONES = Set.of("jpg", "jpeg", "png", "webp", "gif");
    private static final Map<String, String> CONTENT_TO_EXT = Map.of(
            "image/jpeg", "jpg",
            "image/jpg", "jpg",
            "image/png", "png",
            "image/webp", "webp",
            "image/gif", "gif"
    );

    private final Path uploadDir;

    public FotoStorageService(@Value("${app.upload-dir:uploads}") String uploadDir) {
        this.uploadDir = Path.of(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo crear el directorio de uploads: " + this.uploadDir, ex);
        }
    }

    public String procesarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }

        String original = archivo.getOriginalFilename() == null ? "" : archivo.getOriginalFilename();
        String lower = original.toLowerCase(Locale.ROOT);
        String contentType = archivo.getContentType() == null ? "" : archivo.getContentType().toLowerCase(Locale.ROOT);

        if (lower.endsWith(".heic") || lower.endsWith(".heif") || contentType.contains("heic") || contentType.contains("heif")) {
            throw new NegocioException("El formato HEIC/HEIF no está soportado. Guarda la foto como JPG o PNG e inténtalo de nuevo.");
        }
        if (archivo.getSize() > MAX_BYTES) {
            throw new NegocioException("La imagen no puede superar 5 MB.");
        }

        String ext = extensionDe(lower, contentType);
        if (ext == null) {
            throw new NegocioException("Solo se permiten imágenes JPG, PNG, WEBP o GIF.");
        }

        String nombre = UUID.randomUUID().toString().replace("-", "") + "." + ext;
        Path destino = uploadDir.resolve(nombre).normalize();
        if (!destino.startsWith(uploadDir)) {
            throw new NegocioException("Nombre de archivo no válido.");
        }

        try (InputStream in = archivo.getInputStream()) {
            Files.copy(in, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new NegocioException("No se pudo guardar la imagen en el servidor.");
        }

        return "/uploads/" + nombre;
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

    private String extensionDe(String filename, String contentType) {
        int dot = filename.lastIndexOf('.');
        if (dot >= 0) {
            String ext = filename.substring(dot + 1);
            if (EXTENSIONES.contains(ext)) {
                return ext.equals("jpeg") ? "jpg" : ext;
            }
        }
        return CONTENT_TO_EXT.get(contentType);
    }
}
