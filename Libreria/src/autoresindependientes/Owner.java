package autoresindependientes;

import javax.swing.JOptionPane;

import autoresindependientes.DataBase.Conexion;


public class Owner extends Usuario {

    public Owner(int id, String name, String email, String password) {
        super(id, name, email, password, "owner");
    }

    
    public boolean establecerPrecioYPublicarLibro(Propuesta propuesta, double precio) {
        if (propuesta == null) {
            JOptionPane.showMessageDialog(null, "La propuesta seleccionada no es válida.", "Error de Publicación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (propuesta.getEstado() != Propuesta.EstadoPropuesta.ACEPTADA) {
            JOptionPane.showMessageDialog(null, "Solo se pueden publicar propuestas con estado ACEPTADA.", "Error de Publicación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (precio <= 0) {
            JOptionPane.showMessageDialog(null, "El precio debe ser un número positivo.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Libro nuevoLibro = new Libro(
            propuesta.getTitulo(),
            propuesta.getDescripcion(),
            precio,
            propuesta.getGenero(),
            propuesta.getCantidadPaginas(),
            propuesta.getAutor() 
        );

        if (nuevoLibro.guardarLibro()) { 
            try {
                propuesta.setEstado(Propuesta.EstadoPropuesta.PUBLICADA);
                propuesta.guardarCambios(); 
                JOptionPane.showMessageDialog(null, "Libro '" + nuevoLibro.getTitulo() + "' publicado con éxito al precio de $" + String.format("%.2f", precio), "Publicación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Advertencia: El libro se publicó, pero hubo un error al actualizar el estado de la propuesta en la base de datos.", "Error de Actualización", JOptionPane.WARNING_MESSAGE);
                return false; 
            }
        } else {

            JOptionPane.showMessageDialog(null, "Error al publicar el libro. Por favor, inténtalo de nuevo.", "Error de Publicación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public int obtenerTotalLibrosVendidos() {
        int totalVentas = 0;

        String sql = "SELECT COUNT(*) AS total " +
                     "FROM cliente_libros_comprados";

        try (java.sql.Connection conn = Conexion.getInstance().getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalVentas = rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalVentas;
    }




    @Override
    public String toString() {
        return super.toString() + ", Rol=Owner";
    }
}