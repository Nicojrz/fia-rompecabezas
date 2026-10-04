package rompecabezas;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class PanelRompecabezas extends JPanel {

    private final int tamanio;
    private int[][] matriz;
    private ImagenDivisor divisor;

    public PanelRompecabezas(int tamanio) {
        this.tamanio = tamanio;
        this.matriz = Tablero.matrizMeta();
        setBackground(Color.DARK_GRAY);
    }

    public void setDivisor(ImagenDivisor divisor) {
        this.divisor = divisor;
        repaint();
    }

    public void setMatriz(int[][] matriz) {
        this.matriz = matriz;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (matriz == null) {
            return;
        }

        int lado = Math.min(getWidth(), getHeight());
        int tamPieza = lado / tamanio;
        if (tamPieza <= 0) {
            return;
        }

        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                int valor = matriz[i][j];
                int x = j * tamPieza;
                int y = i * tamPieza;

                if (valor == 0) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, tamPieza, tamPieza);
                    continue;
                }

                if (divisor != null && divisor.getPieza(valor) != null) {
                    g.drawImage(divisor.getPieza(valor), x, y, tamPieza, tamPieza, null);
                } else {
                    g.setColor(new Color(70, 130, 180));
                    g.fillRect(x, y, tamPieza, tamPieza);
                }

                g.setColor(new Color(30, 30, 30));
                g.drawRect(x, y, tamPieza, tamPieza);
            }
        }
    }
}
