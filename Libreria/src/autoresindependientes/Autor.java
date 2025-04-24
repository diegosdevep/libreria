package autoresindependientes;

import java.util.LinkedList;
import javax.swing.JOptionPane;

public class Autor extends Usuario {

	private String generoQueEscribe;
	private LinkedList<Propuesta> propuestas;

	public Autor(int id, String name, String email, String password, String genero) {
		super(id, name, email, password);
		this.generoQueEscribe = genero;
	}

	public String getGeneroQueEscribe() {
		return generoQueEscribe;
	}

	public void setGeneroQueEscribe(String generoQueEscribe) {
		this.generoQueEscribe = generoQueEscribe;
	}

	public void cargarPropuesta(Propuesta propuesta) {

	}

	public LinkedList<Propuesta> getPropuestas() {
		return propuestas;
	}

	@Override
	public String toString() {
		return super.toString() + ", Autor [género que escribe=" + generoQueEscribe + ", propuestas=" + propuestas
				+ "]";
	}

}
