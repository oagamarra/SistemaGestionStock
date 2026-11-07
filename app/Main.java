package app;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.ArrayList;

import conexion.ConexionDB;
import dao.CompraDAO;
import dao.VentaDAO;
import modelo.Empleado;
import modelo.Encargado;
import modelo.Usuario;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
    String[] opcionesMenu = {
    "Consultar productos",
    "Consultar productos con stock bajo",
    "Registrar venta",
    "Registrar compra",
    "Salir"
};

        // PRUEBA DE POLIMORFISMO
        Usuario usuario1 = new Empleado(
                2,
                "Juan",
                "Perez",
                "juan",
                "1234"
        );

        Usuario usuario2 = new Encargado(
                1,
                "Rebeca",
                "Gamarra",
                "rebeca",
                "1234"
        );

        System.out.println("\n===== PRUEBA DE POLIMORFISMO =====");
        usuario1.mostrarRol();
        usuario2.mostrarRol();

        // CONEXIÓN A MYSQL
        Connection conexion = ConexionDB.conectar();

        if (conexion != null) {
            System.out.println("Prueba de conexión exitosa");
        }

        int opcion = 0;

        while (opcion != 5) {

            System.out.println("\n===== SISTEMA DE GESTIÓN DE STOCK =====");
            for (int i = 0; i < opcionesMenu.length; i++) {
    System.out.println((i + 1) + ". " + opcionesMenu[i]);
}
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {

                    case 1:
                        System.out.println("\n===== LISTADO DE PRODUCTOS =====");
                        ArrayList<String> listaProductos = new ArrayList<>();

                        if (conexion != null) {

                            try {
                                Statement sentencia = conexion.createStatement();

                                String sql = "SELECT * FROM producto";

                                ResultSet resultado =
                                        sentencia.executeQuery(sql);

                                while (resultado.next()) {
    String producto =
        "ID: " + resultado.getInt("id_producto")
        + " | Código: " + resultado.getString("codigo")
        + " | Nombre: " + resultado.getString("nombre")
        + " | Precio: $" + resultado.getDouble("precio_venta")
        + " | Stock: " + resultado.getInt("stock_actual");

listaProductos.add(producto);

System.out.println(producto);
  }
  System.out.println(
        "Total de productos almacenados en ArrayList: "
        + listaProductos.size()
);                              

                            } catch (Exception e) {

                                System.out.println(
                                        "Error al consultar productos"
                                );

                                System.out.println(e.getMessage());
                            }

                        } else {
                            System.out.println(
                                    "No hay conexión con la base de datos"
                            );
                        }

                        break;

                    case 2:
                        System.out.println(
                                "\n===== PRODUCTOS CON STOCK BAJO ====="
                        );

                        if (conexion != null) {

                            try {
                                Statement sentencia = conexion.createStatement();

                                String sqlStockBajo =
                                        "SELECT nombre, stock_actual, stock_minimo "
                                        + "FROM producto "
                                        + "WHERE stock_actual <= stock_minimo";

                                ResultSet stockBajo =
                                        sentencia.executeQuery(sqlStockBajo);

                                boolean hayStockBajo = false;

                                while (stockBajo.next()) {

                                    hayStockBajo = true;

                                    System.out.println(
                                            "Producto: "
                                            + stockBajo.getString("nombre")
                                            + " | Stock actual: "
                                            + stockBajo.getInt("stock_actual")
                                            + " | Stock mínimo: "
                                            + stockBajo.getInt("stock_minimo")
                                    );
                                }

                                if (!hayStockBajo) {
                                    System.out.println(
                                            "No hay productos con stock bajo"
                                    );
                                }

                            } catch (Exception e) {

                                System.out.println(
                                        "Error al consultar stock bajo"
                                );

                                System.out.println(e.getMessage());
                            }

                        } else {
                            System.out.println(
                                    "No hay conexión con la base de datos"
                            );
                        }

                        break;

                    case 3:
                        System.out.println("\n===== REGISTRAR VENTA =====");

                        try {
                            System.out.print("Ingrese ID del usuario: ");
                            int idUsuario =
                                    Integer.parseInt(scanner.nextLine());

                            System.out.print("Ingrese ID del producto: ");
                            int idProducto =
                                    Integer.parseInt(scanner.nextLine());

                            System.out.print("Ingrese cantidad a vender: ");
                            int cantidad =
                                    Integer.parseInt(scanner.nextLine());

                            VentaDAO.registrarVenta(
                                    idUsuario,
                                    idProducto,
                                    cantidad
                            );

                        } catch (NumberFormatException e) {
                            System.out.println(
                                    "Debe ingresar valores numéricos válidos"
                            );
                        }

                        break;

                    case 4:
                        System.out.println("\n===== REGISTRAR COMPRA =====");

                        try {
                            System.out.print("Ingrese ID del proveedor: ");
                            int idProveedor =
                                    Integer.parseInt(scanner.nextLine());

                            System.out.print("Ingrese ID del producto: ");
                            int idProducto =
                                    Integer.parseInt(scanner.nextLine());

                            System.out.print("Ingrese cantidad comprada: ");
                            int cantidad =
                                    Integer.parseInt(scanner.nextLine());

                            System.out.print("Ingrese precio de compra: $");

                            double precioCompra =
                                    Double.parseDouble(
                                            scanner.nextLine()
                                                    .replace(",", ".")
                                    );

                            CompraDAO.registrarCompra(
                                    idProveedor,
                                    idProducto,
                                    cantidad,
                                    precioCompra
                            );

                        } catch (NumberFormatException e) {
                            System.out.println(
                                    "Debe ingresar valores numéricos válidos"
                            );
                        }

                        break;

                    case 5:
                        System.out.println(
                                "\nSaliendo del sistema..."
                        );
                        break;

                    default:
                        System.out.println(
                                "Opción incorrecta. Intente nuevamente."
                        );
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Debe ingresar un número del 1 al 5."
                );
            }
        }

        // CERRAR CONEXIÓN
        try {

            if (conexion != null) {
                conexion.close();
            }

        } catch (Exception e) {
            System.out.println(
                    "Error al cerrar la conexión"
            );
        }

        scanner.close();

        System.out.println("Programa finalizado correctamente.");
    }
}