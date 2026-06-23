# TUP Mobile – Sistema de Gestión de Entradas (FIFA 2026)
### Documentación Exclusiva del Proyecto Móvil Android

---

# SECCIÓN 1 – PRD (Product Requirements Document) - Enfoque Mobile

## 1.1 Visión del Producto
La aplicación **TUP Mobile** es el canal para dispositivos Android de la plataforma de gestión de entradas del Mundial FIFA 2026. Su propósito es brindar una experiencia fluida y nativa para que los usuarios puedan explorar el catálogo de partidos, registrarse, iniciar sesión y adquirir entradas directamente desde su smartphone.

## 1.2 Alcance del Producto Móvil
La aplicación cubre exclusivamente las interacciones del usuario final (cliente). Las tareas de administración quedan reservadas para el panel web.

## 1.3 Requerimientos Funcionales (RF-Mobile)
| ID | Descripción | Prioridad |
|---|---|---|
| RF-M01 | Listar los partidos del Mundial con información básica (equipos, fecha, estadio). | Alta |
| RF-M02 | Ver el detalle avanzado de un partido seleccionado. | Alta |
| RF-M03 | Registrar un usuario nuevo en la plataforma a través de la app. El formulario solicita nombre, email, contraseña y confirmación de contraseña. El backend valida que `password` y `confirmarPassword` coincidan. | Alta |
| RF-M04 | Iniciar sesión y mantener la sesión activa de forma segura mediante token JWT almacenado en DataStore. | Alta |
| RF-M05 | Confirmar y concretar la compra de tickets de forma in-app, simulando un flujo seguro. | Alta |
| RF-M06 | Consultar el historial de tickets adquiridos por el usuario activo con soporte Offline. | Media |

## 1.4 Requerimientos No Funcionales
- La aplicación debe ser compatible con **Android 8.0 (API 26)** o superior.
- Debe mantener una respuesta fluida, delegando las llamadas de red a hilos secundarios o corrutinas.
- Debe implementar inyección de dependencias estructurada para facilitar el escalado.

---

# SECCIÓN 2 – TRD (Technical Requirements Document) - Stack Android

## 2.1 Stack Tecnológico
- **Lenguaje:** Kotlin.
- **Interfaz de Usuario (UI):** **Jetpack Compose** (paradigma declarativo).
- **Arquitectura:** Clean Architecture + MVVM (Model-View-ViewModel).
- **Inyección de Dependencias (DI):** **Dagger Hilt** (Módulos definidos en `di`).
- **Base de Datos Local:** **Room** con soporte Offline-First (Partidos y Compras).
- **Persistencia de Sesión:** **DataStore Preferences** (Almacenamiento seguro del JWT y UserId).
- **Carga de Imágenes:** **Coil**.
- **Red y API:** Retrofit2 + OkHttp + GsonConverterFactory + AuthInterceptor.
- **Asincronismo:** Kotlin Coroutines & Flow (StateFlow / SharedFlow).
- **Navegación:** **Navigation Compose (2.9.0) Type-Safe** (kotlinx.serialization).
- **Build System:** Gradle (Kotlin DSL).

## 2.2 Estructura del Proyecto
El proyecto está organizado bajo el paquete principal `com.app.partidos`, utilizando un enfoque de **Clean Architecture**:

```text
app/src/main/java/com/app/partidos/
├── data/
│   ├── local/         (Room Database, DAOs, Entidades)
│   ├── remote/        (Retrofit interfaces, API response DTOs, AuthInterceptor)
│   └── repository/    (Implementación de repositorios, Mappers)
├── di/                (Módulos Hilt para Inyección de Dependencias)
├── domain/
│   ├── model/         (Clases de dominio: Partido, Usuario, Compra, Pago)
│   └── repository/    (Interfaces de repositorios)
└── presentation/
    ├── navigation/    (AppNavigation, Rutas Type-Safe)
    └── [features]/    (Carpetas por funcionalidad: home, detail, tickets, purchase, login)
```

---

# SECCIÓN 3 – Diseño UI/UX - Componentes Nativos (Compose)

## 3.1 Pautas de Diseño
- **Material Design 3:** Se utilizan las directrices modernas de Google mediante la librería de Compose Material 3.
- **Tematización Dinámica:** Implementada completamente en código Kotlin sin necesidad de archivos `colors.xml` antiguos, soportando Modo Oscuro/Claro fluidamente.

## 3.2 Componentes de Interfaz
- **Listados:** Utilización de `LazyColumn` en Compose para renderizar eficientemente la grilla de partidos y el historial de tickets.
- **Navegación Type-Safe:** Implementada puramente en código mediante **Navigation Compose**, gestionando rutas entre pantallas utilizando Data Classes tipadas y sin requerir paso de objetos complejos en el `NavController` (se utiliza State-Hoisting local, por ejemplo, en `ValidationScreen`).
- **Carga de Imágenes:** Uso del Composable de `Coil` (`AsyncImage` y `SubcomposeAsyncImage`) para traer los escudos de selecciones y fotos de estadios.

## 3.3 Optimizaciones de Rendimiento y UI
- **Decodificación de Imágenes:** Implementado `SubcomposeAsyncImage` con un `CircularProgressIndicator` para suavizar la carga de SVGs de banderas y evitar "huecos" visuales.
- **Validación de Formularios:** Inputs configurados en única línea (`singleLine = true`) en pantallas de Registro, Login y Checkout para evitar saltos de línea indeseados y forzar un correcto flujo de navegación por teclado.
- **Manejo de Estados de Carga:** Lógica optimizada en `HomeViewModel` para prevenir parpadeos y cruces de estado entre Room y la API.

---

# SECCIÓN 4 – AppFlow (Flujos de la Aplicación Móvil)

## 4.1 Flujo de Navegación Principal (Type-Safe)
```text
MainActivity (Host)
  └─► NavHost (Compose)
        ├─► Ruta: ListaPartidosRoute (Inicio)
        │     └─► [Acción: Mis Tickets] ──► MisTicketsRoute
        ├─► Ruta: DetallePartidoRoute(id)
        │     └─► [Si no hay sesión] ──► Flujo Auth (LoginRoute / RegistroRoute)
        │     └─► [Con sesión activa] ──► CheckoutCompraRoute(id)
        │           └─► ValidationScreen (Interna) ──► MisTicketsRoute
```

## 4.2 Flujo de Autenticación y Seguridad
1. El usuario presiona "Comprar" o "Ingresar" / "Registrarse".
2. Se navega a la pantalla Composable correspondiente.
3. **Registro:** `RegisterViewModel` valida localmente que los campos no estén vacíos, que el email sea válido, que la contraseña tenga mínimo 6 caracteres y que `password` coincida con `confirmarPassword`. Luego llama al repositorio con Retrofit (`POST /api/auth/registrar`).
4. **Login:** El `AuthViewModel` llama al caso de uso, comunicándose con Retrofit (`POST /api/auth/login`).
5. Al recibir la respuesta, la app extrae el JWT y el UserId y los persiste localmente mediante **DataStore**.
6. Todas las peticiones futuras (excepto endpoints libres) interceptan la red (`AuthInterceptor`) inyectando automáticamente `Authorization: Bearer <token>`.

---

# SECCIÓN 5 – Esquema de Datos (Consumo de API y Room)

## 5.1 Modelos de Dominio (Kotlin Data Classes)
```kotlin
// domain/model/Partido.kt
data class Partido(
    val id: String,
    val equipoLocal: String,
    val equipoVisitante: String,
    val fecha: String,
    val hora: String,
    val estadioNombre: String,
    val disponible: Boolean,
    val precio: Double
)
```

## 5.2 Contrato de API REST (Retrofit Interface)
| Método | Endpoint | Respuesta Esperada | Requiere JWT |
|---|---|---|---|
| GET | `/api/partidos` | `List<PartidoDto>` | No |
| POST | `/api/auth/registrar` | `204 No Content` (usuario creado) | No |
| POST | `/api/auth/login` | `LoginResponseDto` (contiene JWT e Id) | No |
| POST | `/api/Compra/crear` | `CompraResponseDto` (Ticket Confirmado) | Sí |

### Contratos / DTOs
- `LoginRequestDto`: Email y Password.
- `LoginResponseDto`: Token, Id, Email, Roles.
- `CrearCompraDto`: UsuarioId, Pago, Tickets.
- `CompraResponseDto`: Id, Fecha, Total, Estado, UsuarioId.
- `CrearPagoDto`: MetodoPago, EstadoPago, Monto.
- `CrearTicketDto`: PartidoId, Sector, Fila, Asiento, Precio.

*El backend puede tener rutas de administración adicionales, pero la app Android las ignora completamente y utiliza Retrofit solo para los flujos del consumidor final.*

> **Nota de integración:** La base de datos del backend está alojada en la nube (`SQL1004.site4now.net`). La app mobile no interactúa directamente con ella; toda la comunicación pasa exclusivamente a través de la API REST.
