package autoresindependientes;

public class Owner extends Usuario {

    public Owner(int id, String name, String email, String password) {
        super(id, name, email, password, "owner");
    }

    public void modificarPrecioLibro(int idLibro, double nuevoPrecio) {
        // lógica aquí
    }

    @Override
    public String toString() {
        return super.toString() + ", Rol=Owner";
    }
}