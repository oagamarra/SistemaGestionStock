public class Empleado extends Usuario {

    public Empleado(int idUsuario, String nombre, String apellido,
                    String username, String password) {
        super(idUsuario, nombre, apellido, username, password);
    }

    @Override
    public void mostrarRol() {
        System.out.println("Rol: Empleado");
    }
}