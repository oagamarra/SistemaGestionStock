package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import conexion.ConexionDB;
import dao.CompraDAO;
import dao.VentaDAO;

public class VentanaPrincipal extends JFrame {

    private JPanel panelCentral;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    public VentanaPrincipal() {

        setTitle("Sistema de Gestión de Stock");
        setSize(1000, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // =========================================
        // PANEL LATERAL
        // =========================================

        JPanel panelLateral = new JPanel();
        panelLateral.setPreferredSize(new Dimension(230, 620));
        panelLateral.setLayout(new GridLayout(6, 1, 10, 10));

        panelLateral.setBackground(
                new Color(35, 62, 102)
        );

        JLabel tituloMenu = new JLabel(
                "GESTIÓN DE STOCK",
                SwingConstants.CENTER
        );

        tituloMenu.setForeground(Color.WHITE);
        tituloMenu.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        JButton btnProductos =
                new JButton("Consultar productos");

        JButton btnStockBajo =
                new JButton("Stock bajo");

        JButton btnVenta =
                new JButton("Registrar venta");

        JButton btnCompra =
                new JButton("Registrar compra");

        JButton btnSalir =
                new JButton("Salir");

        JButton[] botones = {
                btnProductos,
                btnStockBajo,
                btnVenta,
                btnCompra,
                btnSalir
        };

        for (JButton boton : botones) {

            boton.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            14
                    )
            );

            boton.setFocusPainted(false);
        }

        panelLateral.add(tituloMenu);
        panelLateral.add(btnProductos);
        panelLateral.add(btnStockBajo);
        panelLateral.add(btnVenta);
        panelLateral.add(btnCompra);
        panelLateral.add(btnSalir);

        // =========================================
        // PANEL CENTRAL
        // =========================================

        panelCentral =
                new JPanel(new BorderLayout());

        panelCentral.setBackground(
                new Color(245, 245, 245)
        );

        JLabel bienvenida = new JLabel(
                "SISTEMA DE GESTIÓN DE STOCK",
                SwingConstants.CENTER
        );

        bienvenida.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        bienvenida.setForeground(
                new Color(35, 62, 102)
        );

        panelCentral.add(
                bienvenida,
                BorderLayout.CENTER
        );

        add(
                panelLateral,
                BorderLayout.WEST
        );

        add(
                panelCentral,
                BorderLayout.CENTER
        );

        // =========================================
        // EVENTOS DE LOS BOTONES
        // =========================================

        btnProductos.addActionListener(
                e -> mostrarProductos()
        );

        btnStockBajo.addActionListener(
                e -> mostrarStockBajo()
        );

        btnVenta.addActionListener(
                e -> registrarVenta()
        );

        btnCompra.addActionListener(
                e -> registrarCompra()
        );

        btnSalir.addActionListener(
                e -> System.exit(0)
        );
    }

    // =========================================
    // CONSULTAR PRODUCTOS
    // =========================================

    private void mostrarProductos() {

        panelCentral.removeAll();

        JLabel titulo = new JLabel(
                "LISTADO DE PRODUCTOS",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        titulo.setForeground(
                new Color(35, 62, 102)
        );

        String[] columnas = {
                "ID",
                "Código",
                "Nombre",
                "Precio",
                "Stock"
        };

        modeloTabla =
                new DefaultTableModel(
                        columnas,
                        0
                );

        tablaProductos =
                new JTable(modeloTabla);

        tablaProductos.setRowHeight(25);

        tablaProductos.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        JScrollPane scroll =
                new JScrollPane(
                        tablaProductos
                );

        Connection conexion =
                ConexionDB.conectar();

        if (conexion == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo conectar con la base de datos"
            );

            return;
        }

        try {

            Statement sentencia =
                    conexion.createStatement();

            String sql =
                    "SELECT * FROM producto";

            ResultSet resultado =
                    sentencia.executeQuery(sql);

            while (resultado.next()) {

                Object[] fila = {

                        resultado.getInt(
                                "id_producto"
                        ),

                        resultado.getString(
                                "codigo"
                        ),

                        resultado.getString(
                                "nombre"
                        ),

                        resultado.getDouble(
                                "precio_venta"
                        ),

                        resultado.getInt(
                                "stock_actual"
                        )
                };

                modeloTabla.addRow(fila);
            }

            conexion.close();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al consultar productos: "
                            + ex.getMessage()
            );
        }

        panelCentral.add(
                titulo,
                BorderLayout.NORTH
        );

        panelCentral.add(
                scroll,
                BorderLayout.CENTER
        );

        panelCentral.revalidate();
        panelCentral.repaint();
    }

    // =========================================
    // PRODUCTOS CON STOCK BAJO
    // =========================================

    private void mostrarStockBajo() {

        panelCentral.removeAll();

        JLabel titulo = new JLabel(
                "PRODUCTOS CON STOCK BAJO",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        titulo.setForeground(
                new Color(35, 62, 102)
        );

        String[] columnas = {
                "Producto",
                "Stock actual",
                "Stock mínimo"
        };

        DefaultTableModel modelo =
                new DefaultTableModel(
                        columnas,
                        0
                );

        JTable tabla =
                new JTable(modelo);

        tabla.setRowHeight(25);

        JScrollPane scroll =
                new JScrollPane(tabla);

        Connection conexion =
                ConexionDB.conectar();

        if (conexion == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo conectar con la base de datos"
            );

            return;
        }

        try {

            Statement sentencia =
                    conexion.createStatement();

            String sql =
                    "SELECT nombre, stock_actual, stock_minimo "
                            + "FROM producto "
                            + "WHERE stock_actual <= stock_minimo";

            ResultSet resultado =
                    sentencia.executeQuery(sql);

            boolean hayResultados = false;

            while (resultado.next()) {

                hayResultados = true;

                Object[] fila = {

                        resultado.getString(
                                "nombre"
                        ),

                        resultado.getInt(
                                "stock_actual"
                        ),

                        resultado.getInt(
                                "stock_minimo"
                        )
                };

                modelo.addRow(fila);
            }

            conexion.close();

            if (!hayResultados) {

                JOptionPane.showMessageDialog(
                        this,
                        "No hay productos con stock bajo",
                        "Stock bajo",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al consultar stock bajo: "
                            + ex.getMessage()
            );
        }

        panelCentral.add(
                titulo,
                BorderLayout.NORTH
        );

        panelCentral.add(
                scroll,
                BorderLayout.CENTER
        );

        panelCentral.revalidate();
        panelCentral.repaint();
    }

    // =========================================
    // REGISTRAR VENTA
    // =========================================

    private void registrarVenta() {

        try {

            String usuarioTexto =
                    JOptionPane.showInputDialog(
                            this,
                            "Ingrese ID del usuario:"
                    );

            if (usuarioTexto == null) {
                return;
            }

            String productoTexto =
                    JOptionPane.showInputDialog(
                            this,
                            "Ingrese ID del producto:"
                    );

            if (productoTexto == null) {
                return;
            }

            String cantidadTexto =
                    JOptionPane.showInputDialog(
                            this,
                            "Ingrese cantidad a vender:"
                    );

            if (cantidadTexto == null) {
                return;
            }

            int idUsuario =
                    Integer.parseInt(
                            usuarioTexto
                    );

            int idProducto =
                    Integer.parseInt(
                            productoTexto
                    );

            int cantidad =
                    Integer.parseInt(
                            cantidadTexto
                    );

            Connection conexion =
                    ConexionDB.conectar();

            if (conexion == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo conectar con la base de datos"
                );

                return;
            }

            Statement sentencia =
                    conexion.createStatement();

            String consulta =
                    "SELECT nombre, stock_actual "
                            + "FROM producto "
                            + "WHERE id_producto = "
                            + idProducto;

            ResultSet resultado =
                    sentencia.executeQuery(
                            consulta
                    );

            if (resultado.next()) {

                String nombreProducto =
                        resultado.getString(
                                "nombre"
                        );

                int stockActual =
                        resultado.getInt(
                                "stock_actual"
                        );

                conexion.close();

                if (stockActual < cantidad) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Stock insuficiente\n\n"
                                    + "Producto: "
                                    + nombreProducto
                                    + "\nStock disponible: "
                                    + stockActual,
                            "Venta",
                            JOptionPane.WARNING_MESSAGE
                    );

                } else {

                    VentaDAO.registrarVenta(
                            idUsuario,
                            idProducto,
                            cantidad
                    );

                    JOptionPane.showMessageDialog(
                            this,
                            "Venta registrada correctamente\n\n"
                                    + "Producto: "
                                    + nombreProducto
                                    + "\nCantidad vendida: "
                                    + cantidad
                                    + "\nNuevo stock: "
                                    + (
                                    stockActual
                                            - cantidad
                            ),
                            "Venta",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    mostrarProductos();
                }

            } else {

                conexion.close();

                JOptionPane.showMessageDialog(
                        this,
                        "Producto no encontrado"
                );
            }

        } catch (
                NumberFormatException ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar valores numéricos válidos"
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al registrar la venta: "
                            + ex.getMessage()
            );
        }
    }

    // =========================================
    // REGISTRAR COMPRA
    // =========================================

    private void registrarCompra() {

        try {

            String proveedorTexto =
                    JOptionPane.showInputDialog(
                            this,
                            "Ingrese ID del proveedor:"
                    );

            if (proveedorTexto == null) {
                return;
            }

            String productoTexto =
                    JOptionPane.showInputDialog(
                            this,
                            "Ingrese ID del producto:"
                    );

            if (productoTexto == null) {
                return;
            }

            String cantidadTexto =
                    JOptionPane.showInputDialog(
                            this,
                            "Ingrese cantidad comprada:"
                    );

            if (cantidadTexto == null) {
                return;
            }

            String precioTexto =
                    JOptionPane.showInputDialog(
                            this,
                            "Ingrese precio de compra:"
                    );

            if (precioTexto == null) {
                return;
            }

            int idProveedor =
                    Integer.parseInt(
                            proveedorTexto
                    );

            int idProducto =
                    Integer.parseInt(
                            productoTexto
                    );

            int cantidad =
                    Integer.parseInt(
                            cantidadTexto
                    );

            double precioCompra =
                    Double.parseDouble(
                            precioTexto.replace(
                                    ",",
                                    "."
                            )
                    );

            Connection conexion =
                    ConexionDB.conectar();

            if (conexion == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo conectar con la base de datos"
                );

                return;
            }

            Statement sentencia =
                    conexion.createStatement();

            String consulta =
                    "SELECT nombre, stock_actual "
                            + "FROM producto "
                            + "WHERE id_producto = "
                            + idProducto;

            ResultSet resultado =
                    sentencia.executeQuery(
                            consulta
                    );

            if (resultado.next()) {

                String nombreProducto =
                        resultado.getString(
                                "nombre"
                        );

                int stockActual =
                        resultado.getInt(
                                "stock_actual"
                        );

                conexion.close();

                CompraDAO.registrarCompra(
                        idProveedor,
                        idProducto,
                        cantidad,
                        precioCompra
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Compra registrada correctamente\n\n"
                                + "Producto: "
                                + nombreProducto
                                + "\nCantidad comprada: "
                                + cantidad
                                + "\nNuevo stock: "
                                + (
                                stockActual
                                        + cantidad
                        ),
                        "Compra",
                        JOptionPane.INFORMATION_MESSAGE
                );

                mostrarProductos();

            } else {

                conexion.close();

                JOptionPane.showMessageDialog(
                        this,
                        "Producto no encontrado"
                );
            }

        } catch (
                NumberFormatException ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar valores numéricos válidos"
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al registrar la compra: "
                            + ex.getMessage()
            );
        }
    }

    // =========================================
    // MAIN
    // =========================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    VentanaPrincipal ventana =
                            new VentanaPrincipal();

                    ventana.setVisible(true);
                }
        );
    }
}