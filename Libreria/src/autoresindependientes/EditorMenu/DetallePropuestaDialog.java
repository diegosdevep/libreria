package autoresindependientes.EditorMenu;

import javax.swing.*;

import autoresindependientes.Propuesta;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

@SuppressWarnings("serial")
public class DetallePropuestaDialog extends JDialog {

    private boolean confirmado = false;
    private boolean aceptada = false;

    private JButton btnAceptar;
    private JButton btnRechazar;
    private JButton btnCancelar;
    private JButton btnDescargarPdf;

    public DetallePropuestaDialog(JFrame parent, Propuesta propuesta) {
        super(parent, "Detalle de la Propuesta", true);

        setLayout(new BorderLayout(10, 10));
        setSize(450, 350);
        setLocationRelativeTo(parent);

        JPanel panelDetalles = new JPanel(new GridLayout(0, 1, 5, 5));
        panelDetalles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelDetalles.add(new JLabel("Título: " + propuesta.getTitulo()));
        panelDetalles.add(new JLabel("Género: " + propuesta.getGenero()));
        panelDetalles.add(new JLabel("Autor: " + propuesta.getAutor().getName()));
        panelDetalles.add(new JLabel("Estado actual: " + propuesta.getEstado()));
        panelDetalles.add(new JLabel("Descripción:"));

        JTextArea areaDescripcion = new JTextArea(propuesta.getDescripcion());
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        areaDescripcion.setEditable(false);
        areaDescripcion.setBackground(getBackground());
        JScrollPane scrollDesc = new JScrollPane(areaDescripcion);
        scrollDesc.setPreferredSize(new Dimension(400, 120));
        panelDetalles.add(scrollDesc);

        add(panelDetalles, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));

        btnAceptar = new JButton("Aceptar");
        btnAceptar.setPreferredSize(new Dimension(150, 40));
        btnRechazar = new JButton("Rechazar");
        btnRechazar.setPreferredSize(new Dimension(150, 40));
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(150, 40));
        btnDescargarPdf = new JButton("Descargar PDF");
        btnDescargarPdf.setPreferredSize(new Dimension(150, 40));

        panelBotones.add(btnDescargarPdf);
        panelBotones.add(btnAceptar);
        panelBotones.add(btnRechazar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        btnDescargarPdf.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar PDF de la propuesta");
            fileChooser.setSelectedFile(new File(propuesta.getTitulo().replaceAll("\\W+", "_") + ".pdf"));
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    propuesta.guardarPdf(fileToSave.getAbsolutePath());
                    JOptionPane.showMessageDialog(this, "PDF guardado correctamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al guardar PDF: " + ex.getMessage());
                }
            }
        });

        btnAceptar.addActionListener((ActionEvent e) -> {
            confirmado = true;
            aceptada = true;
            dispose();
        });

        btnRechazar.addActionListener((ActionEvent e) -> {
            confirmado = true;
            aceptada = false;
            dispose();
        });

        btnCancelar.addActionListener((ActionEvent e) -> {
            confirmado = false;
            dispose();
        });
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public boolean isAceptada() {
        return aceptada;
    }
}
