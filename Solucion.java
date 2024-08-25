import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una solución del problema de las 8 reinas
 *
 * Representación del tablero:
 *
 * El tablero es una lista de listas de enteros, donde cada lista representa una fila del tablero,
 * cada elemento de la sublista representa una columna
 * y el valor de cada elemento representa si hay una reina en esa posición o no.
 *
 */

public class Solucion {
    private final List<List<Integer>> tablero;
    private Integer aptitud; // es el numero de ataques
    public double probabilidadSeleccion; // Probabilidad de selección para ser padre

    public Solucion() {
        this.tablero = new ArrayList<>();
        inicializarSolucion();
    }

    // Inicializa la solución con 8 reinas en posiciones aleatorias
    private void inicializarSolucion() {
        int reinasTablero = 0;

        llenarTableroSinReinas();

        while (reinasTablero < 8) {
            int fila = (int) (Math.random() * 8);
            int columna = (int) (Math.random() * 8);

            if (tablero.get(fila).get(columna) == 0) {
                tablero.get(fila).set(columna, 1);
                reinasTablero++;
            }
        }
    }

    // LLena el tablero con ceros
    private void llenarTableroSinReinas() {
        while (tablero.size() < 8) {
            List<Integer> fila = new ArrayList<>();
            while (fila.size() < 8) {
                fila.add(0);
            }
            tablero.add(fila);
        }
    }

    public int aptitud() {
        return aptitud;
    }

    public void evaluarAptitud() {
        this.aptitud =  this.calcularNumeroAtaques();
    }

    // Define probabilidad para ser padre
    public void definirProbabilidadSeleccion(double probabilidadSeleccion) {
        this.probabilidadSeleccion = probabilidadSeleccion;
    }

    // Probabilidad de selección para ser padre
    public double probablidadSelecccion() {
        return probabilidadSeleccion;
    }

    public void imprimirTablero() {
        for (List<Integer> integers : tablero) {
            for (Integer integer : integers) {
                System.out.print(integer + " ");
            }
            System.out.println();
        }
    }

    // Remueve una reina aleatoria y agrega una reina en una posición aleatoria
    public  Solucion mutar(float probabilidadMutacion) {
        float probabilidad = (float) Math.random();

        // Se asegura que solo haya 8 reinas en el tablero antes de mutar, remueve el exceso y agrega las que faltan
        // this.reparar();

        var solucionMutada = new Solucion();
        solucionMutada.copiarTablero(this);

        if (probabilidad < probabilidadMutacion) {
            solucionMutada.removerReinaAleatoria();
            solucionMutada.agregarReinaAleatoria();
        }


        // la aptitud se calcula en la linea 140 de Poblacion.java

        return solucionMutada;
    }

    public Solucion cruza(float probabilidadCruza, Solucion pareja) {
        var copiaActual = new Solucion();
        copiaActual.copiarTablero(this);

        double probabilidadMitadTablero = Math.random();

        if (probabilidadMitadTablero < 0.5) {
            float probabilidad = (float) Math.random();

            if (probabilidad < probabilidadCruza) {
                copiaActual.combinarMitadParejaDerecha(pareja);
            }
        } else if (probabilidadMitadTablero > 0.5) {
            float probabilidad = (float) Math.random();

            if (probabilidad < probabilidadCruza) {
                copiaActual.combinarMitadParejaIzquierda(pareja);
            } else {
                copiaActual.copiarTablero(pareja);
            }
        }

        // Reparar la solución para asegurar que solo haya 8 reinas en el tablero
        copiaActual.reparar();

        return copiaActual;
    }


    // Se asegura que solo haya 8 reinas en el tablero
    private void reparar() {
        int numReinas = this.numReinas();

        if (numReinas > 8) {
            while (numReinas > 8) {
                removerReinaAleatoria();
                numReinas--;
            }
        } else if (numReinas < 8) {
            while (numReinas < 8) {
                agregarReinaAleatoria();
                numReinas++;
            }
        }
    }

    private void combinarMitadParejaIzquierda(Solucion pareja) {
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 4; columna++) {
                if (pareja.tablero.get(fila).get(columna) == 1) {
                    this.tablero.get(fila).set(columna, 1);
                }
            }
        }
    }

    private void combinarMitadParejaDerecha(Solucion pareja) {
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 4; columna < 8; columna++) {
                if (pareja.tablero.get(fila).get(columna) == 1) {
                    this.tablero.get(fila).set(columna, 1);
                }
            }
        }
    }

    // remueve una reina elegida aleatoriamente, solo si hay reinas en el tablero
    private void removerReinaAleatoria() {
        if (this.numReinas() == 0)
            return;

        int[] posicion = buscarReinaAleatoria();
        tablero.get(posicion[0]).set(posicion[1], 0);
    }

    // si el tablero tiene menos de 8 reinas, entonces agrega solo una reina en una posición aleatoria
    // Si el table
    private void agregarReinaAleatoria() {
        int fila = (int) (Math.random() * 8);
        int columna = (int) (Math.random() * 8);
        while (tablero.get(fila).get(columna) == 1) {
            fila = (int) (Math.random() * 8);
            columna = (int) (Math.random() * 8);
        }
        this.tablero.get(fila).set(columna, 1);
    }

    // Calcula la cantidad de reinas en el tablero
    private int numReinas() {
        var numReinas = 0;

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                if (tablero.get(fila).get(columna) == 1) {
                    numReinas++;
                }
            }
        }

        return numReinas;
    }

    // Busca una reina aleatoria en el tablero x[0] = fila, x[1] = columna
    private int[] buscarReinaAleatoria() {
        int fila = (int) (Math.random() * 8);
        int columna = (int) (Math.random() * 8);
        while (tablero.get(fila).get(columna) == 0) {
            fila = (int) (Math.random() * 8);
            columna = (int) (Math.random() * 8);
        }
        return new int[] { fila, columna };
    }

    private void copiarTablero(Solucion solucion) {
        int numFilas = solucion.tablero.size();
        for (int fila = 0; fila < numFilas; fila++) {
            int numColumnas = solucion.tablero.get(fila).size();
            for (int columna = 0; columna < numColumnas; columna++) {
                int gen = solucion.tablero.get(fila).get(columna);
                this.tablero.get(fila).set(columna, gen);
            }
        }
    }

    // Calcula la cantidad de ataques que tiene la solución
    public int calcularNumeroAtaques() {
        int ataques = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tablero.get(i).get(j) == 1) {
                    ataques += ataquesFila(i, j);
                    ataques += ataquesColumna(i, j);
                    ataques += ataquesDiagonal(i, j);
                }
            }
        }
        return ataques;
    }

    // Calcula los ataques diagonales de una reina en una posición dada
    private int ataquesDiagonal(int fila, int columna) {
        int ataques = 0;
        ataques += ataquesDiagonalSuperiorDerecha(fila, columna);
        ataques += ataquesDiagonalSuperiorIzquierda(fila, columna);
        ataques += ataquesDiagonalInferiorDerecha(fila, columna);
        ataques += ataquesDiagonalInferiorIzquierda(fila, columna);
        return ataques;
    }

    // Calcula los ataques de una reina en una posición dada en la diagonal inferior izquierda
    private int ataquesDiagonalInferiorIzquierda(int fila, int columna) {
        int ataques = 0;
        int i = fila;
        int j = columna;
        while (i < 7 && j > 0) {
            i++;
            j--;
            if (tablero.get(i).get(j) == 1) {
                ataques++;
            }
        }
        return ataques;
    }

    // Calcula los ataques de una reina en una posición dada en la diagonal inferior derecha
    private int ataquesDiagonalInferiorDerecha(int fila, int columna) {
        int ataques = 0;
        int i = fila;
        int j = columna;
        while (i < 7 && j < 7) {
            i++;
            j++;
            if (tablero.get(i).get(j) == 1) {
                ataques++;
            }
        }
        return ataques;
    }

    // Calcula los ataques de una reina en una posición dada en la diagonal superior izquierda
    private int ataquesDiagonalSuperiorIzquierda(int fila, int columna) {
        int ataques = 0;
        int i = fila;
        int j = columna;
        while (i > 0 && j > 0) {
            i--;
            j--;
            if (tablero.get(i).get(j) == 1) {
                ataques++;
            }
        }
        return ataques;
    }

    // Calcula los ataques de una reina en una posición dada en la diagonal superior derecha
    private int ataquesDiagonalSuperiorDerecha(int fila, int columna) {
        int ataques = 0;
        int i = fila;
        int j = columna;
        while (i > 0 && j < 7) {
            i--;
            j++;
            if (tablero.get(i).get(j) == 1) {
                ataques++;
            }
        }
        return ataques;
    }

    // Calcula los ataques de una reina en una posición dada en la columna
    private int ataquesColumna(int fila, int columna) {
        int ataques = 0;
        for (int i = 0; i < 8; i++) {
            if (i != fila && tablero.get(i).get(columna) == 1) {
                ataques++;
            }
        }
        return ataques;
    }

    // Calcula los ataques en la fila de una reina
    private int ataquesFila(int fila, int columna) {
        int ataques = 0;
        for (int i = 0; i < 8; i++) {
            if (i != columna && tablero.get(fila).get(i) == 1) {
                ataques++;
            }
        }
        return ataques;
    }
}
