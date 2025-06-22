package autoresindependientes.OwnerMenu;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Auth.Login;
import autoresindependientes.Owner;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuOwner extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    public MenuOwner(Owner owner) {
        setTitle("Menú de Owner - " + owner.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);

        setContentPane(contentPane);

        JLabel lblWelcome = new JLabel("Bienvenido, Owner: " + owner.getName() + " (ID: " + owner.getId() + ")");
        lblWelcome.setBounds(50, 30, 400, 25);
        contentPane.add(lblWelcome);

        JButton btnGestionarUsuarios = new JButton("Gestionar Usuarios");
        btnGestionarUsuarios.setBounds(50, 80, 200, 40);
        contentPane.add(btnGestionarUsuarios);

        JButton btnVerLibrosVenta = new JButton("Ver Libros a la Venta");
        btnVerLibrosVenta.setBounds(50, 140, 200, 40);
        contentPane.add(btnVerLibrosVenta);

        JButton btnEstablecerPrecios = new JButton("Establecer Precios de Libros");
        btnEstablecerPrecios.setBounds(50, 200, 200, 40);
        contentPane.add(btnEstablecerPrecios);

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBounds(400, 320, 150, 30);
        contentPane.add(btnLogout);

        btnGestionarUsuarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new GestionarUsuariosView(MenuOwner.this).setVisible(true);
            }
        });

        btnVerLibrosVenta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LibrosAVentaView(MenuOwner.this).setVisible(true);
            }
        });

        btnEstablecerPrecios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new PrecioLibroView(MenuOwner.this, owner).setVisible(true);
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Login().setVisible(true);
            }
        });
    }
}