package autoresindependientes;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // Método para selección múltiple de géneros con JCheckBox dentro de JOptionPane
    private static List<String> seleccionarGeneros(List<String> generosDisponibles) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        List<JCheckBox> checkBoxes = new ArrayList<>();
        for (String genero : generosDisponibles) {
            JCheckBox checkBox = new JCheckBox(genero);
            checkBoxes.add(checkBox);
            panel.add(checkBox);
        }

        int result = JOptionPane.showConfirmDialog(null, panel, "Seleccione géneros",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        List<String> generosSeleccionados = new ArrayList<>();
        if (result == JOptionPane.OK_OPTION) {
            for (JCheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    generosSeleccionados.add(checkBox.getText());
                }
            }
        }
        return generosSeleccionados;
    }

    public static void main(String[] args) {
        String[] opcionesInicio = {"Iniciar sesión", "Registrarse", "Salir"};
        String[] opcionesAutor = {"Subir propuesta", "Ver estado propuesta", "Salir"};
        String[] opcionesEditor = {"Revisar propuestas", "Cambiar estado", "Salir"};
        String[] opcionesDueno = {"Modificar precios", "Ver estadísticas", "Salir"};

        List<String> generosDisponibles = List.of(
        	    "Ficcion",
        	    "No Ficcion",
        	    "Misterio",
        	    "Romance",
        	    "Ciencia Ficcion",
        	    "Fantasia",
        	    "Terror",
        	    "Historico",
        	    "Aventura",
        	    "Drama",
        	    "Thriller",
        	    "Biografia"
        	);


        while (true) {
            int opcion = JOptionPane.showOptionDialog(null, "Seleccione una opción:", "Bienvenido",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    opcionesInicio, opcionesInicio[0]);

            if (opcion == 2 || opcion == JOptionPane.CLOSED_OPTION) break;

            if (opcion == 1) { 
                String nombre = JOptionPane.showInputDialog("Ingrese su nombre:");
                String email = JOptionPane.showInputDialog("Ingrese su email:");
                String password = JOptionPane.showInputDialog("Ingrese su contraseña:");
                String[] roles = {"autor", "editor", "owner"};
                String rol = (String) JOptionPane.showInputDialog(null, "Seleccione tipo:", "Rol",
                        JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);

                Usuario nuevo;

                switch (rol) {
                    case "autor":
                        List<String> generosSeleccionados = seleccionarGeneros(generosDisponibles);
                        if (generosSeleccionados.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Debe seleccionar al menos un género.");
                            continue;
                        }
                        String generoConcat = String.join(",", generosSeleccionados);

                        nuevo = new Autor(0, nombre, email, password, generoConcat);
                        break;
                    case "editor":
                        nuevo = new Editor(0, nombre, email, password);
                        break;
                    case "owner":
                        nuevo = new Owner(0, nombre, email, password);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Rol inválido.");
                        continue;
                }

                Usuario.RegistrarUsuario(nuevo);
                continue;
            }

            String nombre = JOptionPane.showInputDialog("Ingrese su Email:");
            String password = JOptionPane.showInputDialog("Ingrese su contraseña:");
            Usuario usuario = Usuario.login(nombre, password);

            if (usuario == null || usuario.getEmail() == null) {
                JOptionPane.showMessageDialog(null, "Credenciales incorrectas.");
                continue;
            }

            JOptionPane.showMessageDialog(null, "Bienvenido " + usuario.getName() + " (" + usuario.getRol() + ")");

            int elegido;
            switch (usuario.getRol()) {
                case "autor":
                    do {
                        elegido = JOptionPane.showOptionDialog(null, "Menú Autor", "Autor", 0, 0,
                                null, opcionesAutor, opcionesAutor[0]);
                        switch (elegido) {
                            case 0:
                                JOptionPane.showMessageDialog(null, "Subiendo propuesta...");
                                break;
                            case 1:
                                JOptionPane.showMessageDialog(null, "Viendo estado de propuestas...");
                                break;
                        }
                    } while (elegido != 2);
                    break;

                case "editor":
                    do {
                        elegido = JOptionPane.showOptionDialog(null, "Menú Editor", "Editor", 0, 0,
                                null, opcionesEditor, opcionesEditor[0]);
                        switch (elegido) {
                            case 0:
                                JOptionPane.showMessageDialog(null, "Revisando propuestas...");
                                break;
                            case 1:
                                JOptionPane.showMessageDialog(null, "Cambiando estado de propuesta...");
                                break;
                        }
                    } while (elegido != 2);
                    break;

                case "owner":
                    do {
                        elegido = JOptionPane.showOptionDialog(null, "Menú Dueño", "Dueño", 0, 0,
                                null, opcionesDueno, opcionesDueno[0]);
                        switch (elegido) {
                            case 0:
                                JOptionPane.showMessageDialog(null, "Modificando precios...");
                                break;
                            case 1:
                                JOptionPane.showMessageDialog(null, "Mostrando estadísticas...");
                                break;
                        }
                    } while (elegido != 2);
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Rol no reconocido.");
            }
        }
    }
}
