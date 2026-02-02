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

    public static final String MSG_NOTIFICACION_AVANCE = "%s|%s: %d puntos";
    public static final String MSG_VICTORIA = "ENHORABUENA, HAS GANADO";
    public static final String MSG_DERROTA = "GAME OVER";

    public static final int MAX_PUNTOS_ENTREGADOS = 11;

    private List<Jugador> jugadores;
    private Random random;

    public Partida() {
        jugadores = new ArrayList<>();
        random = new Random();
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
        int indice = random.nextInt(jugadores.size());
        int puntos = random.nextInt(MAX_PUNTOS_ENTREGADOS);

        Jugador j = jugadores.get(indice);
        j.sumar(puntos);

        return j;
    }

    private void notificar() throws IOException {
        String mensaje = estadoCarrera();
        for (Jugador j : jugadores) {
            Conexion.enviar(mensaje, j.getSocket());
        }
    }

    private String estadoCarrera(){
        String mensaje = " ";
        for (Jugador j : jugadores) {
            mensaje = String.format(MSG_NOTIFICACION_AVANCE, mensaje, j.getNombre(), j.getPuntos());
        }
        return mensaje;
    }

    private void finalizarJuego(Jugador ganador) throws IOException {//intentar hacerlo con operador ternario paraevitar codigo repetido
        for (Jugador j : jugadores) {
            if (j == ganador) {
                Conexion.enviar(MSG_VICTORIA, j.getSocket());
            } else {
                Conexion.enviar(MSG_DERROTA, j.getSocket());
            }
        }
    }
}
