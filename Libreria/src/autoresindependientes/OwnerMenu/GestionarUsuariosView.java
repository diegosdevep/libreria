package autoresindependientes.OwnerMenu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import autoresindependientes.Usuario;
import autoresindependientes.AutorMenu.Autor;

public class GestionarUsuariosView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tablaUsuarios;
    private DefaultTableModel tableModel;
    @SuppressWarnings({ "serial" })
	public GestionarUsuariosView(MenuOwner ownerMenu) {
        setTitle("Gestionar Usuarios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 10));
        setContentPane(contentPane);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel lblTitulo = new JLabel("Gestión de Usuarios del Sistema");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        northPanel.add(lblTitulo);
        contentPane.add(northPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        JLabel lblTablaTitulo = new JLabel("Lista de Usuarios:");
        lblTablaTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        centerPanel.add(lblTablaTitulo, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Nombre", "Email", "Rol", "Género (Autor)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int intColumn) {
                return false;
            }
        };
        tablaUsuarios = new JTable(tableModel);
        tablaUsuarios.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnAgregarUsuario = new JButton("Agregar Usuario");
        southPanel.add(btnAgregarUsuario);

        JButton btnEditarUsuario = new JButton("Editar Usuario");
        southPanel.add(btnEditarUsuario);

        JButton btnEliminarUsuario = new JButton("Eliminar Usuario");
        southPanel.add(btnEliminarUsuario);

        JButton btnVolver = new JButton("Volver al Menú Owner");
        southPanel.add(btnVolver);
        contentPane.add(southPanel, BorderLayout.SOUTH);

        cargarUsuarios();

        btnAgregarUsuario.addActionListener(e -> {
            UsuarioView dialog = new UsuarioView(this);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                Usuario nuevoUsuario = dialog.getUsuario(); 
                if (nuevoUsuario != null) {
                    Usuario.RegistrarUsuario(nuevoUsuario);
                    cargarUsuarios(); 
                }
            }
        });

        btnEditarUsuario.addActionListener(e -> {
            int selectedRow = tablaUsuarios.getSelectedRow();
            if (selectedRow != -1) {
                int userId = (int) tableModel.getValueAt(selectedRow, 0);
                Usuario usuarioOriginal = Usuario.buscarUsuarioPorId(userId); 

                if (usuarioOriginal != null) {
                    UsuarioView dialog = new UsuarioView(this, usuarioOriginal);
                    dialog.setVisible(true);

                    if (dialog.isSaved()) {
                        
                        Usuario usuarioConCambios = dialog.getUsuario();
                        String newPassword = dialog.getNewPassword(); 

                        if (usuarioConCambios != null && usuarioConCambios.actualizarUsuario(usuarioConCambios, newPassword)) {
                            cargarUsuarios();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo cargar la información del usuario para editar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un usuario para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnEliminarUsuario.addActionListener(e -> {
            int selectedRow = tablaUsuarios.getSelectedRow();
            if (selectedRow != -1) {
                int userId = (int) tableModel.getValueAt(selectedRow, 0);
                String userName = (String) tableModel.getValueAt(selectedRow, 1);
                int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar al usuario '" + userName + "' (ID: " + userId + ")?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (Usuario.eliminarUsuario(userId)) { 
                        cargarUsuarios(); 
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un usuario para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                ownerMenu.setVisible(true);
            }
        });
    }

    private void cargarUsuarios() {
        tableModel.setRowCount(0);

        LinkedList<Usuario> usuarios = Usuario.mostrarUsuarios(); 

        if (usuarios.isEmpty()) {
        } else {
            for (Usuario u : usuarios) {
                String genero = (u instanceof Autor) ? ((Autor) u).getGeneroQueEscribe() : "N/A";
                tableModel.addRow(new Object[]{u.getId(), u.getName(), u.getEmail(), u.getRol(), genero});
            }
        }
    }
}