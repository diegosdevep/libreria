package autoresindependientes;

public abstract class Usuario {

	private int id;
	private String name;
	private String email;
	private String password;

	public Usuario(int id, String name, String email, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", name=" + name + ", email=" + email + "]";
	}

	public void subirPropuesta(Propuesta propuesta) {

	}

	public void verEstadoPropuesta(int idPropuesta) {

	}

}
