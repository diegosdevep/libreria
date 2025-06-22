package autoresindependientes.OwnerMenu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import autoresindependientes.Libro;

public class LibrosAVentaView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tablaLibros;
    private DefaultTableModel tableModel;
    @SuppressWarnings("serial")
	public LibrosAVentaView(MenuOwner ownerMenu) {
        setTitle("Libros a la Venta");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 750, 500);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 10));
        setContentPane(contentPane);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel lblTitulo = new JLabel("Libros Actualmente a la Venta");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        northPanel.add(lblTitulo);
        contentPane.add(northPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        JLabel lblTablaTitulo = new JLabel("Lista de Libros Publicados:");
        lblTablaTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        centerPanel.add(lblTablaTitulo, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Título", "Autor", "Género", "Páginas", "Precio"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaLibros = new JTable(tableModel);
        tablaLibros.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnVolver = new JButton("Volver al Menú Owner");
        southPanel.add(btnVolver);
        contentPane.add(southPanel, BorderLayout.SOUTH);

        cargarLibros();

        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                ownerMenu.setVisible(true);
            }
        });
    }

    private void cargarLibros() {
        tableModel.setRowCount(0);

        List<Libro> libros = Libro.obtenerTodosLosLibros();

        if (libros.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay libros publicados actualmente.", "Sin Libros", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Libro libro : libros) {
                Object[] rowData = {
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAutor().getName(),
                    libro.getGenero(),
                    libro.getCantidadPaginas(),
                    String.format("%.2f", libro.getPrecio())
                };
                tableModel.addRow(rowData);
            }
        }
    }
}