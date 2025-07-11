package autoresindependientes.OwnerMenu;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Auth.Login;
import autoresindependientes.Owner;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BorderFactory;

import java.awt.Color;
import java.awt.Font;
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

        int totalVentas = owner.obtenerTotalLibrosVendidos();
        JLabel lblTotalVentas = new JLabel("📚 Total de libros vendidos: " + totalVentas);
        lblTotalVentas.setBounds(50, 60, 400, 40);
        lblTotalVentas.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotalVentas.setForeground(new Color(25, 88, 155));
        lblTotalVentas.setOpaque(true);
        lblTotalVentas.setBackground(new Color(230, 240, 255));
        lblTotalVentas.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 240), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        contentPane.add(lblTotalVentas);

        JButton btnGestionarUsuarios = new JButton("Gestionar Usuarios");
        btnGestionarUsuarios.setBounds(50, 120, 200, 40);
        contentPane.add(btnGestionarUsuarios);

        JButton btnVerLibrosVenta = new JButton("Ver Libros a la Venta");
        btnVerLibrosVenta.setBounds(50, 180, 200, 40);
        contentPane.add(btnVerLibrosVenta);

        JButton btnEstablecerPrecios = new JButton("Establecer Precios de Libros");
        btnEstablecerPrecios.setBounds(50, 240, 200, 40);
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
