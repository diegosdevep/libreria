package autoresindependientes.AutorMenu;

import javax.swing.*;

import autoresindependientes.Propuesta;

import java.awt.*;
import java.util.List;

@SuppressWarnings("serial")
public class PanelVerPropuestas extends JPanel {
    private Autor autor;
    private JPanel lista;
    private JScrollPane scroll;

    public PanelVerPropuestas(Autor autor) {
        this.autor = autor;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));

        scroll = new JScrollPane(lista);
        add(scroll, BorderLayout.CENTER);

        cargarPropuestas();
    }

    public void cargarPropuestas() {
        lista.removeAll();

        int ventas = autor.obtenerVentasAutor();
        JLabel lblVentas = new JLabel("Total de libros vendidos: " + ventas);
        lblVentas.setFont(new Font("Arial", Font.BOLD, 16));
        lblVentas.setForeground(new Color(0, 102, 204));
        lblVentas.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        lista.add(lblVentas);

        List<Propuesta> propuestas = autor.obtenerPropuestasDesdeBD();

        if (propuestas.isEmpty()) {
            lista.add(new JLabel("No hay propuestas registradas."));
        } else {
            for (Propuesta p : propuestas) {
                JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
                fila.setMaximumSize(new Dimension(450, 30));

                JLabel lblTitulo = new JLabel("Titulo: " + p.getTitulo());
                lblTitulo.setPreferredSize(new Dimension(250, 25));
                lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));

                JLabel lblEstado = new JLabel(p.getEstado().name());
                lblEstado.setOpaque(true);
                lblEstado.setHorizontalAlignment(SwingConstants.CENTER);
                lblEstado.setPreferredSize(new Dimension(120, 25));

                switch (p.getEstado()) {
                    case ACEPTADA:
                        lblEstado.setBackground(new Color(144, 238, 144)); 
                        lblEstado.setForeground(Color.BLACK);
                        break;
                    case EN_REVISION:
                        lblEstado.setBackground(new Color(255, 255, 204)); 
                        lblEstado.setForeground(Color.BLACK);
                        break;
                    case RECHAZADA:
                        lblEstado.setBackground(new Color(255, 160, 160)); 
                        lblEstado.setForeground(Color.BLACK);
                        break;
                    default:
                        lblEstado.setBackground(Color.LIGHT_GRAY);
                        lblEstado.setForeground(Color.BLACK);
                        break;
                }

                fila.add(lblTitulo);
                fila.add(lblEstado);
                lista.add(fila);
            }
        }

        lista.revalidate();
        lista.repaint();
    }

    public void actualizarPropuestas() {
        cargarPropuestas();
    }
}
