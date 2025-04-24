package autoresindependientes;

import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) {

		String[] opcionesAutor = { "Subir propuesta", "Ver estado propuesta", "Salir" };
		String[] opcionesEditor = { "Revisar propuestas", "Cambiar estado", "Salir" };
		String[] opcionesDueno = { "Modificar precios", "Ver estadísticas", "Salir" };

		String email;
		do {
			email = JOptionPane.showInputDialog("Ingrese su correo:");
			if (email == null || email.isEmpty())
				break;

			String password = JOptionPane.showInputDialog("Ingrese su contraseña:");

			if (email.equals("autor@editorial.com") && password.equals("1234")) {
				int elegido;
				do {
					elegido = JOptionPane.showOptionDialog(null, "Bienvenido Autor " + email, "Menú Autor", 0, 0, null,
							opcionesAutor, opcionesAutor[0]);

					switch (elegido) {
					case 0:
						JOptionPane.showMessageDialog(null, "Subiendo propuesta...");
						break;
					case 1:
						JOptionPane.showMessageDialog(null, "Mostrando estado de propuesta...");
						break;
					case 2:
						JOptionPane.showMessageDialog(null, "Saliendo...");
						break;
					default:
						break;
					}

				} while (elegido != 2);

			} else if (email.equals("editor@editorial.com") && password.equals("1234")) {
				int elegido;
				do {
					elegido = JOptionPane.showOptionDialog(null, "Bienvenido Editor " + email, "Menú Editor", 0, 0,
							null, opcionesEditor, opcionesEditor[0]);

					switch (elegido) {
					case 0:
						JOptionPane.showMessageDialog(null, "Revisando propuestas...");
						break;
					case 1:
						JOptionPane.showMessageDialog(null, "Cambiando estado de propuesta...");
						break;
					case 2:
						JOptionPane.showMessageDialog(null, "Saliendo...");
						break;
					default:
						break;
					}

				} while (elegido != 2);

			} else if (email.equals("dueno@editorial.com") && password.equals("1234")) {
				int elegido;
				do {
					elegido = JOptionPane.showOptionDialog(null, "Bienvenido Dueño " + email, "Menú Dueño", 0, 0, null,
							opcionesDueno, opcionesDueno[0]);

					switch (elegido) {
					case 0:
						JOptionPane.showMessageDialog(null, "Modificando precios...");
						break;
					case 1:
						JOptionPane.showMessageDialog(null, "Mostrando estadísticas (futuro)...");
						break;
					case 2:
						JOptionPane.showMessageDialog(null, "Saliendo...");
						break;
					default:
						break;
					}

				} while (elegido != 2);

			} else {
				JOptionPane.showMessageDialog(null, "Credenciales incorrectas. Intente nuevamente.");
			}

		} while (true);
	}
}
