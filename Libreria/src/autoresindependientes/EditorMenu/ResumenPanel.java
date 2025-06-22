package autoresindependientes.EditorMenu;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class ResumenPanel extends JPanel {
    private JPanel contenedorResumen;

    public ResumenPanel() {
        setLayout(new BorderLayout());
        contenedorResumen = new JPanel();
        contenedorResumen.setLayout(new BoxLayout(contenedorResumen, BoxLayout.Y_AXIS));
        contenedorResumen.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(new JScrollPane(contenedorResumen), BorderLayout.CENTER);
    }

    public void setResumen(long aceptadas, long rechazadas, long enRevision) {
        contenedorResumen.removeAll();

        JLabel titulo = new JLabel("Resumen de propuestas");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        contenedorResumen.add(titulo);

        contenedorResumen.add(crearFilaResumen("Aceptadas", aceptadas, new Color(144, 238, 144)));
        contenedorResumen.add(Box.createRigidArea(new Dimension(0, 10)));
        contenedorResumen.add(crearFilaResumen("Rechazadas", rechazadas, new Color(255, 160, 160)));
        contenedorResumen.add(Box.createRigidArea(new Dimension(0, 10)));
        contenedorResumen.add(crearFilaResumen("En revisión", enRevision, new Color(255, 255, 204)));

        contenedorResumen.revalidate();
        contenedorResumen.repaint();
    }

    private JPanel crearFilaResumen(String texto, long cantidad, Color colorFondo) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.setMaximumSize(new Dimension(450, 30));

        JLabel lblTexto = new JLabel(texto + ": ");
        lblTexto.setFont(new Font("Arial", Font.BOLD, 14));
        lblTexto.setPreferredSize(new Dimension(200, 25));

        JLabel lblCantidad = new JLabel(String.valueOf(cantidad));
        lblCantidad.setFont(new Font("Arial", Font.PLAIN, 14));
        lblCantidad.setOpaque(true);
        lblCantidad.setBackground(colorFondo);
        lblCantidad.setForeground(Color.BLACK);
        lblCantidad.setHorizontalAlignment(SwingConstants.CENTER);
        lblCantidad.setPreferredSize(new Dimension(100, 25));

        fila.add(lblTexto);
        fila.add(lblCantidad);

        return fila;
    }

    public void mostrarError() {
        contenedorResumen.removeAll();
        JLabel error = new JLabel("Error al cargar resumen.");
        error.setFont(new Font("Arial", Font.BOLD, 16));
        error.setForeground(Color.RED);
        contenedorResumen.add(error);
        contenedorResumen.revalidate();
        contenedorResumen.repaint();
    }
}
