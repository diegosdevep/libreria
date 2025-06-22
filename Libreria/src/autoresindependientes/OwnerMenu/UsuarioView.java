package autoresindependientes.OwnerMenu;

import autoresindependientes.Owner;
import autoresindependientes.Usuario;
import autoresindependientes.AutorMenu.Autor;
import autoresindependientes.EditorMenu.Editor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@SuppressWarnings("serial")
public class UsuarioView extends JDialog {

    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRol;
    private JTextField txtGenero;
    private JLabel lblGenero;
    private JLabel lblPassword;

    private Usuario usuarioActual; 
    private boolean saved = false;

    public UsuarioView(JFrame parent) {
        super(parent, "Agregar Nuevo Usuario", true);
        initUI();
        this.usuarioActual = null;
        setTitle("Agregar Nuevo Usuario");
        lblPassword.setText("Contraseña:"); 
    }

    public UsuarioView(JFrame parent, Usuario usuario) {
        super(parent, "Editar Usuario", true);
        this.usuarioActual = usuario;
        initUI();
        setTitle("Editar Usuario: " + usuario.getName());
        populateFields();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(panel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL; 

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtNombre = new JTextField(25); 
        panel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(25); 
        panel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        lblPassword = new JLabel("Contraseña:");
        panel.add(lblPassword, gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(25); 
        panel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1;
        String[] roles = {"autor", "editor", "owner"};
        cmbRol = new JComboBox<>(roles);
        panel.add(cmbRol, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        lblGenero = new JLabel("Género (para Autor):");
        panel.add(lblGenero, gbc);
        gbc.gridx = 1;
        txtGenero = new JTextField(25); 
        panel.add(txtGenero, gbc);

        cmbRol.addActionListener(e -> toggleGeneroField());
        toggleGeneroField(); 


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> {
            if (validateInput()) {
                saved = true;
                dispose();
            }
        });

        btnCancelar.addActionListener(e -> {
            saved = false;
            dispose();
        });

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(500, 400)); 
        pack(); 
        setLocationRelativeTo(getParent());
    }

    private void toggleGeneroField() {
        boolean isAutor = "autor".equals(cmbRol.getSelectedItem());
        lblGenero.setVisible(isAutor);
        txtGenero.setVisible(isAutor);
    }

    private void populateFields() {
        if (usuarioActual != null) {
            txtNombre.setText(usuarioActual.getName());
            txtEmail.setText(usuarioActual.getEmail());
            txtPassword.setText(""); 

            cmbRol.setSelectedItem(usuarioActual.getRol());
            toggleGeneroField();

            if (usuarioActual instanceof Autor) {
                txtGenero.setText(((Autor) usuarioActual).getGeneroQueEscribe());
            }
            lblPassword.setText("Nueva Contraseña (dejar vacío para no cambiar):");
        }
    }

    private boolean validateInput() {
        if (txtNombre.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y Email son campos obligatorios.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!txtEmail.getText().trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(this, "Formato de email inválido.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (usuarioActual == null && txtPassword.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "La contraseña es obligatoria para un nuevo usuario.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (txtPassword.getPassword().length > 0 && txtPassword.getPassword().length < 6) {
             JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
             return false;
        }


        if ("autor".equals(cmbRol.getSelectedItem()) && txtGenero.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El género es obligatorio para usuarios con rol 'Autor'.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public Usuario getUsuario() {
        if (!saved) {
            return null;
        }

        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String rol = (String) cmbRol.getSelectedItem();
        String genero = txtGenero.getText().trim();

        int id = (usuarioActual != null) ? usuarioActual.getId() : 0; 

        switch (rol) {
            case "autor":
                return new Autor(id, nombre, email, password, genero);
            case "editor":
                return new Editor(id, nombre, email, password);
            case "owner":
                return new Owner(id, nombre, email, password);
            default:
                return null;
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public String getNewPassword() {
        return new String(txtPassword.getPassword());
    }
}