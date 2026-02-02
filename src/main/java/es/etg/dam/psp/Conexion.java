package es.etg.dam.psp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Conexion {

    public static void enviar(String msg, Socket socket) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        output.writeUTF(msg);
    }

    public static String recibir(Socket socket) throws IOException {
        DataInputStream input = new DataInputStream(socket.getInputStream());
        return input.readUTF();
    }
}
