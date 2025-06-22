package autoresindependientes.EditorMenu;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import autoresindependientes.Propuesta;

import java.awt.*;
import java.util.List;

@SuppressWarnings("serial")
public class RevisarPropuestasPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnSeleccionar;

    public RevisarPropuestasPanel() {
        setLayout(new BorderLayout());

        String[] columnas = {"ID", "Título", "Género", "Autor", "Estado"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabla.getTableHeader().setBackground(new Color(60, 63, 65));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setFillsViewportHeight(true);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String estado = (String) table.getValueAt(row, 4);
                if (!isSelected) {
                    if ("Rechazada".equalsIgnoreCase(estado)) {
                        c.setForeground(Color.RED);
                    } else if ("Aceptada".equalsIgnoreCase(estado)) {
                        c.setForeground(new Color(0, 128, 0));
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                } else {
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel();
        btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.setPreferredSize(new Dimension(150, 40));
        btnSeleccionar.setToolTipText("Seleccionar la propuesta para revisarla");
        btnSeleccionar.setEnabled(false);
        panelAcciones.add(btnSeleccionar);
        add(panelAcciones, BorderLayout.SOUTH);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnSeleccionar.setEnabled(tabla.getSelectedRow() >= 0);
            }
        });
    }

    public void actualizarDatos(List<Propuesta> propuestas) {
        modelo.setRowCount(0);
        for (Propuesta p : propuestas) {
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getTitulo(),
                    p.getGenero(),
                    p.getAutor().getName(),
                    p.getEstado().toString()
            });
        }
    }

    public JTable getTabla() {
        return tabla;
    }

    public JButton getBtnSeleccionar() {
        return btnSeleccionar;
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }
}
