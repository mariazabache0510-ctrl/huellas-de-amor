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

PostgreSQL local queda en el puerto `55433`. Abre http://localhost:8080

### Usuarios demo

| Usuario     | Clave     | Rol        |
|-------------|-----------|------------|
| admin       | admin123  | ADMIN      |
| voluntario  | vol123    | VOLUNTARIO |
| adoptante   | ado123    | ADOPTANTE  |

## Despliegue propuesto: Render + Neon (o Postgres existente)

Render free solo permite **una** base PostgreSQL activa. Por eso el Blueprint
despliega solo el servicio web y pide `DATABASE_URL` manualmente.

Pasos:

1. Crea una base en [Neon](https://console.neon.tech) (o reutiliza tu Postgres free de Render).
2. Copia la connection string (`postgres://...`).
3. En el Blueprint de Render, al sincronizar, pega ese valor en `DATABASE_URL`.
4. Aplica el sync y espera el deploy; la app sembrará datos demo al iniciar.

Blueprint: `https://dashboard.render.com/blueprint/new?repo=https://github.com/mariazabache0510-ctrl/huellas-de-amor`

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
