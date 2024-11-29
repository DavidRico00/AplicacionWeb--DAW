package Utilidad;

public class Direccion {

    public static final Direccion direccion = new Direccion();
    private final String HOST = "localhost";
    private final String redirect = "http://" + HOST + ":8080/PortalVentas";
    
    public static Direccion getInstance() {
        return direccion;
    }

    public String getRedirect() {
        return redirect;
    }

}
