package autoresindependientes.AutorMenu;

import javax.swing.*;

import autoresindependientes.Propuesta;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;

@SuppressWarnings("serial")
public class PanelSubirPropuesta extends JPanel {
    private JTextField txtTitulo, txtPaginas;
    private JTextArea txtDescripcion;
    private JComboBox<String> comboGenero;
    private File archivoPDF;
    private Autor autor;

    public PanelSubirPropuesta(Autor autor) {
        this.autor = autor;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        add(formulario, BorderLayout.CENTER);

        JPanel panelTitulo = new JPanel(new BorderLayout(5, 5));
        panelTitulo.add(new JLabel("Titulo:"), BorderLayout.WEST);
        txtTitulo = new JTextField();
        txtTitulo.setPreferredSize(new Dimension(300, 30));
        panelTitulo.add(txtTitulo, BorderLayout.CENTER);
        panelTitulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtTitulo.getPreferredSize().height));
        formulario.add(panelTitulo);
        formulario.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel panelDescripcion = new JPanel(new BorderLayout(5, 5));
        panelDescripcion.add(new JLabel("Descripcion:"), BorderLayout.NORTH);
        txtDescripcion = new JTextArea(6, 30);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        scrollDescripcion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        panelDescripcion.add(scrollDescripcion, BorderLayout.CENTER);
        panelDescripcion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        formulario.add(panelDescripcion);
        formulario.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel panelGenero = new JPanel(new BorderLayout(5, 5));
        panelGenero.add(new JLabel("Género:"), BorderLayout.WEST);
        comboGenero = new JComboBox<>(new String[] {
            "Ficcion", "No Ficcion", "Misterio", "Romance", "Ciencia Ficcion",
            "Fantasia", "Terror", "Historico", "Aventura", "Drama", "Thriller", "Biografia"
        });
        comboGenero.setMaximumSize(new Dimension(Integer.MAX_VALUE, comboGenero.getPreferredSize().height));
        panelGenero.add(comboGenero, BorderLayout.CENTER);
        panelGenero.setMaximumSize(new Dimension(Integer.MAX_VALUE, comboGenero.getPreferredSize().height));
        formulario.add(panelGenero);
        formulario.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel panelPaginas = new JPanel(new BorderLayout(5, 5));
        panelPaginas.add(new JLabel("Paginas:"), BorderLayout.WEST);
        txtPaginas = new JTextField();
        txtPaginas.setPreferredSize(new Dimension(300, 30));
        panelPaginas.add(txtPaginas, BorderLayout.CENTER);
        panelPaginas.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtPaginas.getPreferredSize().height));
        formulario.add(panelPaginas);
        formulario.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnArchivo = new JButton("Seleccionar archivo PDF");
        btnArchivo.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnArchivo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnArchivo.addActionListener(e -> seleccionarArchivo());
        formulario.add(btnArchivo);
        formulario.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnSubir = new JButton("Subir Propuesta");
        btnSubir.setPreferredSize(new Dimension(150, 40));
        btnSubir.addActionListener(e -> subirPropuesta());
        add(btnSubir, BorderLayout.SOUTH);
    }

    private void seleccionarArchivo() {
        JFileChooser chooser = new JFileChooser();
        int resultado = chooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoPDF = chooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "Archivo seleccionado: " + archivoPDF.getName());
        }
    }

    private void subirPropuesta() {
        String titulo = txtTitulo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String genero = (String) comboGenero.getSelectedItem();
        String paginasStr = txtPaginas.getText().trim();

        if (titulo.isEmpty() || descripcion.isEmpty() || paginasStr.isEmpty() || archivoPDF == null) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos y selecciona un archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int paginas = Integer.parseInt(paginasStr);
            if (paginas <= 0) throw new NumberFormatException();

            byte[] archivoBytes = Files.readAllBytes(archivoPDF.toPath());
            Propuesta propuesta = new Propuesta(0, titulo, descripcion, genero, paginas, autor, archivoBytes);
            autor.guardarPropuestaEnBD(propuesta);

            JOptionPane.showMessageDialog(this, "Propuesta guardada correctamente.");
            limpiarFormulario();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Numero de paginas invalido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar propuesta.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    

    private void limpiarFormulario() {
        txtTitulo.setText("");
        txtDescripcion.setText("");
        txtPaginas.setText("");
        archivoPDF = null;
    }
}
