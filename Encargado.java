public class Encargado extends Usuario {

    public Encargado(int idUsuario, String nombre, String apellido,
                     String username, String password) {
        super(idUsuario, nombre, apellido, username, password);
    }

    @Override
    public void mostrarRol() {
        System.out.println("Rol: Encargado");
    }
}