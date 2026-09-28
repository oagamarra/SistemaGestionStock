import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CompraDAO {

    public static void registrarCompra(int idProveedor, int idProducto,
                                      int cantidad, double precioCompra) {

        Connection conexion = ConexionDB.conectar();

        if (conexion == null) {
            System.out.println("No se pudo conectar a la base de datos");
            return;
        }

        try {
            conexion.setAutoCommit(false);

            String consultarProducto =
                    "SELECT nombre, stock_actual FROM producto WHERE id_producto = ?";

            PreparedStatement psProducto =
                    conexion.prepareStatement(consultarProducto);

            psProducto.setInt(1, idProducto);

            ResultSet resultado = psProducto.executeQuery();

            if (resultado.next()) {

                String nombreProducto = resultado.getString("nombre");
                int stockActual = resultado.getInt("stock_actual");

                double subtotal = cantidad * precioCompra;

                String insertarCompra =
                        "INSERT INTO compra (fecha, total, id_proveedor) " +
                        "VALUES (CURDATE(), ?, ?)";

                PreparedStatement psCompra =
                        conexion.prepareStatement(
                                insertarCompra,
                                PreparedStatement.RETURN_GENERATED_KEYS
                        );

                psCompra.setDouble(1, subtotal);
                psCompra.setInt(2, idProveedor);

                psCompra.executeUpdate();

                ResultSet claves = psCompra.getGeneratedKeys();

                if (claves.next()) {

                    int idCompra = claves.getInt(1);

                    String insertarDetalle =
                            "INSERT INTO detalle_compra " +
                            "(cantidad, precio_compra, subtotal, id_compra, id_producto) " +
                            "VALUES (?, ?, ?, ?, ?)";

                    PreparedStatement psDetalle =
                            conexion.prepareStatement(insertarDetalle);

                    psDetalle.setInt(1, cantidad);
                    psDetalle.setDouble(2, precioCompra);
                    psDetalle.setDouble(3, subtotal);
                    psDetalle.setInt(4, idCompra);
                    psDetalle.setInt(5, idProducto);

                    psDetalle.executeUpdate();

                    String actualizarStock =
                            "UPDATE producto " +
                            "SET stock_actual = stock_actual + ? " +
                            "WHERE id_producto = ?";

                    PreparedStatement psStock =
                            conexion.prepareStatement(actualizarStock);

                    psStock.setInt(1, cantidad);
                    psStock.setInt(2, idProducto);

                    psStock.executeUpdate();

                    conexion.commit();

                    System.out.println("\nCOMPRA REGISTRADA CORRECTAMENTE");
                    System.out.println("Producto: " + nombreProducto);
                    System.out.println("Cantidad comprada: " + cantidad);
                    System.out.println("Total: $" + subtotal);
                    System.out.println("Nuevo stock: " + (stockActual + cantidad));
                }

            } else {
                System.out.println("Producto no encontrado");
                conexion.rollback();
            }

        } catch (Exception e) {

            try {
                conexion.rollback();
            } catch (Exception ex) {
                System.out.println("Error al cancelar la operación");
            }

            System.out.println("Error al registrar la compra");
            System.out.println(e.getMessage());
        }
    }
}