package autoresindependientes;

public class Editor extends Usuario {

    public Editor(int id, String name, String email, String password) {
        super(id, name, email, password, "editor");
    }

    public void revisarPropuesta(Propuesta propuesta, String estado) {
        // lógica aquí
    }

    public void categorizarPropuesta(Propuesta propuesta, String genero) {
        // lógica aquí
    }

    @Override
    public String toString() {
        return super.toString() + ", Rol=Editor";
    }
}