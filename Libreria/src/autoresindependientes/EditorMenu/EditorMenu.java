package autoresindependientes.EditorMenu;

import javax.swing.*;

import Auth.Login;
import autoresindependientes.Propuesta;
import java.awt.*;
import java.util.List;

@SuppressWarnings("serial")
public class EditorMenu extends JFrame {

    private JPanel contenedor;
    private CardLayout cardLayout;

    private ResumenPanel resumenPanel;
    private RevisarPropuestasPanel revisarPanel;
    private VerTodasPropuestasPanel verTodasPanel;

    public EditorMenu(Editor editor) {
        if (editor == null) throw new IllegalArgumentException("El editor no puede ser null");
        setTitle("Menu Editor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelSuperior.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Bienvenido Editor " + editor.getName());
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setForeground(new Color(33, 33, 33));
        panelSuperior.add(titulo, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contenedor = new JPanel(cardLayout);

        resumenPanel = new ResumenPanel();
        revisarPanel = new RevisarPropuestasPanel();
        verTodasPanel = new VerTodasPropuestasPanel();

        contenedor.add(resumenPanel, "resumen");
        contenedor.add(revisarPanel, "revisar");
        contenedor.add(verTodasPanel, "verTodas");

        add(contenedor, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();

        JButton btnResumen = new JButton("Resumen");
        JButton btnRevisar = new JButton("Revisar propuestas");
        JButton btnVerTodas = new JButton("Ver todas");
        JButton btnLogout = new JButton("Salir");

        panelBotones.add(btnResumen);
        panelBotones.add(btnRevisar);
        panelBotones.add(btnVerTodas);
        panelBotones.add(btnLogout);
        add(panelBotones, BorderLayout.SOUTH);

        btnResumen.addActionListener(e -> {
            actualizarResumen();
            cardLayout.show(contenedor, "resumen");
        });

        btnRevisar.addActionListener(e -> {
            try {
                List<Propuesta> todas = Propuesta.obtenerTodasLasPropuestas();
                List<Propuesta> enRevision = todas.stream()
                        .filter(p -> p.getEstado() == Propuesta.EstadoPropuesta.EN_REVISION)
                        .toList();

                if (enRevision.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hay propuestas en revisión.");
                } else {
                    revisarPanel.actualizarDatos(enRevision);
                    cardLayout.show(contenedor, "revisar");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar propuestas: " + ex.getMessage());
            }
        });

        btnVerTodas.addActionListener(e -> {
            try {
                List<Propuesta> todas = Propuesta.obtenerTodasLasPropuestas();
                if (todas.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hay propuestas registradas.");
                } else {
                    verTodasPanel.actualizarDatos(todas);
                    cardLayout.show(contenedor, "verTodas");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar propuestas: " + ex.getMessage());
            }
        });

        btnLogout.addActionListener(e -> {
            dispose(); 
            new Login().setVisible(true); 
        });

        revisarPanel.getBtnSeleccionar().addActionListener(e -> {
            int fila = revisarPanel.getTabla().getSelectedRow();
            if (fila >= 0) {
                int idSeleccionado = (Integer) revisarPanel.getModelo().getValueAt(fila, 0);
                try {
                    Propuesta p = Propuesta.buscarPorId(idSeleccionado);
                    if (p != null) {
                        DetallePropuestaDialog dialog = new DetallePropuestaDialog(this, p); 
                        dialog.setVisible(true);

                        if (dialog.isConfirmado()) {
                            p.setEstado(dialog.isAceptada() ? Propuesta.EstadoPropuesta.ACEPTADA : Propuesta.EstadoPropuesta.RECHAZADA);
                            p.guardarCambios();
                            JOptionPane.showMessageDialog(this, "Propuesta " + 
                                (dialog.isAceptada() ? "aceptada" : "rechazada") + " correctamente.");
                            btnRevisar.doClick();
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al procesar la propuesta: " + ex.getMessage());
                }
            }
        });


        actualizarResumen();
        cardLayout.show(contenedor, "resumen");
    }

    private void actualizarResumen() {
        try {
            List<Propuesta> todas = Propuesta.obtenerTodasLasPropuestas();

            long aceptadas = todas.stream()
                    .filter(p -> p.getEstado() == Propuesta.EstadoPropuesta.ACEPTADA)
                    .count();
            long rechazadas = todas.stream()
                    .filter(p -> p.getEstado() == Propuesta.EstadoPropuesta.RECHAZADA)
                    .count();
            long enRevision = todas.stream()
                    .filter(p -> p.getEstado() == Propuesta.EstadoPropuesta.EN_REVISION)
                    .count();

            resumenPanel.setResumen(aceptadas, rechazadas, enRevision);
        } catch (Exception e) {
            resumenPanel.mostrarError();
        }
    }
}
