package es.etg.dam.psp.partida;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.etg.dam.psp.Conexion;

public class Partida implements Runnable {

    public static final int TIEMPO_ESPERA_INICIO = 2000;
    public static final int TIEMPO_ESPERA_PUNTOS = 500;

    public static final int UNO = 1;
    public static final int CERO = 0;

    public static final String MSG_NOTIFICACION_AVANCE = "%s|%s: %d puntos";
    public static final String MSG_VICTORIA = "%s --> ENHORABUENA, HAS GANADO";
    public static final String MSG_DERROTA = "%s --> GAME OVER";

    public static final int MAX_PUNTOS_ENTREGADOS = 10;

    private List<Jugador> jugadores;

    public Partida() {
        jugadores = new ArrayList<>();
    }

    public void registrarJugador(String nombre, Socket socket) {
        jugadores.add(new Jugador(nombre, socket));
    }

    @Override
    public void run() {
        try {
            jugar();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void jugar() throws InterruptedException, IOException {

        Thread.sleep(TIEMPO_ESPERA_INICIO);

        boolean fin = false;
        Jugador ganador = null;

        while (!fin) {
            Jugador j = avanzar();
            notificar();

            if (j.hasGanado()) {
                ganador = j;
                fin = true;
            }

            Thread.sleep(TIEMPO_ESPERA_PUNTOS);
        }

        finalizarJuego(ganador);
    }

    private Jugador avanzar() {
        Jugador j = jugadorAleatorio();
        int puntos = generarPuntos(UNO, MAX_PUNTOS_ENTREGADOS);

        j.sumar(puntos);

        return j;
    }

    private int generarPuntos(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + UNO) + min;
    }

    private Jugador jugadorAleatorio() {
        int posicion = generarPuntos(CERO, jugadores.size() - UNO);
        return jugadores.get(posicion);
    }

    private void notificar() throws IOException {
        String mensaje = estadoCarrera();
        for (Jugador j : jugadores) {
            Conexion.enviar(mensaje, j.getSocket());
        }
    }

    private String estadoCarrera(){
        String mensaje = "";
        for (Jugador j : jugadores) {
            mensaje = String.format(MSG_NOTIFICACION_AVANCE, mensaje, j.getNombre(), j.getPuntos());
        }
        return mensaje;
    }

    private void finalizarJuego(Jugador ganador) throws IOException {
        for (Jugador j : jugadores) {
            String mensaje = (j == ganador) ? String.format(MSG_VICTORIA, j.getNombre()) : String.format(MSG_DERROTA, j.getNombre());
            Conexion.enviar(mensaje, j.getSocket());
            j.getSocket().close();
        }
    }
}
