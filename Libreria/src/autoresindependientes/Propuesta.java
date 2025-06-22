package autoresindependientes;

import java.io.*;
import java.sql.*;
import java.util.*;

import autoresindependientes.AutorMenu.Autor;
import autoresindependientes.DataBase.Conexion;

public class Propuesta {

    private int id;
    private String titulo;
    private String descripcion;
    private EstadoPropuesta estado;
    private String genero;
    private int cantidadPaginas;
    private Autor autor;
    private byte[] archivoPDF;

    public enum EstadoPropuesta {
        EN_REVISION, ACEPTADA, RECHAZADA,
        PUBLICADA 
    }

    public Propuesta(String titulo, String descripcion, String genero, int cantidadPaginas, Autor autor, byte[] archivoPDF) {
        this(0, titulo, descripcion, genero, cantidadPaginas, autor, archivoPDF); 
    }

    public Propuesta(int id, String titulo, String descripcion, String genero, int cantidadPaginas, Autor autor, byte[] archivoPDF) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.genero = genero;
        this.cantidadPaginas = cantidadPaginas;
        this.autor = autor;
        this.estado = EstadoPropuesta.EN_REVISION; 
        this.archivoPDF = archivoPDF;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public EstadoPropuesta getEstado() { return estado; }
    public String getGenero() { return genero; }
    public int getCantidadPaginas() { return cantidadPaginas; }
    public Autor getAutor() { return autor; }
    public byte[] getArchivoPDF() { return archivoPDF; }

    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setEstado(EstadoPropuesta estado) { this.estado = estado; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setCantidadPaginas(int cantidadPaginas) {
        if (cantidadPaginas > 0) {
            this.cantidadPaginas = cantidadPaginas;
        } else {
            System.out.println("La cantidad de páginas debe ser positiva.");
        }
    }
    public void setAutor(Autor autor) { this.autor = autor; }
    public void setArchivoPDF(byte[] archivoPDF) { this.archivoPDF = archivoPDF; }

    @Override
    public String toString() {
        return "Propuesta [id=" + id + ", titulo=" + titulo + ", descripcion=" + descripcion +
               ", estado=" + estado + ", genero=" + genero + ", cantidadPaginas=" + cantidadPaginas +
               ", autor=" + (autor != null ? autor.getName() : "N/A") + "]";
    }


    public boolean guardarCambios() {
        String sql = "UPDATE propuestas SET estado = ? WHERE id = ?";
        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement stmt = (PreparedStatement) con.prepareStatement(sql)) {
            stmt.setString(1, this.estado.name()); 
            stmt.setInt(2, this.id);            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al actualizar el estado de la propuesta " + this.id + ": " + e.getMessage());
            return false;
        }
    }

    public void guardarPdf(String rutaArchivo) throws IOException {
        if (archivoPDF == null) {
            throw new IOException("No hay archivo PDF asociado a esta propuesta.");
        }
        try (FileOutputStream fos = new FileOutputStream(rutaArchivo)) {
            fos.write(archivoPDF);
        }
    }

    public static List<Propuesta> obtenerTodasLasPropuestas() {
        List<Propuesta> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstance().getConnection()) {
            PreparedStatement stmt = (PreparedStatement) con.prepareStatement("SELECT p.*, u.id AS autor_id, u.nombre AS autor_nombre, u.email AS autor_email, u.password AS autor_password, u.genero_autor AS autor_genero FROM propuestas p JOIN usuarios u ON p.id_autor = u.id");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String descripcion = rs.getString("descripcion");
                String genero = rs.getString("genero_propuesta");
                int paginas = rs.getInt("cantidad_paginas");
                String estadoStr = rs.getString("estado");
                byte[] pdf = rs.getBytes("archivo_pdf");

                int autorId = rs.getInt("autor_id");
                String autorNombre = rs.getString("autor_nombre");
                String autorEmail = rs.getString("autor_email");
                String autorPassword = rs.getString("autor_password");
                String autorGenero = rs.getString("autor_genero");
                Autor autor = new Autor(autorId, autorNombre, autorEmail, autorPassword, autorGenero);

                Propuesta.EstadoPropuesta estado = Propuesta.EstadoPropuesta.valueOf(estadoStr);

                Propuesta p = new Propuesta(id, titulo, descripcion, genero, paginas, autor, pdf);
                p.setEstado(estado);
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al obtener todas las propuestas: " + e.getMessage());
        }
        return lista;
    }

    public static Propuesta buscarPorId(int idBuscado) {
        try (Connection con = Conexion.getInstance().getConnection()) {
            PreparedStatement stmt = (PreparedStatement) con.prepareStatement("SELECT p.*, u.id AS autor_id, u.nombre AS autor_nombre, u.email AS autor_email, u.password AS autor_password, u.genero_autor AS autor_genero FROM propuestas p JOIN usuarios u ON p.id_autor = u.id WHERE p.id = ?");
            stmt.setInt(1, idBuscado);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String descripcion = rs.getString("descripcion");
                String genero = rs.getString("genero_propuesta");
                int paginas = rs.getInt("cantidad_paginas");
                String estadoStr = rs.getString("estado");
                byte[] pdf = rs.getBytes("archivo_pdf");

                int autorId = rs.getInt("autor_id");
                String autorNombre = rs.getString("autor_nombre");
                String autorEmail = rs.getString("autor_email");
                String autorPassword = rs.getString("autor_password");
                String autorGenero = rs.getString("autor_genero");
                Autor autor = new Autor(autorId, autorNombre, autorEmail, autorPassword, autorGenero);

                Propuesta p = new Propuesta(id, titulo, descripcion, genero, paginas, autor, pdf);
                p.setEstado(EstadoPropuesta.valueOf(estadoStr));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Propuesta> obtenerEnRevision() {
        List<Propuesta> enRevision = new ArrayList<>();
        try (Connection con = Conexion.getInstance().getConnection()) {
            PreparedStatement stmt = (PreparedStatement) con.prepareStatement("SELECT p.*, u.id AS autor_id, u.nombre AS autor_nombre, u.email AS autor_email, u.password AS autor_password, u.genero_autor AS autor_genero FROM propuestas p JOIN usuarios u ON p.id_autor = u.id WHERE p.estado = ?");
            stmt.setString(1, EstadoPropuesta.EN_REVISION.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String descripcion = rs.getString("descripcion");
                String genero = rs.getString("genero_propuesta");
                int paginas = rs.getInt("cantidad_paginas");
                String estadoStr = rs.getString("estado");
                byte[] pdf = rs.getBytes("archivo_pdf");

                int autorId = rs.getInt("autor_id");
                String autorNombre = rs.getString("autor_nombre");
                String autorEmail = rs.getString("autor_email");
                String autorPassword = rs.getString("autor_password");
                String autorGenero = rs.getString("autor_genero");
                Autor autor = new Autor(autorId, autorNombre, autorEmail, autorPassword, autorGenero);

                Propuesta p = new Propuesta(id, titulo, descripcion, genero, paginas, autor, pdf);
                p.setEstado(EstadoPropuesta.valueOf(estadoStr));
                enRevision.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enRevision;
    }

    public static List<Propuesta> obtenerPropuestasAceptadasNoPublicadas() {
        List<Propuesta> aceptadasNoPublicadas = new ArrayList<>();
        try (Connection con = Conexion.getInstance().getConnection()) {
            PreparedStatement stmt = (PreparedStatement) con.prepareStatement(
                "SELECT p.*, u.id AS autor_id, u.nombre AS autor_nombre, u.email AS autor_email, u.password AS autor_password, u.genero_autor AS autor_genero " +
                "FROM propuestas p JOIN usuarios u ON p.id_autor = u.id " +
                "WHERE p.estado = ? AND p.estado != ?" 
            );
            stmt.setString(1, EstadoPropuesta.ACEPTADA.name());
            stmt.setString(2, EstadoPropuesta.PUBLICADA.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String descripcion = rs.getString("descripcion");
                String genero = rs.getString("genero_propuesta");
                int paginas = rs.getInt("cantidad_paginas");
                byte[] pdf = rs.getBytes("archivo_pdf");
                String estadoStr = rs.getString("estado");

                int autorId = rs.getInt("autor_id");
                String autorNombre = rs.getString("autor_nombre");
                String autorEmail = rs.getString("autor_email");
                String autorPassword = rs.getString("autor_password");
                String autorGenero = rs.getString("autor_genero");
                Autor autor = new Autor(autorId, autorNombre, autorEmail, autorPassword, autorGenero);

                Propuesta p = new Propuesta(id, titulo, descripcion, genero, paginas, autor, pdf);
                p.setEstado(EstadoPropuesta.valueOf(estadoStr));
                aceptadasNoPublicadas.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al obtener propuestas aceptadas y no publicadas: " + e.getMessage());
        }
        return aceptadasNoPublicadas;
    }

    public static List<Propuesta> obtenerPropuestasAceptadas() {
        List<Propuesta> aceptadas = new ArrayList<>();
        try (Connection con = Conexion.getInstance().getConnection()) {
            PreparedStatement stmt = (PreparedStatement) con.prepareStatement(
                "SELECT p.*, u.id AS autor_id, u.nombre AS autor_nombre, u.email AS autor_email, u.password AS autor_password, u.genero_autor AS autor_genero " +
                "FROM propuestas p JOIN usuarios u ON p.id_autor = u.id " +
                "WHERE p.estado = ?"
            );
            stmt.setString(1, EstadoPropuesta.ACEPTADA.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String descripcion = rs.getString("descripcion");
                String genero = rs.getString("genero_propuesta");
                int paginas = rs.getInt("cantidad_paginas");
                byte[] pdf = rs.getBytes("archivo_pdf");

                int autorId = rs.getInt("autor_id");
                String autorNombre = rs.getString("autor_nombre");
                String autorEmail = rs.getString("autor_email");
                String autorPassword = rs.getString("autor_password");
                String autorGenero = rs.getString("autor_genero");
                Autor autor = new Autor(autorId, autorNombre, autorEmail, autorPassword, autorGenero);

                Propuesta p = new Propuesta(id, titulo, descripcion, genero, paginas, autor, pdf);
                p.setEstado(EstadoPropuesta.ACEPTADA);
                aceptadas.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al obtener propuestas aceptadas: " + e.getMessage());
        }
        return aceptadas;
    }


    public static boolean rechazarPropuesta(int propuestaId) {
        try (Connection con = Conexion.getInstance().getConnection()) {
            PreparedStatement stmt = (PreparedStatement) con.prepareStatement("UPDATE propuestas SET estado = ? WHERE id = ?");
            stmt.setString(1, EstadoPropuesta.RECHAZADA.name());
            stmt.setInt(2, propuestaId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean aceptarPropuesta(int propuestaId) {
        try (Connection con = Conexion.getInstance().getConnection()) {
            PreparedStatement stmt = (PreparedStatement) con.prepareStatement("UPDATE propuestas SET estado = ? WHERE id = ?");
            stmt.setString(1, EstadoPropuesta.ACEPTADA.name());
            stmt.setInt(2, propuestaId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}