package rompecabezas;

import java.util.ArrayList;
import java.util.List;

public class Nodo {

    private int[][] matriz;
    private Nodo anterior;
    private List<Nodo> siguientes;
    private int profundidad;
    private String movimiento;

    public Nodo(int[][] matriz) {
        this.matriz = matriz;
        this.anterior = null;
        this.siguientes = new ArrayList<>();
        this.profundidad = 0;
        this.movimiento = null;
    }

    public Nodo(int[][] matriz, Nodo anterior, String movimiento) {
        this.matriz = matriz;
        this.anterior = anterior;
        this.siguientes = new ArrayList<>();
        this.movimiento = movimiento;
        this.profundidad = (anterior != null) ? anterior.getProfundidad() + 1 : 0;
    }

    public int[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(int[][] matriz) {
        this.matriz = matriz;
    }

    public Nodo getAnterior() {
        return anterior;
    }

    public void setAnterior(Nodo anterior) {
        this.anterior = anterior;
    }

    public List<Nodo> getSiguientes() {
        return siguientes;
    }

    public void agregarSiguiente(Nodo hijo) {
        this.siguientes.add(hijo);
    }

    public int getProfundidad() {
        return profundidad;
    }

    public String getMovimiento() {
        return movimiento;
    }


    public String claveEstado() {
        StringBuilder sb = new StringBuilder();
        for (int[] fila : matriz) {
            for (int valor : fila) {
                sb.append(valor).append(',');
            }
        }
        return sb.toString();
    }

    public List<Nodo> reconstruirCamino() {
        List<Nodo> camino = new ArrayList<>();
        Nodo actual = this;
        while (actual != null) {
            camino.add(0, actual);
            actual = actual.getAnterior();
        }
        return camino;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Nodo)) return false;
        Nodo otro = (Nodo) obj;
        return this.claveEstado().equals(otro.claveEstado());
    }

    @Override
    public int hashCode() {
        return claveEstado().hashCode();
    }

    public void imprimir() {
        for (int[] fila : matriz) {
            for (int valor : fila) {
                System.out.print((valor == 0 ? "-" : String.valueOf(valor)) + " ");
            }
            System.out.println();
        }
    }
}
