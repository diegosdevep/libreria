package autoresindependientes;

import java.util.LinkedList;

public class Autor extends Usuario {

    private String generoAutor;
    private LinkedList<Propuesta> propuestas;

    public Autor(int id, String name, String email, String password, String generoAutor) {
        super(id, name, email, password, "autor");
        this.generoAutor = generoAutor;
        this.propuestas = new LinkedList<>();
    }

    public String getGeneroQueEscribe() {
        return generoAutor;
    }

    public void setGeneroQueEscribe(String generoQueEscribe) {
        this.generoAutor = generoQueEscribe;
    }

    public LinkedList<Propuesta> getPropuestas() {
        return propuestas;
    }

    public void cargarPropuesta(Propuesta propuesta) {
        // lógica aquí
    }

    @Override
    public String toString() {
        return super.toString() + ", Autor [género que escribe=" + generoAutor + "]";
    }
}