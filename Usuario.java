public abstract class Usuario {

    private int idUsuario;
    private String nombre;
    private String apellido;
    private String username;
    private String password;

    public Usuario(int idUsuario, String nombre, String apellido,
                   String username, String password) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.password = password;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public abstract void mostrarRol();
}