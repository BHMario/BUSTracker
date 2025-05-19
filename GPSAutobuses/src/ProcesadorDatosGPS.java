import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ProcesadorDatosGPS {
    // Validar si los datos de uno de los registros son coherentes
    public static boolean validarRegistro(DatoGPS dato) {
        try {
            // Validar marca temporal
            LocalDateTime.parse(dato.getMarcaTiempo());
            // Latitud entre -90 y +90
            if (dato.getLatitud() < -90 || dato.getLatitud() > 90) {
                return false;
            }
            // Longitud entre -180 y +180
            if (dato.getLongitud() < -180 || dato.getLongitud() > 180) {
                return false;
            }
            // Velocidad mayor o igual a 0
            if (dato.getVelocidad() < 0) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Filtrar los datos por ID del autobus y por el rango horario
    public static ArrayList<DatoGPS> filtrarPorBusYRango(ArrayList<DatoGPS> datos, String idAutobus, String horaInicio, String horaFin) {
        ArrayList<DatoGPS> filtrados = new ArrayList<>();
        DateTimeFormatter formato = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime inicio = LocalDateTime.parse(horaInicio, formato);
        LocalDateTime fin = LocalDateTime.parse(horaFin, formato);

        for (DatoGPS dato : datos) {
            if (dato.getIdAutobus().equals(idAutobus)) {
                LocalDateTime marca = LocalDateTime.parse(dato.getMarcaTiempo());
                if (!marca.isBefore(inicio) && !marca.isAfter(fin)) {
                    filtrados.add(dato);
                }
            }
        }
        return filtrados;
    }

    // Calcular la velocidad media por autobus
    public static void calcularVelocidadMedia(ArrayList<DatoGPS> datos) {
        HashMap<String, ArrayList<Double>> velocidades = new HashMap<>();

        for (DatoGPS dato : datos) {
            if (!validarRegistro(dato)) {
                continue;
            }

            velocidades.putIfAbsent(dato.getIdAutobus(), new ArrayList<>());
            velocidades.get(dato.getIdAutobus()).add(dato.getVelocidad());
        }

        for (String id : velocidades.keySet()) {
            ArrayList<Double> vel = velocidades.get(id);
            double suma = vel.stream().mapToDouble(Double::doubleValue).sum();
            double media = suma / vel.size();
            System.out.printf(Locale.US, "Autobus %s - velocidad: %.2f Km/h\n", id, media);
        }
    }

    // Detectar y contar las paradas por autobus
    public static void contarParadas(ArrayList<DatoGPS> datos) {
        HashMap<String, Integer> paradas = new HashMap<>();

        for (DatoGPS dato : datos) {
            if (!validarRegistro(dato)) {
                continue;
            }
            if (dato.getVelocidad() == 0) {
                paradas.put(dato.getIdAutobus(), paradas.getOrDefault(dato.getIdAutobus(), 0) + 1);
            }
        }

        for (String id : paradas.keySet()) {
            System.out.println("Autobus " + id + " - Paradas registradas: " + paradas.get(id));
        }
    }
}