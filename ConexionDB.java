import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/gestion_stock";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    public static Connection conectar() {
        Connection conexion = null;

        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conexión a MySQL realizada correctamente");
        } catch (SQLException e) {
            System.out.println("Error al conectar con MySQL");
            System.out.println(e.getMessage());
        }

        return conexion;
    }
}
