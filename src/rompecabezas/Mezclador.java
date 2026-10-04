package rompecabezas;
import java.util.Random;

public class Mezclador {

    private final Random random;

    public Mezclador() {
        this.random = new Random();
    }

    public Mezclador(long semilla) {
        this.random = new Random(semilla);
    }

    public int[][] revolver(int pasos) {
        int[][] estado = Tablero.matrizMeta();
        String ultimaDireccionInversa = null;

        for (int i = 0; i < pasos; i++) {
            String direccion;
            int[][] siguienteEstado;
            int intentos = 0;
            do {
                direccion = Tablero.DIRECCIONES[random.nextInt(Tablero.DIRECCIONES.length)];
                siguienteEstado = Tablero.mover(estado, direccion);
                intentos++;

            } while ((siguienteEstado == null || direccion.equals(ultimaDireccionInversa)) && intentos < 20);

            if (siguienteEstado == null) {
                continue;
            }

            estado = siguienteEstado;
            ultimaDireccionInversa = direccionInversa(direccion);
        }

        return estado;
    }

    private String direccionInversa(String direccion) {
        switch (direccion) {
            case "ARRIBA": return "ABAJO";
            case "ABAJO": return "ARRIBA";
            case "IZQUIERDA": return "DERECHA";
            case "DERECHA": return "IZQUIERDA";
            default: return null;
        }
    }
}
