import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ExportadorJSON {

    public static void exportarUltimasPosiciones(ArrayList<DatoGPS> datos) {
        HashMap<String, DatoGPS> ultimaPosicion = new HashMap<>();

        // Recorremos todos los datos y actualizamos la última posición por busId
        for (DatoGPS dato : datos) {
            ultimaPosicion.put(dato.getIdAutobus(), dato);
        }

        // Creamos un archivo JSON para cada autobús
        for (String id : ultimaPosicion.keySet()) {
            DatoGPS dato = ultimaPosicion.get(id);

            String nombreArchivo = "src/estado_" + id.toLowerCase() + ".json";

            try (FileWriter escritor = new FileWriter(nombreArchivo)) {
                StringBuilder json = new StringBuilder();
                json.append("{\n");
                json.append("  \"idAutobus\": \"" + dato.getIdAutobus() + "\",\n");
                json.append("  \"latitud\": " + dato.getLatitud() + ",\n");
                json.append("  \"longitud\": " + dato.getLongitud() + ",\n");
                json.append("  \"marcaTiempo\": \"" + dato.getMarcaTiempo() + "\"\n");
                json.append("}");

                escritor.write(json.toString());
                System.out.println("JSON exportado: " + nombreArchivo);

            } catch (IOException e) {
                System.out.println("Error exportando JSON para " + id);
            }
        }
    }
}