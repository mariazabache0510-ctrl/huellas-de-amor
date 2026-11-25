package co.frameworks.huellas.validation;

import co.frameworks.huellas.dto.RegistroAdoptanteDTO;
import org.springframework.validation.BindingResult;

import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public final class RegistroValidators {

    private static final Pattern PASSWORD = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,60}$");
    private static final Pattern USERNAME = Pattern.compile("^[a-zA-Z0-9._-]{4,30}$");
    private static final Pattern NOMBRE = Pattern.compile("^[\\p{L} .'-]{3,80}$");
    private static final Pattern EMAIL = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@(gmail\\.com|googlemail\\.com|hotmail\\.com|hotmail\\.es|outlook\\.com|outlook\\.es|live\\.com|yahoo\\.com|yahoo\\.es|icloud\\.com|me\\.com|mac\\.com|proton\\.me|protonmail\\.com|edu\\.co|[A-Za-z0-9.-]+\\.edu\\.co|huellasdeamor\\.com)$",
            Pattern.CASE_INSENSITIVE);

    private static final Set<String> OFENSIVOS = Set.of(
            "puto", "puta", "mierda", "pendejo", "idiota", "estupido", "estúpido",
            "negrode", "hijueputa", "hp", "verga", "coño", "marica", "malparido",
            "admin", "root", "null", "script", "drop", "select", "insert", "delete"
    );

    private RegistroValidators() {
    }

    public static void validar(RegistroAdoptanteDTO dto, BindingResult result) {
        if (dto.getNombre() != null) {
            String nombre = dto.getNombre().trim();
            if (nombre.length() < 3) {
                result.rejectValue("nombre", "size", "El nombre debe tener al menos 3 caracteres");
            } else if (!NOMBRE.matcher(nombre).matches()) {
                result.rejectValue("nombre", "pattern", "Usa solo letras y espacios válidos");
            } else if (contieneOfensivo(nombre)) {
                result.rejectValue("nombre", "offensive", "El nombre contiene términos no permitidos");
            }
        }

        if (dto.getUsername() != null) {
            String user = dto.getUsername().trim();
            if (!USERNAME.matcher(user).matches()) {
                result.rejectValue("username", "pattern",
                        "Usuario de 4-30 caracteres (letras, números, . _ -)");
            } else if (contieneOfensivo(user)) {
                result.rejectValue("username", "offensive", "Ese usuario no está permitido");
            }
        }

        if (dto.getCorreo() != null) {
            String correo = dto.getCorreo().trim().toLowerCase(Locale.ROOT);
            if (!EMAIL.matcher(correo).matches()) {
                result.rejectValue("correo", "email",
                        "Usa un correo real (Gmail, Hotmail, Outlook, Yahoo, iCloud, Proton o edu.co)");
            }
        }

        if (dto.getPassword() != null && !PASSWORD.matcher(dto.getPassword()).matches()) {
            result.rejectValue("password", "strength",
                    "Mínimo 8 caracteres, con mayúscula, minúscula, número y carácter especial");
        } else if (dto.getPassword() != null && contieneOfensivo(dto.getPassword())) {
            result.rejectValue("password", "offensive", "La contraseña no puede usar palabras ofensivas");
        }
    }

    private static boolean contieneOfensivo(String value) {
        String lower = value.toLowerCase(Locale.ROOT);
        for (String palabra : OFENSIVOS) {
            if (lower.contains(palabra)) {
                return true;
            }
        }
        return false;
    }
}
