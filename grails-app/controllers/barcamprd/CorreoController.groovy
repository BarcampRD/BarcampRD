package barcamprd

import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import com.sparkpost.Client
import com.sparkpost.exception.SparkPostErrorServerResponseException
import com.sparkpost.exception.SparkPostException
import org.springframework.security.access.annotation.Secured

@Secured(["permitAll"])
class CorreoController {

    def groovyPageRenderer;

    def proceso() throws SparkPostException, SparkPostErrorServerResponseException {
        def registros = Registro.findAllByEstado(EstadoRegistro.findByNumero(EstadoRegistro.ESTADO_APROBADO))

        registros.each {
            println('Enviando correo a ' + it.correo)
            if (!it.correoConfirmacionEnviado){
                HttpResponse<String> salida = Unirest.get("http://localhost:8080/correo/?id=" + it.id).asString()
                println "la salida: " + salida.body
                String API_KEY = "7cc9b263021914c819d287b6ffc3bda8e90fd9d3";
                Client client = new Client(API_KEY);
                client.sendMessage(
                        "logistica@barcamp.org.do",
                        it.correo.toLowerCase(),
                        "Información importante Barcamp 2018",
                        "",
                        salida.body)
                it.correoConfirmacionEnviado = true
                def reg = Registro.findById(it.id)
                reg.correoConfirmacionEnviado = true
                reg.save(flush: true, failOnSafe: true)
            }
        }
        render 'Enviando...'
    }

    def index() {
        def registro = Registro.findById(params.id as long)
        [participante: registro]
    }

    def encuesta() {
        def registro = Registro.findById(params.id as long)
        [participante: registro]
    }

    def qr() {
        println(params.id)
        ['participante': Registro.findById(params.id as long)]
    }


    def enviarSurvey(){
        def registros = Registro.findAllByEstado(EstadoRegistro.findByNumero(EstadoRegistro.ESTADO_CONFIRMADO))

        registros.each {
            println('Enviando correo a ' + it.correo)
            if (!it.correoEncuestaEnviado){
                HttpResponse<String> salida = Unirest.get("http://localhost:8080/correo/encuesta/?id=" + it.id).asString()
                println "la salida: " + salida.body
                String API_KEY = "7cc9b263021914c819d287b6ffc3bda8e90fd9d3";
                Client client = new Client(API_KEY);
                client.sendMessage(
                        "logistica@barcamp.org.do",
                        it.correo.toLowerCase(),
                        "Encuesta Barcamp 2018",
                        "",
                        salida.body)
                it.correoEncuestaEnviado = true
                it.save(flush: true, failOnSafe: true)
            }
        }

        /*HttpResponse<String> salida = Unirest.get("http://localhost:8080/correo/encuesta/?id=" + registros.get(0).id).asString()
        println "la salida: " + salida.body
        String API_KEY = "7cc9b263021914c819d287b6ffc3bda8e90fd9d3";
        Client client = new Client(API_KEY);
        client.sendMessage(
                "logistica@barcamp.org.do",
                'dewyn.liriano@gmail.com',
                "Encuesta Barcamp 2018",
                "",
                salida.body)*/


        render 'Enviando...'
    }
}
