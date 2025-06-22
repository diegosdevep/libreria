package autoresindependientes.EditorMenu;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JOptionPane;

import autoresindependientes.Propuesta;
import autoresindependientes.Usuario;
import autoresindependientes.DataBase.Conexion;

public class Editor extends Usuario {

    public Editor(int id, String name, String email, String password) {
        super(id, name, email, password, "editor");
    }

    public void revisarPropuesta(Propuesta propuesta, Propuesta.EstadoPropuesta nuevoEstado) {
        try (Connection con = Conexion.getInstance().getConnection()) {
            PreparedStatement stmt = con.prepareStatement(
                "UPDATE propuestas SET estado = ? WHERE id = ?"
            );
            stmt.setString(1, nuevoEstado.name());
            stmt.setInt(2, propuesta.getId());

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                propuesta.setEstado(nuevoEstado);
                JOptionPane.showMessageDialog(null, "Estado actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró la propuesta.");
            }

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el estado.");
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", Rol=Editor";
    }
}
