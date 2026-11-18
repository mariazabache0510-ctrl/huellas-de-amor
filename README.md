# Huellas de Amor

Aplicación web monolítica para gestionar la **adopción de mascotas** en Villavicencio.

Stack alineado con la materia de Frameworks:

- Spring Boot 4 + Thymeleaf + Spring Security
- Spring Data JPA + PostgreSQL
- DTO, Mapper y Builder
- Repositorios con consultas personalizadas
- Manejo global de excepciones
- Roles: `ADMIN`, `VOLUNTARIO`, `ADOPTANTE`

## Arranque local

```bash
docker compose up -d
./mvnw spring-boot:run
```

Abre http://localhost:8080

### Usuarios demo

| Usuario     | Clave     | Rol        |
|-------------|-----------|------------|
| admin       | admin123  | ADMIN      |
| voluntario  | vol123    | VOLUNTARIO |
| adoptante   | ado123    | ADOPTANTE  |

## Despliegue propuesto: Render

Render es la opción recomendada porque:

1. Soporta **PostgreSQL gestionado** (requerido por el proyecto).
2. Encaja con el binding `0.0.0.0:$PORT`.
3. Permite desplegar con `render.yaml` (Blueprint) web + base de datos.

Pasos:

1. Sube este repositorio a GitHub.
2. Abre el Blueprint: `https://dashboard.render.com/blueprint/new?repo=<URL_HTTPS_DEL_REPO>`
3. Aplica el Blueprint (`huellas-de-amor` + `huellas-de-amor-db`).
4. Espera el deploy; la app sembrará datos demo al iniciar.

## Estructura principal

```
co.frameworks.huellas
  config/        Security + seed + datasource Render
  controller/    MVC Thymeleaf
  dto/ mapper/   Capas de transferencia
  exception/     Handler global
  model/         Entidades y enums
  repository/    JPA + queries
  service/       Reglas de negocio
```

## Entidades

`Usuario`, `Refugio`, `Especie`, `Mascota`, `Adoptante`, `Voluntario`, `Adopcion`, `SeguimientoPostAdopcion`
