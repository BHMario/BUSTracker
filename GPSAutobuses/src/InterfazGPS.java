import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InterfazGPS extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private final String[] buses = {"BUS01", "BUS02", "BUS03"};

    public InterfazGPS() {
        setTitle("GPS Autobuses");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.BLACK);

        addMenuPrincipal();
        addSelectorBus();
        addEstadisticas();
        mostrarSelectorCambioRecorrido(); // precargado

        add(mainPanel);
        setVisible(true);
    }

    private void addMenuPrincipal() {
        JPanel panel = crearPanelConFondo();
        panel.add(Box.createVerticalGlue());

        JLabel titulo = crearTitulo("Menú Principal");
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));

        panel.add(crearBoton("Generar Datos de Autobuses", e -> {
            GeneradorDatosGPS.main(null);
            mostrarMensaje("Datos generados correctamente.");
        }));
        panel.add(Box.createVerticalStrut(10));

        panel.add(crearBoton("Ver Recorrido de Autobuses", e -> cardLayout.show(mainPanel, "selectorBus")));
        panel.add(Box.createVerticalStrut(10));

        panel.add(crearBoton("Ver Estadísticas", e -> {
            actualizarEstadisticas();
            cardLayout.show(mainPanel, "estadisticas");
        }));
        panel.add(Box.createVerticalStrut(10));

        panel.add(crearBoton("Simular Cambio de Recorrido", e -> cardLayout.show(mainPanel, "cambioRecorrido")));
        panel.add(Box.createVerticalStrut(10));

        panel.add(crearBoton("Exportar Última Posición (JSON)", e -> {
            ArrayList<DatoGPS> datos = LectorDatosGPS.leerDatosGPS("src/datos_gps.csv");
            ExportadorJSON.exportarUltimasPosiciones(datos);
            mostrarMensaje("Exportación completada.");
        }));
        panel.add(Box.createVerticalStrut(10));

        panel.add(crearBoton("Salir", e -> System.exit(0)));
        panel.add(Box.createVerticalGlue());

        mainPanel.add(panel, "menu");
    }

    private void addSelectorBus() {
        JPanel panel = crearPanelConFondo();
        panel.add(Box.createVerticalGlue());

        JLabel titulo = crearTitulo("Seleccione un Autobús");
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));

        for (String bus : buses) {
            JButton boton = crearBoton(bus, e -> {
                mostrarRecorrido(bus);
            });
            panel.add(boton);
            panel.add(Box.createVerticalStrut(10));
        }

        panel.add(crearBoton("Volver", e -> cardLayout.show(mainPanel, "menu")));
        panel.add(Box.createVerticalGlue());

        mainPanel.add(panel, "selectorBus");
    }

    private void mostrarSelectorCambioRecorrido() {
        JPanel panel = crearPanelConFondo();
        panel.add(Box.createVerticalGlue());

        JLabel titulo = crearTitulo("Selecciona un autobús para cambiar su recorrido");
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));

        for (String bus : buses) {
            JButton boton = crearBoton(bus, e -> {
                SimuladorCambio.modificarRecorrido("src/datos_gps.csv", bus);
                mostrarMensaje("Recorrido de " + bus + " modificado con éxito.");
                cardLayout.show(mainPanel, "menu");
            });
            panel.add(boton);
            panel.add(Box.createVerticalStrut(10));
        }

        panel.add(crearBoton("Volver", e -> cardLayout.show(mainPanel, "menu")));
        panel.add(Box.createVerticalGlue());

        mainPanel.add(panel, "cambioRecorrido");
    }

    private void mostrarRecorrido(String busId) {
        MapaRecorrido.mostrarMapa(busId, this);
    }

    private void addEstadisticas() {
        JPanel panel = crearPanelConFondo();
        panel.setName("estadisticas");
        panel.add(Box.createVerticalGlue());

        JLabel titulo = crearTitulo("Estadísticas por Autobús");
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));

        JTextArea area = new JTextArea(25, 80);
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setBackground(Color.DARK_GRAY);
        area.setForeground(Color.WHITE);
        area.setEditable(false);

        JScrollPane scroll = new JScrollPane(area);
        panel.add(scroll);
        panel.add(Box.createVerticalStrut(20));

        JButton btnVolver = crearBoton("Volver", e -> cardLayout.show(mainPanel, "menu"));
        panel.add(btnVolver);
        panel.add(Box.createVerticalGlue());

        panel.putClientProperty("areaEstadisticas", area);
        mainPanel.add(panel, "estadisticas");
    }

    private void actualizarEstadisticas() {
        JPanel panel = (JPanel) mainPanel.getComponent(2); // "estadisticas"
        JTextArea area = (JTextArea) panel.getClientProperty("areaEstadisticas");
        area.setText("");

        ArrayList<DatoGPS> datos = LectorDatosGPS.leerDatosGPS("src/datos_gps.csv");

        for (String bus : buses) {
            ArrayList<DatoGPS> datosBus = new ArrayList<>();
            for (DatoGPS d : datos) {
                if (d.getIdAutobus().equals(bus)) {
                    datosBus.add(d);
                }
            }

            double suma = 0;
            int paradas = 0;
            for (DatoGPS d : datosBus) {
                suma += d.getVelocidad();
                if (d.getVelocidad() == 0) paradas++;
            }

            double media = datosBus.isEmpty() ? 0 : suma / datosBus.size();

            area.append("▶ " + bus + "\n");
            area.append("  Velocidad media: " + String.format("%.2f", media) + " km/h\n");
            area.append("  Paradas: " + paradas + "\n\n");
        }
    }

    private JPanel crearPanelConFondo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        return panel;
    }

    private JLabel crearTitulo(String texto) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setForeground(new Color(255, 215, 0));
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JButton crearBoton(String texto, java.awt.event.ActionListener listener) {
        JButton boton = new JButton(texto);
        boton.addActionListener(listener);
        estilizarBoton(boton);
        return boton;
    }

    private void estilizarBoton(JButton boton) {
        boton.setForeground(new Color(255, 215, 0));
        boton.setBackground(Color.BLACK);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Arial", Font.PLAIN, 16));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
        boton.setPreferredSize(new Dimension(320, 50));
        boton.setMaximumSize(new Dimension(320, 50));
        boton.setMinimumSize(new Dimension(320, 50));
        boton.setMargin(new Insets(10, 20, 10, 20));
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InterfazGPS::new);
    }
}
