package autoresindependientes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import autoresindependientes.DataBase.Conexion;

public class Cliente extends Usuario {

    private List<Libro> librosCompradosCache;

    public Cliente(int id, String nombre, String email, String password) {
        super(id, nombre, email, password, "cliente");
        this.librosCompradosCache = new ArrayList<>();
        cargarLibrosCompradosDesdeDB();
    }

    private void cargarLibrosCompradosDesdeDB() {
        this.librosCompradosCache.clear();
        String sql = "SELECT libro_id FROM cliente_libros_comprados WHERE cliente_id = ?";

        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, this.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int libroId = rs.getInt("libro_id");
                Libro libro = Libro.obtenerPorId(libroId);
                if (libro != null) {
                    this.librosCompradosCache.add(libro);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar libros comprados del cliente: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean comprarLibro(Libro libro) {
        if (yaTieneLibroCompradoEnDB(libro.getId())) {
            JOptionPane.showMessageDialog(null, "Ya tienes este libro en tu colección.", "Compra Fallida", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String sql = "INSERT INTO cliente_libros_comprados (cliente_id, libro_id, fecha_compra) VALUES (?, ?, NOW())";
        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, this.getId());
            stmt.setInt(2, libro.getId());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                this.librosCompradosCache.add(libro);
                System.out.println("Compra exitosa: " + libro.getTitulo() + " (guardado en DB)");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo registrar la compra en la base de datos.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al intentar comprar y guardar el libro: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean yaTieneLibroCompradoEnDB(int libroId) {
        String sql = "SELECT COUNT(*) FROM cliente_libros_comprados WHERE cliente_id = ? AND libro_id = ?";
        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, this.getId());
            stmt.setInt(2, libroId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Libro> getLibrosComprados() {
        cargarLibrosCompradosDesdeDB();
        return new ArrayList<>(librosCompradosCache);
    }

    public void mostrarLibrosComprados() {
        List<Libro> comprados = getLibrosComprados();
        if (comprados.isEmpty()) {
            System.out.println("No has comprado ningún libro aún.");
        } else {
            System.out.println("Libros comprados:");
            for (Libro libro : comprados) {
                System.out.println("- " + libro.getTitulo() + " (ID: " + libro.getId() + ")");
            }
        }
    }
}