import java.sql.Connection;import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {

        Connection conexion = ConexionDB.conectar();

        if (conexion != null) {
            System.out.println("Prueba de conexión exitosa");

            try {
                Statement sentencia = conexion.createStatement();

                String sql = "SELECT * FROM producto";

                ResultSet resultado = sentencia.executeQuery(sql);

                System.out.println("\nLISTADO DE PRODUCTOS");

                while (resultado.next()) {
                    System.out.println(
                        "ID: " + resultado.getInt("id_producto")
                        + " | Código: " + resultado.getString("codigo")
                        + " | Nombre: " + resultado.getString("nombre")
                        + " | Precio: $" + resultado.getDouble("precio_venta")
                        + " | Stock: " + resultado.getInt("stock_actual")
                    );
                }
String sqlStockBajo =
        "SELECT nombre, stock_actual, stock_minimo " +
        "FROM producto " +
        "WHERE stock_actual <= stock_minimo";

ResultSet stockBajo = sentencia.executeQuery(sqlStockBajo);

System.out.println("\nPRODUCTOS CON STOCK BAJO");

boolean hayStockBajo = false;

while (stockBajo.next()) {
    hayStockBajo = true;

    System.out.println(
        "Producto: " + stockBajo.getString("nombre")
        + " | Stock actual: " + stockBajo.getInt("stock_actual")
        + " | Stock mínimo: " + stockBajo.getInt("stock_minimo")
    );
}

if (!hayStockBajo) {
    System.out.println("No hay productos con stock bajo");

}
int idProductoVenta = 1;
int cantidadVenta = 100;

String consultaStock =
        "SELECT nombre, stock_actual FROM producto WHERE id_producto = "
        + idProductoVenta;

ResultSet rsVenta = sentencia.executeQuery(consultaStock);

if (rsVenta.next()) {

    String nombreProducto = rsVenta.getString("nombre");
    int stockActual = rsVenta.getInt("stock_actual");

    System.out.println("\nPRUEBA DE VENTA");

    if (stockActual >= cantidadVenta) {

        int nuevoStock = stockActual - cantidadVenta;

        String actualizar =
                "UPDATE producto SET stock_actual = "
                + nuevoStock
                + " WHERE id_producto = "
                + idProductoVenta;

        sentencia.executeUpdate(actualizar);

        System.out.println("Venta permitida");
        System.out.println("Producto: " + nombreProducto);
        System.out.println("Cantidad vendida: " + cantidadVenta);
        System.out.println("Nuevo stock: " + nuevoStock);

    } else {

        System.out.println("Stock insuficiente");
        System.out.println("Producto: " + nombreProducto);
        System.out.println("Stock disponible: " + stockActual);
    }
}
            } catch (Exception e) {
                System.out.println("Error al consultar productos");
                System.out.println(e.getMessage());
            }
           // VentaDAO.registrarVenta(2, 1, 1);
            CompraDAO.registrarCompra(1, 3, 2, 1200.00);
        }
    }
}
