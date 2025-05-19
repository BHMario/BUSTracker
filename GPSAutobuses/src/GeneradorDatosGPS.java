import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class GeneradorDatosGPS {

    public static void main(String[] args) {
        String archivoPrincipal = "src/datos_gps.csv";
        String carpetaArchivados = "src/archivados";
        int registrosPorBus = 50;

        // 📁 Crear carpeta archivados si no existe
        File carpeta = new File(carpetaArchivados);
        if (!carpeta.exists()) carpeta.mkdirs();

        // 📦 Mover archivo actual a archivado si existe
        File archivoOriginal = new File(archivoPrincipal);
        if (archivoOriginal.exists()) {
            String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            File archivoNuevo = new File(carpetaArchivados + "/datos_" + fecha + ".csv");
            try {
                Files.move(archivoOriginal.toPath(), archivoNuevo.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("📁 Archivo antiguo archivado como: " + archivoNuevo.getName());
            } catch (IOException e) {
                System.out.println("❌ No se pudo archivar el archivo anterior: " + e.getMessage());
            }
        }

        // 🧹 Eliminar archivos archivados con más de 7 días
        File[] archivados = carpeta.listFiles((dir, name) -> name.endsWith(".csv"));
        if (archivados != null) {
            long ahora = System.currentTimeMillis();
            for (File f : archivados) {
                long edad = ahora - f.lastModified();
                if (edad > 7L * 24 * 60 * 60 * 1000) {
                    if (f.delete()) {
                        System.out.println("🗑️ Archivo eliminado por antigüedad: " + f.getName());
                    }
                }
            }
        }

        // 🧭 Datos por cada bus
        String[] buses = {"BUS01", "BUS02", "BUS03"};
        double[][] puntosIniciales = {
                {40.4168, -3.7038},
                {40.4179, -3.7055},
                {40.4185, -3.7012}
        };

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoPrincipal))) {
            writer.write("idAutobus,marcaTiempo,latitud,longitud,velocidad\n");
            for (int i = 0; i < buses.length; i++) {
                generarTrayectoriaAleatoria(writer, buses[i], puntosIniciales[i][0], puntosIniciales[i][1], registrosPorBus);
            }
            System.out.println("✅ Nuevos datos generados en: " + archivoPrincipal);
        } catch (IOException e) {
            System.out.println("❌ Error al generar el archivo nuevo: " + e.getMessage());
        }
    }

    private static void generarTrayectoriaAleatoria(BufferedWriter writer, String idBus, double latInicial, double lonInicial, int cantidad) throws IOException {
        Random random = new Random();
        double lat = latInicial;
        double lon = lonInicial;

        double paso = 0.0005;
        double angulo = random.nextDouble() * 2 * Math.PI;

        for (int i = 0; i < cantidad; i++) {
            int velocidad = random.nextInt(41); // 0 a 40 km/h
            String tiempo = "2024-05-16 10:" + String.format("%02d", i);

            writer.write(idBus + "," + tiempo + "," +
                    String.format(Locale.US, "%.6f", lat) + "," +
                    String.format(Locale.US, "%.6f", lon) + "," +
                    velocidad + "\n");

            angulo += (random.nextDouble() - 0.5) * 0.4;
            lat += Math.sin(angulo) * paso;
            lon += Math.cos(angulo) * paso;
        }
    }
}
