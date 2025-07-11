package autoresindependientes.ClienteMenu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.List;

import autoresindependientes.Cliente;
import autoresindependientes.Libro;

public class ComprasClienteView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel booksPanel;

    public ComprasClienteView(Cliente cliente) {
        setTitle("Mis Compras - " + cliente.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 750, 500);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        JLabel lblTitulo = new JLabel("Mis Libros Comprados", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        contentPane.add(lblTitulo, BorderLayout.NORTH);

        booksPanel = new JPanel();
        booksPanel.setLayout(new GridLayout(0, 3, 20, 20));

        JScrollPane scrollPane = new JScrollPane(booksPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotonesInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVolver = new JButton("Volver al Menú Principal");
        btnVolver.addActionListener(e -> {
            dispose();
            new MenuCliente(cliente).setVisible(true);
        });
        panelBotonesInferior.add(btnVolver);
        contentPane.add(panelBotonesInferior, BorderLayout.SOUTH);

        loadPurchasedBooks(cliente);
    }

    private void loadPurchasedBooks(Cliente cliente) {
        booksPanel.removeAll();

        List<Libro> comprados = cliente.getLibrosComprados();

        if (comprados == null || comprados.isEmpty()) {
            JLabel lblNoBooks = new JLabel("No has comprado ningún libro aún.", SwingConstants.CENTER);
            lblNoBooks.setFont(new Font("Segoe UI", Font.PLAIN, 16));

            JPanel noBooksPanel = new JPanel(new GridBagLayout());
            noBooksPanel.add(lblNoBooks, new GridBagConstraints());
            booksPanel.add(noBooksPanel);
        } else {
            for (Libro libro : comprados) {
                JPanel cardPanel = createBookCard(libro);
                booksPanel.add(cardPanel);
            }
        }
        booksPanel.revalidate();
        booksPanel.repaint();
    }

    private JPanel createBookCard(Libro libro) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            new EtchedBorder(EtchedBorder.LOWERED),
            new EmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(200, 250));
        card.setBackground(new Color(245, 245, 245));

        JLabel lblTituloLibro = new JLabel(libro.getTitulo(), SwingConstants.CENTER);
        lblTituloLibro.setFont(new Font("Segoe UI", Font.BOLD, 16));
        card.add(lblTituloLibro, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(0, 1));
        detailsPanel.setOpaque(false);

        detailsPanel.add(new JLabel("Autor: " + (libro.getAutor() != null ? libro.getAutor().getName() : "Desconocido")));
        detailsPanel.add(new JLabel("Género: " + libro.getGenero()));
        detailsPanel.add(new JLabel("Páginas: " + libro.getCantidadPaginas()));
        detailsPanel.add(new JLabel("Precio: $" + String.format("%.2f", libro.getPrecio())));

        card.add(detailsPanel, BorderLayout.CENTER);

        JButton btnVerDetalles = new JButton("Ver Detalles");
        btnVerDetalles.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Detalles de: " + libro.getTitulo() + "\nDescripción: " + libro.getDescripcion(),
                "Detalles del Libro", JOptionPane.INFORMATION_MESSAGE);
        });
        card.add(btnVerDetalles, BorderLayout.SOUTH);

        return card;
    }
}