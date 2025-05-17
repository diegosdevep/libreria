package autoresindependientes;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        String[] opcionesInicio = {"Iniciar sesión", "Registrarse", "Salir"};
        String[] opcionesAutor = {"Subir propuesta", "Ver estado propuesta", "Salir"};
        String[] opcionesEditor = {"Revisar propuestas", "Cambiar estado", "Salir"};
        String[] opcionesDueno = {"Modificar precios", "Ver estadísticas", "Salir"};

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
                        String genero = JOptionPane.showInputDialog("¿Qué género literario escribe?");
                        nuevo = new Autor(0, nombre, email, password, genero);
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
