package autoresindependientes.AutorMenu;

import java.util.ArrayList;
import java.util.List;

import autoresindependientes.Propuesta;
import autoresindependientes.Usuario;
import autoresindependientes.DataBase.Conexion;

public class Autor extends Usuario {

    private String generoAutor;

    public Autor(int id, String name, String email, String password, String generoAutor) {
        super(id, name, email, password, "autor");
        this.generoAutor = generoAutor;
    }

    public String getGeneroQueEscribe() {
        return generoAutor;
    }

    public void setGeneroQueEscribe(String generoQueEscribe) {
        this.generoAutor = generoQueEscribe;
    }

    public void guardarPropuestaEnBD(Propuesta propuesta) {
        String sql = "INSERT INTO propuestas (titulo, descripcion, genero_propuesta, cantidad_paginas, estado, id_autor, archivo_pdf) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (java.sql.Connection conn = Conexion.getInstance().getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, propuesta.getTitulo());
            stmt.setString(2, propuesta.getDescripcion());
            stmt.setString(3, propuesta.getGenero());
            stmt.setInt(4, propuesta.getCantidadPaginas());
            stmt.setString(5, propuesta.getEstado().name());
            stmt.setInt(6, this.getId());
            stmt.setBytes(7, propuesta.getArchivoPDF());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Propuesta> obtenerPropuestasDesdeBD() {
        List<Propuesta> propuestas = new ArrayList<>();

        String sql = "SELECT id, titulo, descripcion, genero_propuesta, cantidad_paginas, estado, archivo_pdf " +
                     "FROM propuestas WHERE id_autor = ?";

        try (java.sql.Connection conn = Conexion.getInstance().getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, this.getId());

            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String titulo = rs.getString("titulo");
                    String descripcion = rs.getString("descripcion");
                    String genero = rs.getString("genero_propuesta");
                    int paginas = rs.getInt("cantidad_paginas");
                    String estadoStr = rs.getString("estado");
                    byte[] archivoPDF = rs.getBytes("archivo_pdf");

                    Propuesta.EstadoPropuesta estado = Propuesta.EstadoPropuesta.valueOf(estadoStr);

                    Propuesta p = new Propuesta(id, titulo, descripcion, genero, paginas, this, archivoPDF);
                    p.setEstado(estado);

                    propuestas.add(p);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return propuestas;
    }

    public static Autor obtenerAutorPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ? AND rol = 'autor'";

        try (java.sql.Connection conn = Conexion.getInstance().getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    String generoAutor = rs.getString("genero_autor");

                    return new Autor(id, nombre, email, password, generoAutor);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public String toString() {
        return super.toString() + ", Autor [género que escribe=" + generoAutor + "]";
    }
}
