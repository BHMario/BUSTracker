import java.util.ArrayList;

public class Principal {
    public static void main(String[] args) {
        // Leer los datos desde el archivo CSV
        ArrayList<DatoGPS> datos = LectorDatosGPS.leerDatosGPS("src/datos_gps.csv");

        // Verificar si los datos fueron cargados correctamente
        if (datos.isEmpty()) {
            System.out.println("No se encontraron datos en el archivo.");
            return;
        } else {
            System.out.println("Datos cargados: " + datos.size() + " registros\n");
        }

        // Mostrar velocidad media por autobús
        System.out.println("== Velocidad media por autobús ==");
        ProcesadorDatosGPS.calcularVelocidadMedia(datos);

        // Mostrar número de paradas
        System.out.println("\n== Paradas detectadas por autobús ==");
        ProcesadorDatosGPS.contarParadas(datos);

        // Filtrar datos por autobús y rango horario (por ejemplo: BUS01 entre 08:00 y 08:30)
        System.out.println("\n== Datos filtrados de BUS01 entre 08:00 y 08:30 ==");
        ArrayList<DatoGPS> filtrados = ProcesadorDatosGPS.filtrarPorBusYRango(datos, "BUS01", "2025-03-25T08:00:00", "2025-03-25T08:30:00");

        for (DatoGPS dato : filtrados) {
            System.out.println(dato);  // Este toString muestra los datos como CSV
        }

        System.out.println("\n== Exportando últimas posiciones en JSON ==");
        ExportadorJSON.exportarUltimasPosiciones(datos);

    }
}