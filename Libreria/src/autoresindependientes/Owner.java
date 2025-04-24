package autoresindependientes;

public class Owner extends Usuario {

	public Owner(int id, String nombre, String email, String password) {
		super(id, nombre, email, password);
	}

	// @Override
	public String toString() {
		return super.toString() + ", Rol=Owner";
	}

	public void modificarPrecioLibro(int idLibro, double nuevoPrecio) {

	}
}
