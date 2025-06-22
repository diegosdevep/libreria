package autoresindependientes.OwnerMenu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import autoresindependientes.Propuesta;
import autoresindependientes.Owner; 

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PrecioLibroView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tablaPropuestas;
    private DefaultTableModel tableModel;
    private JTextField txtPrecio;
    private JButton btnEstablecerPrecio;
    private Propuesta selectedPropuestaFromTable;
    private Owner currentOwner; 

    @SuppressWarnings("serial")
	public PrecioLibroView(MenuOwner ownerMenu, Owner currentOwner) {
        this.currentOwner = currentOwner; 

        setTitle("Establecer Precios de Libros");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 700, 450);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 10));
        setContentPane(contentPane);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel lblTitulo = new JLabel("Establecer Precio para Propuestas Aceptadas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        northPanel.add(lblTitulo);
        contentPane.add(northPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        JLabel lblTablaTitulo = new JLabel("Propuestas Aceptadas:");
        lblTablaTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        centerPanel.add(lblTablaTitulo, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Título", "Autor", "Género", "Páginas"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPropuestas = new JTable(tableModel);
        tablaPropuestas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPropuestas.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tablaPropuestas);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel priceAndPublishPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        priceAndPublishPanel.add(new JLabel("Precio del Libro:"));
        txtPrecio = new JTextField(10);
        priceAndPublishPanel.add(txtPrecio);
        
        btnEstablecerPrecio = new JButton("Guardar"); 
        priceAndPublishPanel.add(btnEstablecerPrecio);

        southPanel.add(priceAndPublishPanel, BorderLayout.CENTER);

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JButton btnVolver = new JButton("Volver al Menú Owner");
        backButtonPanel.add(btnVolver);
        southPanel.add(backButtonPanel, BorderLayout.WEST);

        contentPane.add(southPanel, BorderLayout.SOUTH);

        cargarPropuestasAceptadas(); 

        tablaPropuestas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tablaPropuestas.getSelectedRow();
                if (selectedRow != -1) {
                    int proposalId = (int) tableModel.getValueAt(selectedRow, 0);
                    selectedPropuestaFromTable = Propuesta.buscarPorId(proposalId);
                    btnEstablecerPrecio.setEnabled(true);
                }
            }
        });

        btnEstablecerPrecio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                establecerPrecioYPublicar(); 
            }
        });

        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                ownerMenu.setVisible(true);
            }
        });

        btnEstablecerPrecio.setEnabled(false);
    }

    private void cargarPropuestasAceptadas() {
        tableModel.setRowCount(0);
        List<Propuesta> aceptadas = Propuesta.obtenerPropuestasAceptadasNoPublicadas();

        if (aceptadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay propuestas aceptadas para establecer precio.", "Sin Propuestas", JOptionPane.INFORMATION_MESSAGE);
            btnEstablecerPrecio.setEnabled(false);
        } else {
            for (Propuesta p : aceptadas) {
                Object[] rowData = {
                    p.getId(),
                    p.getTitulo(),
                    p.getAutor().getName(),
                    p.getGenero(),
                    p.getCantidadPaginas()
                };
                tableModel.addRow(rowData);
            }
        }
    }

    private void establecerPrecioYPublicar() {
        if (selectedPropuestaFromTable == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una propuesta de la tabla.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(txtPrecio.getText().trim());
            if (precio <= 0) {
                JOptionPane.showMessageDialog(this, "El precio debe ser un número positivo.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un precio válido (solo números).", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (currentOwner.establecerPrecioYPublicarLibro(selectedPropuestaFromTable, precio)) {
            cargarPropuestasAceptadas();
            txtPrecio.setText("");
            selectedPropuestaFromTable = null;
            btnEstablecerPrecio.setEnabled(false);
        }
        
    }
}