# GPSBus


---

## 🚀 Cómo Ejecutar

### ✅ Requisitos

- **Java 21 o superior**
- Un entorno como **IntelliJ IDEA**, **Eclipse**, o terminal con `javac`
- **Conexión a Internet** para cargar el mapa (Leaflet CDN)

### ▶️ Ejecución

1. Abrí el proyecto en IntelliJ o tu entorno favorito.
2. Ejecutá la clase `InterfazGPS.java`.
3. Navegá por la interfaz gráfica.

---

## 🎮 Funcionalidades

### 📍 Generar datos de autobuses
- Simula datos de posición GPS cada 10 segundos.
- Los guarda en `src/datos_gps.csv`.
- Si ya existe un archivo anterior, se archiva automáticamente en `src/archivados/`.

### 🗺️ Ver recorrido de autobuses
- Elige uno de los 3 autobuses disponibles.
- Abre un mapa interactivo (con animación) en el navegador mostrando su ruta.
- Basado en los datos generados aleatoriamente.

### 📊 Ver estadísticas
- Muestra por autobús:
  - Velocidad media
  - Número de paradas (velocidad 0)

### 🔁 Simular cambio de recorrido
- Seleccioná un bus para modificar aleatoriamente su trayectoria en `datos_gps.csv`.
- Los datos anteriores se sobrescriben.

### 📤 Exportar a JSON
- Guarda un archivo JSON con la última posición conocida de cada autobús.
- Útil para integraciones o visualizaciones externas.

---

## 🎨 Interfaz Gráfica

- Desarrollada con **Swing**
- Diseño oscuro con detalles dorados
- Botones grandes, verticales y centrados
- Ventana redimensionable y estética

---

## 🛠️ Personalización

- Podés modificar el número de autobuses o el comportamiento de generación en `GeneradorDatosGPS.java`
- El archivo CSV es fácilmente editable y legible

---

## 🧠 Consideraciones Técnicas

- Los mapas se visualizan desde **HTML generado dinámicamente**, abierto en el navegador.
- **Leaflet.js y OpenStreetMap** se usan desde CDN, por lo que **es necesaria conexión a Internet**.
- No requiere levantar servidores ni instalar librerías adicionales.

---

## 📦 Dependencias

- Solo librerías estándar de Java (`java.awt`, `java.io`, `javax.swing`)
- **Leaflet.js** (desde CDN para mapas)

---

## 👨‍💻 Autor

Este proyecto fue desarrollado por Mario Sánchez Ruiz
