package barcamprd

class Registro {

    String cedula
    String nombre
    String correo
    String sizeCamiseta
    EstadoRegistro estado
    boolean correoConfirmacionEnviado = false
    boolean correoEncuestaEnviado = false

    static hasMany = [listaCharlas : Charla]

    Date dateCreated;
    Date lastUpdated;

    static constraints = {
        cedula unique: true
        correo unique: true
    }

    static mapping = {
        listaCharlas(lazy: false)
    }
}
