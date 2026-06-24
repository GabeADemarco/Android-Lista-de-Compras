# Android Lista de Compras

App Android personal para listas de supermercado y gastos en efectivo. Sin publicidad, sin internet, datos guardados solo en el dispositivo.

## Requisitos

- Android Studio (recomendado) o JDK 17 + Android SDK
- Dispositivo Android 8.0+ (API 26)

## Instalación en el teléfono

### Opción A — Android Studio

1. Abrí Android Studio → **Open** → seleccioná la carpeta del proyecto.
2. Esperá a que Gradle sincronice.
3. Conectá el teléfono con **Depuración USB** activada (opcional).
4. Pulsá **Run** y elegí tu dispositivo o emulador.

### Opción B — APK manual

En la raíz del proyecto:

```bat
gradlew.bat assembleDebug
```

El APK queda en `app\build\outputs\apk\debug\app-debug.apk`. Copialo al teléfono e instalalo.

### Opción C — Instalar por cable

```bat
gradlew.bat installDebug
```

## Uso

| Acción | Gestos |
|--------|--------|
| Crear lista | Botón **+** flotante |
| Desplegar / contraer lista | Tocar el encabezado |
| Marcar artículo como listo | Tocar el artículo |
| Editar o eliminar lista / artículo | Mantener apretado |
| Agregar artículo | **Agregar artículo** dentro de una lista desplegada |

- **Cantidad** y **valor** son opcionales.
- Si hay valores, se muestra el **total pendiente** (solo artículos no marcados).
- Los datos se guardan localmente en el teléfono.

## Tecnologías

- Kotlin + Jetpack Compose
- Room (base de datos local)
- Material 3

## Licencia

Uso personal. Libre para fork y modificación.
