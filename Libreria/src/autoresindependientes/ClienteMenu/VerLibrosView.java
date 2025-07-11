package autoresindependientes.ClienteMenu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import autoresindependientes.Cliente;
import autoresindependientes.Libro;

public class VerLibrosView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel booksPanel;
    private JLabel lblTituloLibro;
    private JLabel lblAutorLibro;
    private JLabel lblGeneroLibro;
    private JLabel lblPrecioLibro;
    private JButton btnComprar;
    private Cliente clienteActual;

    public VerLibrosView(Cliente cliente) {
        this.clienteActual = cliente;

        setTitle("Libros Disponibles - " + cliente.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1100, 700);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        booksPanel = new JPanel();
        booksPanel.setLayout(new GridLayout(0, 3, 20, 20)); 

        JScrollPane scrollPane = new JScrollPane(booksPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Explora Libros"));
        
        scrollPane.setPreferredSize(new Dimension(720, 550)); 

        contentPane.add(scrollPane, BorderLayout.WEST);

        JPanel panelDetalles = new JPanel();
        panelDetalles.setLayout(new GridBagLayout());
        panelDetalles.setBorder(BorderFactory.createTitledBorder("Detalles del Libro Seleccionado"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panelDetalles.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; lblTituloLibro = new JLabel("N/A"); panelDetalles.add(lblTituloLibro, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelDetalles.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; lblAutorLibro = new JLabel("N/A"); panelDetalles.add(lblAutorLibro, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelDetalles.add(new JLabel("Género:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; lblGeneroLibro = new JLabel("N/A"); panelDetalles.add(lblGeneroLibro, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelDetalles.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; lblPrecioLibro = new JLabel("N/A"); panelDetalles.add(lblPrecioLibro, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.weighty = 1.0; panelDetalles.add(new JPanel(), gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        btnComprar = new JButton("Comprar Libro");
        btnComprar.setEnabled(false);
        panelDetalles.add(btnComprar, gbc);

        contentPane.add(panelDetalles, BorderLayout.CENTER);

        JPanel panelBotonesInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.addActionListener(e -> {
            dispose();
            new MenuCliente(clienteActual).setVisible(true);
        });
        panelBotonesInferior.add(btnVolver);
        contentPane.add(panelBotonesInferior, BorderLayout.SOUTH);

        cargarLibros();
        agregarEventos();
    }

    private void cargarLibros() {
        booksPanel.removeAll();
        
        List<Libro> libros = Libro.obtenerTodosLosLibros();
        if (libros.isEmpty()) {
            JLabel lblNoBooks = new JLabel("No hay libros disponibles en este momento.", SwingConstants.CENTER);
            lblNoBooks.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            JPanel noBooksPanel = new JPanel(new GridBagLayout());
            noBooksPanel.add(lblNoBooks, new GridBagConstraints());
            booksPanel.add(noBooksPanel);
            btnComprar.setEnabled(false);
        } else {
            for (Libro libro : libros) {
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
        card.setPreferredSize(new Dimension(200, 270)); 
        card.setBackground(new Color(245, 245, 245));

        JLabel lblTituloLibroCard = new JLabel(libro.getTitulo(), SwingConstants.CENTER);
        lblTituloLibroCard.setFont(new Font("Segoe UI", Font.BOLD, 16));
        card.add(lblTituloLibroCard, BorderLayout.NORTH);

        JPanel detailsPanelCard = new JPanel();
        detailsPanelCard.setLayout(new GridLayout(0, 1));
        detailsPanelCard.setOpaque(false);

        detailsPanelCard.add(new JLabel("Autor: " + (libro.getAutor() != null ? libro.getAutor().getName() : "Desconocido")));
        detailsPanelCard.add(new JLabel("Género: " + libro.getGenero()));
        detailsPanelCard.add(new JLabel("Páginas: " + libro.getCantidadPaginas()));
        detailsPanelCard.add(new JLabel("Precio: $" + String.format("%.2f", libro.getPrecio())));
        
        card.add(detailsPanelCard, BorderLayout.CENTER);

        JButton btnVerDetallesCard = new JButton("Ver Detalles");
        btnVerDetallesCard.addActionListener(e -> {
            lblTituloLibro.setText(libro.getTitulo());
            lblAutorLibro.setText((libro.getAutor() != null) ? libro.getAutor().getName() : "Desconocido");
            lblGeneroLibro.setText(libro.getGenero());
            lblPrecioLibro.setText("$" + String.format("%.2f", libro.getPrecio()));
            btnComprar.setEnabled(true);
            
            btnComprar.putClientProperty("selectedLibro", libro);
        });
        card.add(btnVerDetallesCard, BorderLayout.SOUTH);

        return card;
    }

    private void agregarEventos() {
        btnComprar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Libro libroAComprar = (Libro) btnComprar.getClientProperty("selectedLibro");
                if (libroAComprar != null) {
                    boolean exito = clienteActual.comprarLibro(libroAComprar);
                    if (exito) {
                        JOptionPane.showMessageDialog(VerLibrosView.this, "¡Felicidades! Has comprado '" + libroAComprar.getTitulo() + "'.", "Compra Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(VerLibrosView.this, "No se pudo completar la compra. Quizás ya tienes este libro o hay un problema.", "Error de Compra", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(VerLibrosView.this, "Por favor, selecciona un libro para comprar.", "Ningún Libro Seleccionado", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Cliente clienteDePrueba = new Cliente(1, "Cliente Test", "test@example.com", "password"); 
                VerLibrosView frame = new VerLibrosView(clienteDePrueba);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}