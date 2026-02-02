package es.etg.dam.psp.partida;

import java.net.Socket;

public class Jugador {

    public static final int PUNTOS_INICIALES = 0;
    public static final int MAXIMO_PUNTOS = 100;

    private String nombre;
    private int puntos;
    private Socket socket;

    public Jugador(String nombre, Socket socket) {
        this.nombre = nombre;
        this.socket = socket;
        this.puntos = PUNTOS_INICIALES;
    }

    public void sumar(int puntos) {
        this.puntos += puntos;
    }

    public boolean hasGanado() {
        if (puntos > MAXIMO_PUNTOS) {
            puntos = MAXIMO_PUNTOS;
        }
        return puntos == MAXIMO_PUNTOS;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public Socket getSocket() {
        return socket;
    }
}

