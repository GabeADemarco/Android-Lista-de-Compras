# Android Lista de Compras

App Android personal para listas de supermercado y gastos en efectivo. Sin publicidad, sin internet: todos los datos quedan guardados solo en tu teléfono.

## Versión actual: 0.3.0-alpha

- Agregar varios artículos seguidos sin cerrar el diálogo
- Mover artículos entre listas
- Enlace a releases de GitHub en el header
- Número de versión visible en el header

**Descargar APK:** [Releases en GitHub](https://github.com/GabeADemarco/Android-Lista-de-Compras/releases/latest)

---

## Cómo usar la app

### Primeros pasos

1. Abrí la app **Mis Listas**.
2. Tocá el botón **+** (abajo a la derecha) para crear tu primera lista (ej. *Supermercado*, *Gastos enero*).
3. Tocá el encabezado de la lista para **desplegarla**.
4. Tocá **Agregar artículo** y completá el nombre. La cantidad y el valor son opcionales.
5. En el supermercado, tocá cada artículo cuando lo pongas en el carrito: se **tacha y se apaga** (queda marcado como listo).

### Gestos principales

| Qué querés hacer | Cómo |
|------------------|------|
| Crear una lista nueva | Botón **+** flotante |
| Desplegar o contraer una lista | Tocar el **encabezado** de la lista |
| Marcar artículo como listo / desmarcar | **Tocar** el artículo |
| Editar o eliminar lista | **Mantener apretado** sobre la lista |
| Editar, mover o eliminar artículo | **Mantener apretado** sobre el artículo |
| Agregar artículo a una lista | **Agregar artículo** (con la lista desplegada) |
| Agregar varios artículos seguidos | En «Nuevo artículo», **Agregar** vacía el formulario y deja el diálogo abierto; **Cancelar** cierra |

### Barra superior

| Ícono | Función |
|-------|---------|
| Enlace externo | Abre **releases en GitHub** para ver o descargar actualizaciones |
| Cuadrícula (⊞) | Cambia a **vista chips**: los artículos se ven como pastillas que llenan el renglón |
| Lista (≡) | Vuelve a la **vista lista** clásica (nombre, cantidad y valor en renglones) |
| Brillo (auto / sol / luna) | Cambia el **tema**: Sistema → Claro → Oscuro → Sistema |
| Texto bajo el título | Muestra la **versión instalada** (ej. `0.3.0-alpha`) para comparar con GitHub |

La preferencia de tema y de vista se **guarda sola** al cerrar la app.

### Vista lista vs vista chips

**Vista lista** (ideal para el super con detalles):
- Muestra nombre, cantidad y valor de cada artículo.
- Un artículo por renglón.

**Vista chips** (aprovecha mejor el ancho de pantalla):
- Cada artículo es una pastilla redondeada; van una al lado de la otra y bajan de línea si no entran.
- Muestra solo el **nombre**, o **nombre x cantidad** si tiene cantidad (ej. `Leche x2`).
- No muestra el precio en la pastilla, pero el **total pendiente** de la lista sigue visible abajo.

En ambas vistas: tocar marca/desmarca; mantener apretado permite editar, mover o eliminar.

### Cantidad y valor

- **Cantidad:** texto libre (ej. `2`, `1 kg`, `pack x6`).
- **Valor:** número para anotar gastos (ej. `1500` o `1500.50`). Sirve para llevar control y ver el total pendiente.
- El **total pendiente** suma solo los artículos **no marcados** que tengan valor.

### Ideas de uso

| Lista | Para qué |
|-------|----------|
| Supermercado | Ir tachando lo que ya metiste en el carrito |
| Gastos en efectivo | Anotar montos y después pasarlos a tu planilla |
| Cualquier otra cosa | Lista de tareas, pendientes, etc. |

---

## Instalación en el teléfono

### Opción A — Descargar APK desde GitHub (más fácil)

1. Andá a [Releases](https://github.com/GabeADemarco/Android-Lista-de-Compras/releases/latest).
2. Descargá `MisListas-0.3.0-alpha.apk` (o la última de [Releases](https://github.com/GabeADemarco/Android-Lista-de-Compras/releases/latest)).
3. Abrilo en el teléfono e instalalo.
4. Si Android lo pide, permití **instalar apps desconocidas** para el explorador o app que uses para abrir el archivo.

### Opción B — Compilar vos mismo

En la raíz del proyecto:

```bat
gradlew.bat assembleDebug
```

El APK queda en `app\build\outputs\apk\debug\app-debug.apk`.

### Opción C — Android Studio

1. Abrí el proyecto en Android Studio.
2. Conectá el teléfono (opcional) o usá un emulador.
3. Pulsá **Run** (triángulo verde).

### Opción D — Instalar por cable

```bat
gradlew.bat installDebug
```

---

## Requisitos

- Android 8.0 o superior (API 26+)
- Para compilar: Android Studio o JDK 17 + Android SDK

---

## Historial de versiones

| Versión | Cambios |
|---------|---------|
| **0.3.0-alpha** | Agregar artículos en racha, mover entre listas, link a releases, versión en header |
| **0.2.0-alpha** | Modo oscuro, vista chips, preferencias guardadas |
| **0.1.0-alpha** | Primera versión: listas, artículos, marcar/editar/eliminar |

---

## Tecnologías

- Kotlin + Jetpack Compose
- Room (base de datos local)
- DataStore (preferencias de tema y vista)
- Material 3

---

## Licencia

Uso personal. Libre para fork y modificación.
