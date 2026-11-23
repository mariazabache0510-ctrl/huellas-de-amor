# Huellas de Amor

Aplicación **full stack** para gestionar la adopción responsable de mascotas en Villavicencio (Meta, Colombia).

Proyecto final de Frameworks: monolito Spring Boot con vistas Thymeleaf y seguridad por roles.

## Tecnologías elegidas (justificación)

| Tecnología | Uso | Por qué |
|------------|-----|---------|
| **Spring Boot 4** | Backend + despliegue | Servicios, JPA, configuración por entorno |
| **Thymeleaf** | Frontend server-side | Vistas HTML con el mismo stack, sin SPA extra |
| **Spring Security** | AuthN/AuthZ | Roles ADMIN / VOLUNTARIO / ADOPTANTE, CSRF, CSP |
| **Spring Data JPA + PostgreSQL** | Persistencia | Modelo relacional coherente con entidades |
| React / Next.js / Strapi / SerpAPI | No usadas | El problema se resuelve con un monolito MVC desplegable en Render |

Informe académico detallado: [`docs/INFORME.md`](docs/INFORME.md) (contexto, RF/RNF, modelo, endpoints y vistas).

## Arranque local

Requisitos: Java 25+, Docker (PostgreSQL).

```bash
docker compose up -d
./mvnw spring-boot:run
```

Abre http://localhost:8080  
PostgreSQL local: puerto `55433` (ver `docker-compose.yml`).

### Usuarios demo

| Usuario     | Clave     | Rol        |
|-------------|-----------|------------|
| admin       | admin123  | ADMIN      |
| voluntario  | vol123    | VOLUNTARIO |
| adoptante   | ado123    | ADOPTANTE  |

## Despliegue (Render + Neon)

1. Crea una base en [Neon](https://console.neon.tech) y copia `DATABASE_URL`.
2. Conecta el repo en Render (Blueprint `render.yaml`) y pega `DATABASE_URL`.
3. Deploy automático desde `main`.

Producción: https://huellas-de-amor.onrender.com

## Módulos principales

- **Público**: inicio, catálogo, detalle, refugios, cómo adoptar, términos/privacidad.
- **Adoptante**: solicitud de adopción e historial.
- **Voluntario**: revisión de solicitudes, inventario y seguimientos.
- **Admin**: KPIs, gráficas y pestañas de gestión (mascotas, refugios, adopciones, seguimientos).

## Estructura del código

```
co.frameworks.huellas
  config/        Security, seed, datasource
  controller/    MVC Thymeleaf
  dto/ mapper/   Transferencia y mapeo
  exception/     Handler global
  model/         Entidades JPA
  repository/    Consultas
  service/       Reglas de negocio
  security/      Rate limit login
  validation/    Reglas de registro
```

## Entidades

`Usuario` (con enum **`Rol`**: ADMIN / VOLUNTARIO / ADOPTANTE), `Refugio`, `Especie`, `Mascota`, `Adopcion`, `SeguimientoPostAdopcion`

No hay entidades separadas Adoptante/Voluntario/Admin: el tipo de usuario se define solo con `Usuario.rol`.
