package autoresindependientes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import Auth.Encriptador;
import autoresindependientes.AutorMenu.Autor;
import autoresindependientes.DataBase.Conexion;
import autoresindependientes.EditorMenu.Editor;

public abstract class Usuario implements Encriptador {

    private int id;
    private String nombre;
    private String email;
    private String password; 
    private String rol;

    public Usuario(int id, String nombre, String email, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return nombre; }
    public void setName(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; } 
    public void setPassword(String password) { this.password = password; } 

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", Nombre=" + nombre + ", email=" + email + ", password=" + "[HIDDEN]" + ", rol=" + rol + "]";
    }


    public static Usuario buscarUsuarioPorId(int id) {
        Usuario usuario = null;
        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM usuarios WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String password = rs.getString("password"); 
                String rol = rs.getString("rol");

                switch (rol) {
	                case "cliente":
	                    usuario = new Cliente(id, nombre, email, password);
	                    break;
                    case "autor":
                        String genero = rs.getString("genero_autor");
                        usuario = new Autor(id, nombre, email, password, genero);
                        break;
                    case "editor":
                        usuario = new Editor(id, nombre, email, password);
                        break;
                    case "owner":
                        usuario = new Owner(id, nombre, email, password);
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al buscar usuario por ID: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        return usuario;
    }


    public static Usuario login(String email, String password) {
        Usuario usuario = null;
        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(
                 "SELECT * FROM usuarios WHERE email = ? AND password = ?"
             )) {
            stmt.setString(1, email);
            stmt.setString(2, Encriptador.encriptar(password)); 

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String rol = rs.getString("rol");
                String passFromDB = rs.getString("password");

                switch (rol) {
	                case "cliente":
	                    usuario = new Cliente(id, nombre, email, passFromDB);
	                    break;
                    case "autor":
                        String genero = rs.getString("genero_autor");
                        usuario = new Autor(id, nombre, email, passFromDB, genero);
                        break;
                    case "editor":
                        usuario = new Editor(id, nombre, email, passFromDB);
                        break;
                    case "owner":
                        usuario = new Owner(id, nombre, email, passFromDB);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error de autenticación: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        return usuario;
    }


    public static void agregarUsuario(Usuario usuario) {
        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement statement = con.prepareStatement(
                 "INSERT INTO usuarios (nombre, email, password, rol, genero_autor) VALUES (?, ?, ?, ?, ?)"
             )) {
            statement.setString(1, usuario.getName());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, Encriptador.encriptar(usuario.getPassword())); 
            statement.setString(4, usuario.getRol());
            statement.setString(5, usuario instanceof Autor ? ((Autor) usuario).getGeneroQueEscribe() : null);

            int filas = statement.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Usuario agregado correctamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
            JOptionPane.showMessageDialog(null,
                                          "Error al agregar el usuario. Asegúrate de que el email no esté ya registrado.",
                                          "Error de Registro",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void RegistrarUsuario(Usuario nuevo) {
        LinkedList<Usuario> existentes = mostrarUsuarios(); 
        boolean flag = true;
        for (Usuario existente : existentes) {
            if (existente.getEmail().equals(nuevo.getEmail())) {
                flag = false;
                break;
            }
        }
        if (flag) {
            agregarUsuario(nuevo);
        } else {
            JOptionPane.showMessageDialog(null, "Usuario ya creado");
        }
    }


    public boolean actualizarUsuario(Usuario usuarioAActualizar, String newPassword) {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE usuarios SET nombre = ?, email = ?, rol = ?, genero_autor = ?");
        List<Object> params = new LinkedList<>();

        params.add(usuarioAActualizar.getName());
        params.add(usuarioAActualizar.getEmail());
        params.add(usuarioAActualizar.getRol());
        params.add(usuarioAActualizar instanceof Autor ? ((Autor) usuarioAActualizar).getGeneroQueEscribe() : null);

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            sqlBuilder.append(", password = ?");
            params.add(Encriptador.encriptar(newPassword));
        }

        sqlBuilder.append(" WHERE id = ?");
        params.add(usuarioAActualizar.getId());

        String sql = sqlBuilder.toString();

        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            int filas = statement.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Usuario actualizado correctamente.");
                if (newPassword != null && !newPassword.trim().isEmpty()) {
                    usuarioAActualizar.setPassword(Encriptador.encriptar(newPassword));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                                          "Error al actualizar el usuario. Asegúrate de que el email no esté ya registrado.",
                                          "Error de Actualización",
                                          JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }


    public static boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {

            statement.setInt(1, idUsuario);
            int filas = statement.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el usuario con ID: " + idUsuario, "Usuario no encontrado", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                                          "Error al eliminar el usuario. Es posible que esté asociado a otros datos en el sistema (como propuestas o libros).", 
                                          "Error de Eliminación", 
                                          JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }


    public static LinkedList<Usuario> mostrarUsuarios() {
        LinkedList<Usuario> usuarios = new LinkedList<>();
        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM usuarios");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("nombre");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String rol = rs.getString("rol");

                switch (rol) {
	                case "cliente":
	                    usuarios.add(new Cliente(id, name, email, password));
	                    break;

                    case "autor":
                        String genero = rs.getString("genero_autor");
                        usuarios.add(new Autor(id, name, email, password, genero));
                        break;
                    case "editor":
                        usuarios.add(new Editor(id, name, email, password));
                        break;
                    case "owner":
                        usuarios.add(new Owner(id, name, email, password));
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar usuarios: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        return usuarios;
    }

}