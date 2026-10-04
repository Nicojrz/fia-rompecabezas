package rompecabezas;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImagenDivisor {

    private final Map<Integer, BufferedImage> piezas;

    public ImagenDivisor(File archivoImagen, int tamanioTablero, int resolucionPiezaPx) throws IOException {
        BufferedImage original = ImageIO.read(archivoImagen);
        if (original == null) {
            throw new IOException("El archivo no es una imagen legible: " + archivoImagen.getAbsolutePath());
        }

        int ladoTotal = tamanioTablero * resolucionPiezaPx;
        BufferedImage escalada = escalarYRecortarCuadrada(original, ladoTotal);

        this.piezas = new HashMap<>();
        int anchoPieza = escalada.getWidth() / tamanioTablero;
        int altoPieza = escalada.getHeight() / tamanioTablero;

        int valor = 1;
        for (int i = 0; i < tamanioTablero; i++) {
            for (int j = 0; j < tamanioTablero; j++) {
                if (i == tamanioTablero - 1 && j == tamanioTablero - 1) {
                    continue; // esta casilla es el hueco (0), no lleva imagen
                }
                BufferedImage sub = escalada.getSubimage(
                        j * anchoPieza, i * altoPieza, anchoPieza, altoPieza);
                piezas.put(valor, sub);
                valor++;
            }
        }
    }

    private BufferedImage escalarYRecortarCuadrada(BufferedImage origen, int lado) {
        int anchoOrig = origen.getWidth();
        int altoOrig = origen.getHeight();
        int menor = Math.min(anchoOrig, altoOrig);

        int x = (anchoOrig - menor) / 2;
        int y = (altoOrig - menor) / 2;
        BufferedImage cuadrada = origen.getSubimage(x, y, menor, menor);

        Image escalada = cuadrada.getScaledInstance(lado, lado, Image.SCALE_SMOOTH);
        BufferedImage resultado = new BufferedImage(lado, lado, BufferedImage.TYPE_INT_RGB);
        resultado.getGraphics().drawImage(escalada, 0, 0, null);
        return resultado;
    }

    public BufferedImage getPieza(int valor) {
        return piezas.get(valor);
    }
}
