package autoresindependientes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; 

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import autoresindependientes.AutorMenu.Autor;
import autoresindependientes.DataBase.Conexion;

public class Libro {

    private int id;
    private String titulo;
    private String descripcion;
    private double precio;
    private String genero; 
    private int cantidadPaginas;
    private Autor autor; 

    public Libro(int id, String titulo, String descripcion, double precio, String genero, int cantidadPaginas, Autor autor) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.genero = genero;
        this.cantidadPaginas = cantidadPaginas;
        this.autor = autor;
    }

   
    public Libro(String titulo, String descripcion, double precio, String genero, int cantidadPaginas, Autor autor) {
        this(0, titulo, descripcion, precio, genero, cantidadPaginas, autor); 
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public String getGenero() {
        return genero;
    }

    public int getCantidadPaginas() {
        return cantidadPaginas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(double precio) {
        if (precio >= 0) {
            this.precio = precio;
        } else {
            System.out.println("El precio debe ser positivo.");
        }
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setCantidadPaginas(int cantidadPaginas) {
        if (cantidadPaginas > 0) {
            this.cantidadPaginas = cantidadPaginas;
        } else {
            System.out.println("La cantidad de páginas debe ser positiva.");
        }
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public boolean guardarLibro() {
        String sql = "INSERT INTO libros (titulo, descripcion, precio, genero_libro, cantidad_paginas, id_autor) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = (Connection) Conexion.getInstance().getConnection();
             PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, this.titulo);
            pstmt.setString(2, this.descripcion);
            pstmt.setDouble(3, this.precio);
            pstmt.setString(4, this.genero); 
            pstmt.setInt(5, this.cantidadPaginas);
            pstmt.setInt(6, this.autor.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.id = generatedKeys.getInt(1); 
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al guardar el libro: " + e.getMessage());
        }
        return false;
    }

    public static List<Libro> obtenerTodosLosLibros() {
        List<Libro> listaLibros = new ArrayList<>();

        String sql = "SELECT l.*, u.id AS autor_id, u.nombre AS autor_nombre, u.email AS autor_email, u.password AS autor_password, u.genero_autor AS autor_genero FROM libros l JOIN usuarios u ON l.id_autor = u.id";

        try (Connection con = (Connection) Conexion.getInstance().getConnection();
             PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                String genero = rs.getString("genero_libro");
                int cantidadPaginas = rs.getInt("cantidad_paginas");

                int autorId = rs.getInt("autor_id");
                String autorNombre = rs.getString("autor_nombre");
                String autorEmail = rs.getString("autor_email");
                String autorPassword = rs.getString("autor_password");
                String autorGenero = rs.getString("autor_genero");
                Autor autor = new Autor(autorId, autorNombre, autorEmail, autorPassword, autorGenero);

                Libro libro = new Libro(id, titulo, descripcion, precio, genero, cantidadPaginas, autor);
                listaLibros.add(libro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al obtener todos los libros: " + e.getMessage());
        }
        return listaLibros;
    }

    @Override
    public String toString() {
        return "Libro [id=" + id + ", titulo=" + titulo + ", descripcion=" + descripcion + ", precio=" + precio
                + ", genero=" + genero + ", cantidadPaginas=" + cantidadPaginas + ", autor=" + (autor != null ? autor.getName() : "N/A") + "]";
    }
}