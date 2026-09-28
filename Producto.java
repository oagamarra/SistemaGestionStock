public class Producto {

    private int idProducto;
    private String codigo;
    private String nombre;
    private String descripcion;
    private double precioVenta;
    private int stockActual;
    private int stockMinimo;

    public Producto(int idProducto, String codigo, String nombre, String descripcion,
                    double precioVenta, int stockActual, int stockMinimo) {
        this.idProducto = idProducto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public int getStockActual() {
        return stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public boolean esStockBajo() {
        return stockActual <= stockMinimo;
    }
}