public class DatoGPS {
    private String idAutobus;   // Identificador del autobus
    private String marcaTiempo; // En formato ISO 8601
    private double latitud;     // Latitud geográfica del autobus
    private double longitud;    // Altitud geográfica del autobus
    private double velocidad;   // Velocidad del autobus en km/h

    public DatoGPS(String idAutobus, String marcaTiempo, double latitud, double longitud, double velocidad) {
        this.idAutobus = idAutobus;
        this.marcaTiempo = marcaTiempo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.velocidad = velocidad;
    }

    public String getIdAutobus() {
        return idAutobus;
    }

    public String getMarcaTiempo() {
        return marcaTiempo;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setLatitud(double nuevaLatitud) {
        this.latitud = nuevaLatitud;
    }

    public void setLongitud(double nuevaLongitud) {
        this.longitud = nuevaLongitud;
    }


    @Override
    public String toString() {
        return idAutobus + "," + marcaTiempo + "," + latitud + "," + longitud + "," + velocidad;
    }
}
