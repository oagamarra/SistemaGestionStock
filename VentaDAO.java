import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VentaDAO {

    public static void registrarVenta(int idUsuario, int idProducto, int cantidad) {

        Connection conexion = ConexionDB.conectar();

        if (conexion == null) {
            System.out.println("No se pudo conectar a la base de datos");
            return;
        }

        try {

            String consultaProducto =
                    "SELECT nombre, precio_venta, stock_actual " +
                    "FROM producto WHERE id_producto = ?";

            PreparedStatement psProducto =
                    conexion.prepareStatement(consultaProducto);

            psProducto.setInt(1, idProducto);

            ResultSet resultado = psProducto.executeQuery();

            if (resultado.next()) {

                String nombre = resultado.getString("nombre");
                double precio = resultado.getDouble("precio_venta");
                int stockActual = resultado.getInt("stock_actual");

                if (stockActual < cantidad) {
                    System.out.println("Stock insuficiente");
                    return;
                }

                double subtotal = precio * cantidad;

                String insertarVenta =
                        "INSERT INTO venta (fecha, total, id_usuario) " +
                        "VALUES (CURDATE(), ?, ?)";

                PreparedStatement psVenta =
                        conexion.prepareStatement(
                                insertarVenta,
                                PreparedStatement.RETURN_GENERATED_KEYS
                        );

                psVenta.setDouble(1, subtotal);
                psVenta.setInt(2, idUsuario);

                psVenta.executeUpdate();

                ResultSet claves = psVenta.getGeneratedKeys();

                if (claves.next()) {

                    int idVenta = claves.getInt(1);

                    String insertarDetalle =
                            "INSERT INTO detalle_venta " +
                            "(cantidad, precio_venta, subtotal, id_venta, id_producto) " +
                            "VALUES (?, ?, ?, ?, ?)";

                    PreparedStatement psDetalle =
                            conexion.prepareStatement(insertarDetalle);

                    psDetalle.setInt(1, cantidad);
                    psDetalle.setDouble(2, precio);
                    psDetalle.setDouble(3, subtotal);
                    psDetalle.setInt(4, idVenta);
                    psDetalle.setInt(5, idProducto);

                    psDetalle.executeUpdate();

                    String actualizarStock =
                            "UPDATE producto " +
                            "SET stock_actual = stock_actual - ? " +
                            "WHERE id_producto = ?";

                    PreparedStatement psStock =
                            conexion.prepareStatement(actualizarStock);

                    psStock.setInt(1, cantidad);
                    psStock.setInt(2, idProducto);

                    psStock.executeUpdate();

                    System.out.println("\nVENTA REGISTRADA CORRECTAMENTE");
                    System.out.println("Producto: " + nombre);
                    System.out.println("Cantidad: " + cantidad);
                    System.out.println("Total: $" + subtotal);
                    System.out.println("Nuevo stock: " + (stockActual - cantidad));
                }
            }

        } catch (Exception e) {
            System.out.println("Error al registrar la venta");
            System.out.println(e.getMessage());
        }
    }
}
