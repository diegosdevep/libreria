package Auth;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import autoresindependientes.Usuario;
import autoresindependientes.AutorMenu.Autor;

@SuppressWarnings("serial")
public class SeleccionGeneros extends JFrame {

    private Autor autorTemporal; 
    private Login loginFrame;    
    private JPanel panelGeneros; 

    private List<String> generosDisponibles = List.of(
            "Ficción", "No Ficción", "Misterio", "Romance", "Ciencia Ficción", "Fantasía",
            "Terror", "Histórico", "Aventura", "Drama", "Thriller", "Biografía"
    );

    public SeleccionGeneros(Autor autorTemporal, Login loginFrame) {
        this.autorTemporal = autorTemporal;
        this.loginFrame = loginFrame;

        setTitle("Selecciona tus Géneros Literarios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 577, 450); 
        setLocationRelativeTo(null);

        getContentPane().setLayout(null);

        JLabel lblTitulo = new JLabel("¡Casi listo, " + autorTemporal.getName() + "! Selecciona tus géneros.");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(76, 20, 400, 25);
        getContentPane().add(lblTitulo);

        JLabel lblInstruccion = new JLabel("<html><center>Selecciona al menos un género en el que sueles escribir.<br>Esto ayudará a los editores a encontrarte.</center></html>");
        lblInstruccion.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInstruccion.setHorizontalAlignment(SwingConstants.CENTER);
        lblInstruccion.setBounds(76, 50, 400, 40);
        getContentPane().add(lblInstruccion);

        panelGeneros = new JPanel();
        panelGeneros.setLayout(new BoxLayout(panelGeneros, BoxLayout.Y_AXIS));
        panelGeneros.setBorder(BorderFactory.createTitledBorder("Géneros (obligatorio seleccionar al menos uno)"));

        JScrollPane scrollPaneGeneros = new JScrollPane(panelGeneros);
        scrollPaneGeneros.setBounds(157, 100, 261, 200); 
        getContentPane().add(scrollPaneGeneros);

        for (String genero : generosDisponibles) {
            panelGeneros.add(new JCheckBox(genero));
        }

        JButton btnConfirmar = new JButton("Confirmar y Registrar Autor");
        btnConfirmar.setBounds(157, 320, 261, 48);
        getContentPane().add(btnConfirmar);

        JButton btnCancelar = new JButton("Cancelar Registro");
        btnCancelar.setBounds(157, 370, 261, 48);
        getContentPane().add(btnCancelar);

        btnConfirmar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                confirmarGeneros();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int opcion = JOptionPane.showConfirmDialog(SeleccionGeneros.this,
                                "Si cancelas, el registro de tu autor no se completará. ¿Deseas continuar?",
                                "Cancelar Registro", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (opcion == JOptionPane.YES_OPTION) {
                    SeleccionGeneros.this.dispose();
                    loginFrame.setVisible(true);
                }
            }
        });
    }

    private void confirmarGeneros() {
        List<String> generosSeleccionados = new ArrayList<>();
        for (Component comp : panelGeneros.getComponents()) {
            if (comp instanceof JCheckBox) {
                JCheckBox cb = (JCheckBox) comp;
                if (cb.isSelected()) {
                    generosSeleccionados.add(cb.getText());
                }
            }
        }

        if (generosSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar al menos un género para registrarte como autor.", "Error de Selección", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String generoConcat = String.join(",", generosSeleccionados);
        autorTemporal.setGeneroQueEscribe(generoConcat); 

        Usuario.RegistrarUsuario(autorTemporal);

        JOptionPane.showMessageDialog(this, "Autor registrado exitosamente con tus géneros. ¡Ya puedes iniciar sesión!", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
        this.dispose(); 
        loginFrame.setVisible(true); 
    }
}