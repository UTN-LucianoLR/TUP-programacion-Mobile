# TUP-programacion-Mobile
### Sistema de Gestión de Entradas — Mundial FIFA 2026

Aplicación Android nativa desarrollada en Kotlin con Jetpack Compose para la materia Programación III (TUP).

---

## Requisitos previos

- **Android Studio** (versión Hedgehog o superior)
- **JDK 17** o superior
- La **API Backend** corriendo localmente (proyecto `TUP - Mundial.WebApiApp`)

---

## Configuración inicial

El archivo `local.properties` **no está incluido en el repositorio** (es ignorado por `.gitignore` por contener valores locales de cada máquina). Debés crearlo vos antes de compilar.

### Pasos

1. Cloná el repositorio y abrilo en Android Studio.
2. En la raíz del proyecto, copiá el archivo de plantilla:
   ```
   local.properties.example  →  local.properties
   ```
3. Editá `local.properties` y configurá la URL de la API según tu caso:

---

## Configurar la URL de la API (`API_BASE_URL`)

### ▶ Si usás el emulador de Android Studio

No necesitás hacer nada especial. Usá la siguiente URL (ya está como valor por defecto):

```
API_BASE_URL=http://10.0.2.2:5066/
```

> `10.0.2.2` es la dirección especial que el emulador de Android usa para referirse al `localhost` de tu computadora.

---

### 📱 Si probás en un celular físico (conexión inalámbrica)

En este caso, `10.0.2.2` **no funciona**. Tu celular necesita apuntar a la dirección IP real de tu computadora dentro de la red local (Wi-Fi).

#### ¿Cómo averiguo mi IP?

Abrí la terminal (CMD) y escribí:

```
ipconfig
```

Buscá el adaptador activo (Wi-Fi o Ethernet) y copiá el valor que dice **"Dirección IPv4"**. Ejemplo: `192.168.1.15`.

#### Configuración en `local.properties`

```
API_BASE_URL=http://192.168.1.15:5066/
```

> ⚠️ Reemplazá `192.168.1.15` por la IP real de **tu** computadora. Esta IP puede cambiar si te reconectás a otra red.

---

## Levantar la API Backend

Antes de correr la app, asegurate de que el proyecto `TUP - Mundial.WebApiApp` esté corriendo en Visual Studio. Deberías ver en la consola:

```
Now listening on: http://localhost:5066
Now listening on: http://0.0.0.0:5066
```

---

## Compilar y correr

Una vez configurado el `local.properties`, sincronizá Gradle desde Android Studio y ejecutá la app en el emulador o dispositivo físico que prefieras.
