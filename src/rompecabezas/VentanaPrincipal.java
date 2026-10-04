package rompecabezas;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private static final int LIMITE_PROFUNDIDAD_DFS = 20;

    private final PanelRompecabezas panel;
    private final JButton btnRevolver;
    private final JButton btnBfs;
    private final JButton btnDfs;
    private final JButton btnImagen;
    private final JSpinner spinnerPasos;
    private final JLabel lblEstado;

    private int[][] estadoActual;

    public VentanaPrincipal() {
        super("Rompecabezas " + Tablero.TAMANIO + "x" + Tablero.TAMANIO);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        estadoActual = Tablero.matrizMeta();

        panel = new PanelRompecabezas(Tablero.TAMANIO);
        panel.setMatriz(estadoActual);
        panel.setPreferredSize(new Dimension(640, 640));

        btnImagen = new JButton("Cargar imagen...");
        btnRevolver = new JButton("Revolver");
        btnBfs = new JButton("Resolver (BFS)");
        btnDfs = new JButton("Resolver (DFS/IDDFS)");
        spinnerPasos = new JSpinner(new SpinnerNumberModel(6, 1, 16, 1));
        spinnerPasos.setToolTipText("Búsqueda ciega (BFS/DFS)");
        lblEstado = new JLabel("Cargue una imagen y presione Revolver.");

        btnImagen.addActionListener(e -> cargarImagen());
        btnRevolver.addActionListener(e -> revolver());
        btnBfs.addActionListener(e -> resolver(true));
        btnDfs.addActionListener(e -> resolver(false));

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        controles.add(btnImagen);
        controles.add(new JLabel("Pasos de revoltura:"));
        controles.add(spinnerPasos);
        controles.add(btnRevolver);
        controles.add(btnBfs);
        controles.add(btnDfs);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(controles, BorderLayout.SOUTH);
        add(lblEstado, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void cargarImagen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecciona la imagen de fondo del rompecabezas");
        int resultado = chooser.showOpenDialog(this);
        if (resultado != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File archivo = chooser.getSelectedFile();
        try {
            ImagenDivisor divisor = new ImagenDivisor(archivo, Tablero.TAMANIO, 40);
            panel.setDivisor(divisor);
            lblEstado.setText("Imagen cargada: " + archivo.getName());
        } catch (Exception ex) {
            lblEstado.setText("Error al cargar la imagen: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar la imagen:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void revolver() {
        int pasos = (Integer) spinnerPasos.getValue();
        Mezclador mezclador = new Mezclador();
        estadoActual = mezclador.revolver(pasos);
        panel.setMatriz(estadoActual);
        lblEstado.setText("Rompecabezas revuelto con " + pasos + " movimientos.");
    }

    private void establecerControlesHabilitados(boolean habilitado) {
        btnRevolver.setEnabled(habilitado);
        btnBfs.setEnabled(habilitado);
        btnDfs.setEnabled(habilitado);
        btnImagen.setEnabled(habilitado);
        spinnerPasos.setEnabled(habilitado);
    }

    private void resolver(boolean usarBfs) {
        if (Tablero.esMeta(estadoActual)) {
            lblEstado.setText("El rompecabezas ya está resuelto.");
            return;
        }

        establecerControlesHabilitados(false);
        lblEstado.setText(usarBfs ? "Resolviendo con BFS..." : "Resolviendo con DFS/IDDFS...");

        final int[][] estadoParaResolver = estadoActual;

        SwingWorker<List<Nodo>, Void> worker = new SwingWorker<List<Nodo>, Void>() {
            private int nodosExplorados;
            private long tiempoMs;
            private String error;

            @Override
            protected List<Nodo> doInBackground() {
                long inicio = System.currentTimeMillis();
                List<Nodo> camino = null;
                try {
                    if (usarBfs) {
                        BusquedaAnchura bfs = new BusquedaAnchura();
                        camino = bfs.buscar(estadoParaResolver);
                        nodosExplorados = bfs.getNodosExplorados();
                    } else {
                        BusquedaProfundidad dfs = new BusquedaProfundidad();
                        camino = dfs.buscarConProfundizacionIterativa(estadoParaResolver, LIMITE_PROFUNDIDAD_DFS);
                        nodosExplorados = dfs.getNodosExplorados();
                    }
                } catch (OutOfMemoryError oom) {
                    error = "Memoria insuficiente: la revoltura es demasiado profunda para una "
                            + "búsqueda no informada en un tablero de " + Tablero.TAMANIO + "x" + Tablero.TAMANIO
                            + ". Prueba con menos pasos de revoltura.";
                }
                tiempoMs = System.currentTimeMillis() - inicio;
                return camino;
            }

            @Override
            protected void done() {
                if (error != null) {
                    lblEstado.setText(error);
                    establecerControlesHabilitados(true);
                    return;
                }
                try {
                    List<Nodo> camino = get();
                    if (camino == null || camino.isEmpty()) {
                        lblEstado.setText("No se encontró solución dentro del límite establecido.");
                        establecerControlesHabilitados(true);
                        return;
                    }
                    animarCamino(camino, nodosExplorados, tiempoMs);
                } catch (Exception ex) {
                    lblEstado.setText("Error durante la búsqueda: " + ex.getMessage());
                    establecerControlesHabilitados(true);
                }
            }
        };
        worker.execute();
    }

    private void animarCamino(List<Nodo> camino, int nodosExplorados, long tiempoMs) {
        final int[] indice = {0};
        Timer timer = new Timer(120, null);

        timer.addActionListener(e -> {
            if (indice[0] >= camino.size()) {
                timer.stop();
                estadoActual = camino.get(camino.size() - 1).getMatriz();
                lblEstado.setText("Resuelto en " + (camino.size() - 1) + " movimientos. "
                        + "Nodos explorados: " + nodosExplorados + ". Tiempo: " + tiempoMs + " ms.");
                establecerControlesHabilitados(true);
                return;
            }
            panel.setMatriz(camino.get(indice[0]).getMatriz());
            indice[0]++;
        });
        timer.start();
    }
}
