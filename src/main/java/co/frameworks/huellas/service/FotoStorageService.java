package co.frameworks.huellas.service;

import co.frameworks.huellas.exception.NegocioException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

@Service
public class FotoStorageService {

    private static final long MAX_BYTES = 5 * 1024 * 1024; // 5 MB de entrada
    private static final int MAX_LADO = 1200;
    private static final Set<String> TIPOS = Set.of(
            "image/jpeg", "image/jpg", "image/png", "image/webp", "image/gif"
    );

    /**
     * Guarda la imagen redimensionada como data URL en BD (sobrevive el filesystem efímero de Render).
     */
    public String procesarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }
        String contentType = archivo.getContentType() == null ? "" : archivo.getContentType().toLowerCase(Locale.ROOT);
        String nombre = archivo.getOriginalFilename() == null ? "" : archivo.getOriginalFilename().toLowerCase(Locale.ROOT);

        if (nombre.endsWith(".heic") || nombre.endsWith(".heif") || contentType.contains("heic") || contentType.contains("heif")) {
            throw new NegocioException("El formato HEIC/HEIF no está soportado. Exporta la foto como JPG o PNG e inténtalo de nuevo.");
        }
        if (!TIPOS.contains(contentType) && !nombre.matches(".*\\.(jpe?g|png|webp|gif)$")) {
            throw new NegocioException("Solo se permiten imágenes JPG, PNG, WEBP o GIF");
        }
        if (archivo.getSize() > MAX_BYTES) {
            throw new NegocioException("La imagen no puede superar 5 MB. Prueba con una foto más liviana.");
        }

        try {
            byte[] original = archivo.getBytes();
            BufferedImage imagen = ImageIO.read(new ByteArrayInputStream(original));
            if (imagen == null) {
                throw new NegocioException("No se pudo leer la imagen. Usa JPG o PNG.");
            }

            BufferedImage normalizada = redimensionar(imagen);
            byte[] jpeg = aJpeg(normalizada, 0.82f);
            String base64 = Base64.getEncoder().encodeToString(jpeg);
            return "data:image/jpeg;base64," + base64;
        } catch (NegocioException ex) {
            throw ex;
        } catch (IOException ex) {
            throw new NegocioException("No se pudo procesar el archivo de imagen");
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

    private BufferedImage redimensionar(BufferedImage origen) {
        int w = origen.getWidth();
        int h = origen.getHeight();
        double scale = Math.min(1.0, Math.min((double) MAX_LADO / w, (double) MAX_LADO / h));
        int nw = Math.max(1, (int) Math.round(w * scale));
        int nh = Math.max(1, (int) Math.round(h * scale));

        BufferedImage destino = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = destino.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, nw, nh);
        g.drawImage(origen, 0, 0, nw, nh, null);
        g.dispose();
        return destino;
    }

    private byte[] aJpeg(BufferedImage imagen, float calidad) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imagen, "jpg", baos);
            return baos.toByteArray();
        }
        ImageWriter writer = writers.next();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(calidad);
            }
            writer.write(null, new IIOImage(imagen, null, null), param);
        } finally {
            writer.dispose();
        }
        return baos.toByteArray();
    }
}
