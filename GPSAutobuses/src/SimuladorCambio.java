import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class SimuladorCambio {

    public static void modificarRecorrido(String rutaArchivo, String idSeleccionado) {
        ArrayList<DatoGPS> datos = LectorDatosGPS.leerDatosGPS(rutaArchivo);
        ArrayList<DatoGPS> nuevosDatos = new ArrayList<>();

        // Punto base nuevo para el recorrido simulado
        double latBase = 40.415 + Math.random() * 0.01;
        double lonBase = -3.705 + Math.random() * 0.01;
        double paso = 0.0005;
        Random random = new Random();
        double angulo = random.nextDouble() * 2 * Math.PI;

        int contador = 0;
        for (DatoGPS dato : datos) {
            if (dato.getIdAutobus().equals(idSeleccionado)) {
                // Nueva ruta aleatoria suave
                angulo += (random.nextDouble() - 0.5) * 0.4;
                double nuevaLat = latBase + Math.sin(angulo) * paso * contador;
                double nuevaLon = lonBase + Math.cos(angulo) * paso * contador;

                dato.setLatitud(nuevaLat);
                dato.setLongitud(nuevaLon);
                contador++;
            }
            nuevosDatos.add(dato);
        }

        // Guardar el archivo modificado
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo))) {
            escritor.write("idAutobus,marcaTiempo,latitud,longitud,velocidad\n");
            for (DatoGPS dato : nuevosDatos) {
                escritor.write(dato.getIdAutobus() + "," +
                        dato.getMarcaTiempo() + "," +
                        String.format(java.util.Locale.US, "%.6f", dato.getLatitud()) + "," +
                        String.format(java.util.Locale.US, "%.6f", dato.getLongitud()) + "," +
                        dato.getVelocidad() + "\n");
            }
        } catch (IOException e) {
            System.out.println("❌ Error al guardar archivo modificado: " + e.getMessage());
        }
    }
}
