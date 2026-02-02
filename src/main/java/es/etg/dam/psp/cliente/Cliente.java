package es.etg.dam.psp.cliente;

import java.io.IOException;
import java.net.Socket;

import es.etg.dam.psp.Conexion;
import es.etg.dam.psp.partida.Partida;

public class Cliente {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    private static final String MSG_USO = "Uso: java Cliente <nombreJugador>";
    private static final String MSG_ERROR = "Error en el cliente";

    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println(MSG_USO);
            return;
        }

        String nombre = args[0];

        try (Socket socket = new Socket(HOST, PUERTO)) {

            Conexion.enviar(nombre, socket);

            boolean fin = false;

            while (!fin) {
                String mensaje = Conexion.recibir(socket);
                System.out.println(mensaje);

                if (mensaje.equals(Partida.MSG_VICTORIA) || mensaje.equals(Partida.MSG_DERROTA)) {
                    fin = true;
                }
            }
            socket.close();

        } catch (IOException e) {
            System.err.println(MSG_ERROR);
            e.printStackTrace();
        }
    }
}

