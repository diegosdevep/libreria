package Auth;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import autoresindependientes.Owner;
import autoresindependientes.Usuario;
import autoresindependientes.AutorMenu.Autor;
import autoresindependientes.EditorMenu.Editor;

@SuppressWarnings("serial")
public class Registro extends JFrame {

    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRol;
    @SuppressWarnings("unused")
	private List<String> generosDisponibles = List.of(
            "Ficción", "No Ficción", "Misterio", "Romance", "Ciencia Ficción", "Fantasía",
            "Terror", "Histórico", "Aventura", "Drama", "Thriller", "Biografía"
    );

    private Login loginFrame;

    public Registro(Login loginFrame) {
        this.loginFrame = loginFrame;

        setTitle("Registro de Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 577, 480); 
        setLocationRelativeTo(null);

        getContentPane().setLayout(null);

        ImageIcon icon = new ImageIcon(getClass().getResource("/imagenes/logolibro.png"));
        Image img = icon.getImage().getScaledInstance(260, 190, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        JLabel labelImagen = new JLabel(icon);
        labelImagen.setBounds(259, 20, 60, 60);
        getContentPane().add(labelImagen);

        JLabel lblTitulo = new JLabel("Crea una nueva cuenta");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(76, 90, 400, 25);
        getContentPane().add(lblTitulo);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(76, 130, 80, 25);
        getContentPane().add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(157, 124, 261, 37);
        getContentPane().add(txtNombre);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(76, 175, 80, 25);
        getContentPane().add(lblEmail);
        txtEmail = new JTextField();
        txtEmail.setBounds(157, 169, 261, 37);
        getContentPane().add(txtEmail);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(76, 220, 80, 25);
        getContentPane().add(lblPassword);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(157, 214, 261, 37);
        getContentPane().add(txtPassword);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setBounds(76, 265, 80, 25);
        getContentPane().add(lblRol);
        String[] roles = {"autor", "editor", "owner"};
        cmbRol = new JComboBox<>(roles);
        cmbRol.setBounds(157, 259, 261, 37);
        getContentPane().add(cmbRol);


        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(157, 320, 261, 48); 
        getContentPane().add(btnRegistrar);

        JButton btnVolver = new JButton("Volver al Login");
        btnVolver.setBounds(157, 380, 261, 48); 
        getContentPane().add(btnVolver);

        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });

        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Registro.this.dispose();
                loginFrame.setVisible(true);
            }
        });
    }

    public void registrarUsuario() {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String password = String.valueOf(txtPassword.getPassword()).trim();
        String rol = (String) cmbRol.getSelectedItem();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario nuevoUsuario = null;
        switch (rol) {
            case "autor":
                nuevoUsuario = new Autor(0, nombre, email, password, null);
                Registro.this.dispose();
                new SeleccionGeneros((Autor) nuevoUsuario, loginFrame).setVisible(true);
                break;
            case "editor":
                nuevoUsuario = new Editor(0, nombre, email, password);
                Usuario.RegistrarUsuario(nuevoUsuario);
                JOptionPane.showMessageDialog(this, "Usuario Editor registrado exitosamente. Ya puedes iniciar sesión.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                Registro.this.dispose();
                loginFrame.setVisible(true);
                break;
            case "owner":
                nuevoUsuario = new Owner(0, nombre, email, password);
                Usuario.RegistrarUsuario(nuevoUsuario);
                JOptionPane.showMessageDialog(this, "Usuario Owner registrado exitosamente. Ya puedes iniciar sesión.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                Registro.this.dispose();
                loginFrame.setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Rol inválido seleccionado.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }
}