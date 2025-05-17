package autoresindependientes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

import javax.swing.JOptionPane;

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
        return "Usuario [id=" + id + ", Nombre=" + nombre + ", email=" + email + ", password=" + password + ", rol=" + rol + "]";
    }

    private static Connection con = Conexion.getInstance().getConnection();

    public static Usuario login(String email, String password) {
    	Usuario usuario = null;
    	try {
            PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM usuarios WHERE email = ? AND password = ?"
            );
            stmt.setString(1, email);
            stmt.setString(2, Encriptador.encriptar(password));



            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String rol = rs.getString("rol");
                String pass = rs.getString("password");

                switch (rol) {
                    case "autor":
                        String genero = rs.getString("genero_autor");
                        usuario = new Autor(id, nombre, email, pass, genero);
                        break;
                    case "editor":
                        usuario = new Editor(id, nombre, email, pass);
                        break;
                    case "owner":
                        usuario = new Owner(id, nombre, email, pass);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public static void agregarUsuario(Usuario usuario) {
        try {
            PreparedStatement statement = con.prepareStatement(
                "INSERT INTO usuarios (nombre, email, password, rol, genero_autor) VALUES (?, ?, ?, ?, ?)"
            );
            statement.setString(1, usuario.getName());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, Encriptador.encriptar(usuario.getPassword()));
            statement.setString(4, usuario.getRol());
            statement.setString(5, usuario instanceof Autor ? ((Autor) usuario).getGeneroQueEscribe() : null);

            int filas = statement.executeUpdate();
            if (filas > 0) {
                System.out.println("Usuario agregado correctamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public static LinkedList<Usuario> mostrarUsuarios() {
        LinkedList<Usuario> usuarios = new LinkedList<>();
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM usuarios");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("nombre");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String rol = rs.getString("rol");

                switch (rol) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuarios;
    }
}