package autoresindependientes;

public class Editor extends Usuario {

    public Editor(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public String toString() {
        return super.toString() + ", Rol=Editor";
    }

    
    public void revisarPropuesta(Propuesta propuesta, String estado) {
        
    }

    public void categorizarPropuesta(Propuesta propuesta, String genero) {

    }
}
