package es.etg.dam.psp.server;

import java.net.ServerSocket;
import java.net.Socket;

import es.etg.dam.psp.Conexion;
import es.etg.dam.psp.partida.Partida;

public class Servidor {

    public static final int PUERTO = 5000;
    public static final String HOST = "localhost";

    public static final String MSG_ESCUCHA = "Servidor escuchando...";
    public static final String MSG_OK = "OK";

    public static final int NUM_JUGADORES = 4;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println(MSG_ESCUCHA);

            while (true) {
                Partida partida = new Partida();

                for (int i = 0; i < NUM_JUGADORES; i++) {
                    Socket socket = serverSocket.accept();

                    String nombre = Conexion.recibir(socket);

                    Conexion.enviar(MSG_OK, socket);

                    partida.registrarJugador(nombre, socket);
                }

                Thread hiloPartida = new Thread(partida);
                hiloPartida.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


