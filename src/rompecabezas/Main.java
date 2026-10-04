package rompecabezas;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0) {
            Tablero.TAMANIO = Integer.parseInt(args[0]);
        } else {
            Tablero.TAMANIO = 4;
        }

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
