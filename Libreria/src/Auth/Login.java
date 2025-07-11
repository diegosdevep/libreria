package Auth;

import javax.swing.*;

import autoresindependientes.Cliente;
import autoresindependientes.Owner;
import autoresindependientes.Usuario;
import autoresindependientes.AutorMenu.Autor;
import autoresindependientes.AutorMenu.MenuAutor;
import autoresindependientes.ClienteMenu.MenuCliente;
import autoresindependientes.EditorMenu.Editor;
import autoresindependientes.EditorMenu.EditorMenu;
import autoresindependientes.OwnerMenu.MenuOwner;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Login extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;

    public Login() {
        setTitle("Login - Autores Independientes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 577, 417);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);


        ImageIcon icon = new ImageIcon(getClass().getResource("/imagenes/logolibro.png"));
        Image img = icon.getImage().getScaledInstance(260, 190, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        JLabel labelImagen = new JLabel(icon);
        labelImagen.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        labelImagen.setBounds(150, 20, 260, 100);
        getContentPane().add(labelImagen);


        JLabel lblUsuario = new JLabel("Email:");
        lblUsuario.setBounds(76, 128, 120, 25);
        getContentPane().add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(190, 122, 261, 37);
        getContentPane().add(txtUsuario);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(76, 187, 120, 25);
        getContentPane().add(lblContrasena);

        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(190, 181, 261, 37);
        getContentPane().add(txtContrasena);

        JButton btnLogin = new JButton("Ingresar");
        btnLogin.setBounds(190, 240, 261, 48);
        getContentPane().add(btnLogin);

        JButton btnRegister = new JButton("Regístrate");
        btnRegister.setBounds(190, 300, 261, 48);
        getContentPane().add(btnRegister);

        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Login.this.setVisible(false); 
                new Registro(Login.this).setVisible(true); 
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = txtUsuario.getText();
                String contrasena = String.valueOf(txtContrasena.getPassword());

                Usuario usuario = Usuario.login(email, contrasena);

                if (usuario == null || usuario.getEmail() == null) {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(null, "Bienvenido " + usuario.getName() + " (" + usuario.getRol() + ")", "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
                dispose(); 

                switch (usuario.getRol()) {
                    case "cliente":
                        new MenuCliente((Cliente) usuario).setVisible(true);
                        break;
                    case "editor":
                        new EditorMenu((Editor) usuario).setVisible(true);
                        break;
                    case "autor":
                        new MenuAutor((Autor) usuario).setVisible(true);
                        break;
                    case "owner":
                        new MenuOwner((Owner) usuario).setVisible(true);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Rol de usuario no reconocido. Contacte al administrador.", "Error de Rol", JOptionPane.ERROR_MESSAGE);
                        new Login().setVisible(true);
                }
            }
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Login frame = new Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}