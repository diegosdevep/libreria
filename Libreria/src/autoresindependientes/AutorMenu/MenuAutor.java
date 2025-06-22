package autoresindependientes.AutorMenu;

import javax.swing.*;

import Auth.Login;

import java.awt.*;

@SuppressWarnings("serial")
public class MenuAutor extends JFrame {
    private CardLayout layout;
    private JPanel contenedor;
    private PanelSubirPropuesta panelSubir;
    private PanelVerPropuestas panelVer;

    public MenuAutor(Autor autor) {
        setTitle("Menú Autor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        layout = new CardLayout();
        contenedor = new JPanel(layout);

        panelSubir = new PanelSubirPropuesta(autor);
        panelVer = new PanelVerPropuestas(autor);

        contenedor.add(panelSubir, "subir");
        contenedor.add(panelVer, "ver");

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Bienvenido " + autor.getName());
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(new Color(33, 33, 33));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelSuperior.add(Box.createRigidArea(new Dimension(0, 10)));
        panelSuperior.add(titulo);


        add(panelSuperior, BorderLayout.NORTH);
        add(contenedor, BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnSubir = new JButton("Cargar Propuesta");
        btnSubir.setPreferredSize(new Dimension(150, 40));

        JButton btnVer = new JButton("Mis Propuestas");
        btnVer.setPreferredSize(new Dimension(150, 40));

        JButton btnLogout = new JButton("Salir");
        btnLogout.setPreferredSize(new Dimension(150, 40));


        btnSubir.addActionListener(e -> layout.show(contenedor, "subir"));

        btnVer.addActionListener(e -> {
            panelVer.actualizarPropuestas();
            layout.show(contenedor, "ver");
        });
        btnVer.addActionListener(e -> layout.show(contenedor, "ver"));
        btnLogout.addActionListener(e -> {
            dispose(); 
            new Login().setVisible(true); 
        });

        botones.add(btnSubir);
        botones.add(btnVer);
        botones.add(btnLogout);

        add(botones, BorderLayout.SOUTH);
    }
}
