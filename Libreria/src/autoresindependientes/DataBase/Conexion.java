package autoresindependientes.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexion {
	private static String URL ="jdbc:mysql://localhost:3306/libreria";
	private static String USER = "root";
	private static String PASSWORD ="";
    private static Conexion instance;

	public static Conexion getInstance() {
        if (instance == null) {
            instance = new Conexion();
        }
        return instance;
    }


    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        return conn;
    }

}
