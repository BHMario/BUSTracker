import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;

public class MapaRecorrido {

    public static void mostrarMapa(String idAutobus, Component parent) {
        ArrayList<DatoGPS> datos = LectorDatosGPS.leerDatosGPS("src/datos_gps.csv");

        StringBuilder coordenadas = new StringBuilder();
        for (DatoGPS d : datos) {
            if (d.getIdAutobus().equalsIgnoreCase(idAutobus)) {
                coordenadas.append("[").append(d.getLatitud()).append(", ").append(d.getLongitud()).append("],\n");
            }
        }

        if (coordenadas.length() == 0) {
            JOptionPane.showMessageDialog(parent, "No se encontraron datos para " + idAutobus);
            return;
        }

        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="utf-8" />
                    <title>Mapa Autobús</title>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css" />
                    <script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>
                    <style>
                        #map { width: 100%%; height: 100vh; margin: 0; padding: 0; }
                        html, body { margin: 0; padding: 0; }
                    </style>
                </head>
                <body>
                <div id="map"></div>
                <script>
                    var coordenadas = [
                        %s
                    ];

                    var map = L.map('map').setView(coordenadas[0], 15);

                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        maxZoom: 19,
                        attribution: '© OpenStreetMap'
                    }).addTo(map);

                    var marker = L.marker(coordenadas[0]).addTo(map);
                    var polyline = L.polyline(coordenadas, {color: 'gold'}).addTo(map);

                    let i = 0;
                    function mover() {
                        if (i >= coordenadas.length) return;
                        marker.setLatLng(coordenadas[i]);
                        map.panTo(coordenadas[i]);
                        i++;
                        setTimeout(mover, 500);
                    }

                    mover();
                </script>
                </body>
                </html>
                """.formatted(coordenadas.toString());

        try {
            File archivoHTML = File.createTempFile("mapa_" + idAutobus.toLowerCase(), ".html");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoHTML))) {
                writer.write(html);
            }

            Desktop.getDesktop().browse(new URI("file:///" + archivoHTML.getAbsolutePath().replace("\\", "/")));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Error al mostrar el mapa: " + e.getMessage());
        }
    }
}
