package rompecabezas;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class BusquedaAnchura {

    private int nodosExplorados;

    public BusquedaAnchura() {
        this.nodosExplorados = 0;
    }

    public int getNodosExplorados() {
        return nodosExplorados;
    }

    public List<Nodo> buscar(int[][] estadoInicial) {
        nodosExplorados = 0;
        Nodo raiz = new Nodo(estadoInicial);

        if (Tablero.esMeta(raiz.getMatriz())) {
            return raiz.reconstruirCamino();
        }

        Queue<Nodo> frontera = new ArrayDeque<>();
        Set<String> visitados = new HashSet<>();

        frontera.add(raiz);
        visitados.add(raiz.claveEstado());

        while (!frontera.isEmpty()) {
            Nodo actual = frontera.poll();
            nodosExplorados++;

            for (String direccion : Tablero.DIRECCIONES) {
                int[][] nuevoEstado = Tablero.mover(actual.getMatriz(), direccion);
                if (nuevoEstado == null) {
                    continue;
                }

                Nodo hijo = new Nodo(nuevoEstado, actual, direccion);
                String clave = hijo.claveEstado();

                if (visitados.contains(clave)) {
                    continue;
                }

                actual.agregarSiguiente(hijo);

                if (Tablero.esMeta(nuevoEstado)) {
                    return hijo.reconstruirCamino();
                }

                visitados.add(clave);
                frontera.add(hijo);
            }
        }

        return null;
    }
}
