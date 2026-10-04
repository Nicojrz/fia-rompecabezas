package rompecabezas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BusquedaProfundidad {

    private int nodosExplorados;

    public BusquedaProfundidad() {
        this.nodosExplorados = 0;
    }

    public int getNodosExplorados() {
        return nodosExplorados;
    }
    
    public List<Nodo> buscarConLimite(int[][] estadoInicial, int limiteProfundidad) {
        nodosExplorados = 0;
        Nodo raiz = new Nodo(estadoInicial);
        Set<String> enRuta = new HashSet<>();
        enRuta.add(raiz.claveEstado());
        return dfsRecursivo(raiz, limiteProfundidad, enRuta);
    }

    private List<Nodo> dfsRecursivo(Nodo actual, int limiteProfundidad, Set<String> enRuta) {
        nodosExplorados++;

        if (Tablero.esMeta(actual.getMatriz())) {
            return actual.reconstruirCamino();
        }

        if (actual.getProfundidad() >= limiteProfundidad) {
            return null;
        }

        for (String direccion : Tablero.DIRECCIONES) {
            int[][] nuevoEstado = Tablero.mover(actual.getMatriz(), direccion);
            if (nuevoEstado == null) {
                continue;
            }

            Nodo hijo = new Nodo(nuevoEstado, actual, direccion);
            String clave = hijo.claveEstado();

            if (enRuta.contains(clave)) {
                continue;
            }

            actual.agregarSiguiente(hijo);
            enRuta.add(clave);

            List<Nodo> resultado = dfsRecursivo(hijo, limiteProfundidad, enRuta);
            if (resultado != null) {
                return resultado;
            }

            enRuta.remove(clave);
        }

        return null;
    }

    public List<Nodo> buscarConProfundizacionIterativa(int[][] estadoInicial, int limiteMaximo) {
        int totalExplorados = 0;
        for (int limite = 0; limite <= limiteMaximo; limite++) {
            List<Nodo> resultado = buscarConLimite(estadoInicial, limite);
            totalExplorados += nodosExplorados;
            if (resultado != null) {
                nodosExplorados = totalExplorados;
                return resultado;
            }
        }
        nodosExplorados = totalExplorados;
        return new ArrayList<>(); 
    }
}
