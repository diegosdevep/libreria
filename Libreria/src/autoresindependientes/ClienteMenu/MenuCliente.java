package autoresindependientes.ClienteMenu;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Auth.Login;
import autoresindependientes.Cliente;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuCliente extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public MenuCliente(Cliente cliente) {
        setTitle("Menú de Cliente - " + cliente.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        setLocationRelativeTo(null); 

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblBienvenido = new JLabel("Bienvenido, Cliente: " + cliente.getName() + " (ID: " + cliente.getId() + ")");
        lblBienvenido.setBounds(50, 30, 400, 25);
        contentPane.add(lblBienvenido);

        JButton btnVerLibros = new JButton("Ver Libros Disponibles");
        btnVerLibros.setBounds(50, 80, 200, 40);
        contentPane.add(btnVerLibros);

        JButton btnMisCompras = new JButton("Mis Compras");
        btnMisCompras.setBounds(50, 140, 200, 40);
        contentPane.add(btnMisCompras);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBounds(400, 320, 150, 30);
        contentPane.add(btnCerrarSesion);


        btnVerLibros.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new VerLibrosView(cliente).setVisible(true); 
            }
        });

        btnMisCompras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ComprasClienteView(cliente).setVisible(true); 
            }
        });

        btnCerrarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Login().setVisible(true);
            }
        });
    }
}
