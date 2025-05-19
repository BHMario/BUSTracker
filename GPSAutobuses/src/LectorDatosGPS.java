import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

// En esta clase nos encargamos de leer el archivo CSV y de convertirlo en una lista de objetos
public class LectorDatosGPS {
    // Recibe la ruta al archivo CSV y devuelve una lista de objetos
    public static ArrayList<DatoGPS> leerDatosGPS(String rutaArchivo) {
        ArrayList<DatoGPS> listaGPS = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            lector.readLine(); // Saltar encabezado del CSV

            // Leer cada linea del archivo
            while ((linea = lector.readLine()) != null) {
                linea = linea.trim(); // Eliminar espacios al inicio y final
                if (linea.isEmpty()) {
                    continue;
                }

                // Dividir y limpiar cada parte
                String[] partes = linea.split(",");
                // Si tiene los 5 campos necesarios, convertimos los valores y creamos el objeto
                if (partes.length == 5) {
                    try {
                        String id = partes[0].trim();
                        String marcaTiempo = partes[1].trim();
                        double latitud = Double.parseDouble(partes[2].trim());
                        double longitud = Double.parseDouble(partes[3].trim());
                        double velocidad = Double.parseDouble(partes[4].trim());

                        DatoGPS dato = new DatoGPS(id, marcaTiempo, latitud, longitud, velocidad);
                        listaGPS.add(dato);
                    } catch (NumberFormatException e) {
                        System.out.println("Formato numérico inválido en línea: " + linea);
                    }
                } else {
                    System.out.println("Linea mal formada: " + linea);
                }
            }
            // Mensaje de confirmación con el numero total de registros leidos
            System.out.println("Se leyeron " + listaGPS.size() + " registros.");
        } catch (IOException e) {
            e.printStackTrace(); // En caso de error
        }

        return listaGPS;
    }
}