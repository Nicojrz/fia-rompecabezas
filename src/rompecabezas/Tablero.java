package rompecabezas;

public class Tablero {

    public static int TAMANIO = 4;

    public static int[][] matrizMeta() {
        int[][] meta = new int[TAMANIO][TAMANIO];
        int valor = 1;
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                if (i == TAMANIO - 1 && j == TAMANIO - 1) {
                    meta[i][j] = 0;
                } else {
                    meta[i][j] = valor++;
                }
            }
        }
        return meta;
    }

    public static int[][] copiarMatriz(int[][] original) {
        int[][] copia = new int[TAMANIO][TAMANIO];
        for (int i = 0; i < TAMANIO; i++) {
            System.arraycopy(original[i], 0, copia[i], 0, TAMANIO);
        }
        return copia;
    }

    public static boolean esMeta(int[][] matriz) {
        int[][] meta = matrizMeta();
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                if (matriz[i][j] != meta[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int[] localizarVacio(int[][] matriz) {
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                if (matriz[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        throw new IllegalStateException("La matriz no contiene espacio vacío (0)");
    }

    public static int[][] mover(int[][] matriz, String direccion) {
        int[] pos = localizarVacio(matriz);
        int fila = pos[0];
        int columna = pos[1];
        int nuevaFila = fila;
        int nuevaColumna = columna;

        switch (direccion) {
            case "ARRIBA":
                nuevaFila = fila - 1;
                break;
            case "ABAJO":
                nuevaFila = fila + 1;
                break;
            case "IZQUIERDA":
                nuevaColumna = columna - 1;
                break;
            case "DERECHA":
                nuevaColumna = columna + 1;
                break;
            default:
                throw new IllegalArgumentException("Dirección inválida: " + direccion);
        }

        if (nuevaFila < 0 || nuevaFila >= TAMANIO || nuevaColumna < 0 || nuevaColumna >= TAMANIO) {
            return null;
        }

        int[][] nueva = copiarMatriz(matriz);
        nueva[fila][columna] = nueva[nuevaFila][nuevaColumna];
        nueva[nuevaFila][nuevaColumna] = 0;
        return nueva;
    }

    public static final String[] DIRECCIONES = {"ARRIBA", "ABAJO", "IZQUIERDA", "DERECHA"};

    public static void imprimir(int[][] matriz) {
        for (int[] fila : matriz) {
            StringBuilder sb = new StringBuilder();
            for (int valor : fila) {
                sb.append(valor == 0 ? "-" : String.valueOf(valor)).append('\t');
            }
            System.out.println(sb.toString().trim());
        }
    }
}
