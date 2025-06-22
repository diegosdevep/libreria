package autoresindependientes.EditorMenu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import autoresindependientes.Propuesta;

import java.awt.*;
import java.util.List;

@SuppressWarnings("serial")
public class VerTodasPropuestasPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

	public VerTodasPropuestasPanel() {
        setLayout(new BorderLayout());
        String[] columnas = {"Título", "Estado", "Autor"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                } else {
                    c.setBackground(new Color(184, 207, 229));
                }
                return c;
            }
        };
        tabla.setRowHeight(25);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabla.getTableHeader().setBackground(new Color(60, 63, 65));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setFillsViewportHeight(true);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);
    }

    public void actualizarDatos(List<Propuesta> propuestas) {
        modelo.setRowCount(0);
        for (Propuesta p : propuestas) {
            modelo.addRow(new Object[]{
                p.getTitulo(),
                p.getEstado().toString(),
                p.getAutor().getName()
            });
        }
    }

    public JTable getTabla() {
        return tabla;
    }
}
