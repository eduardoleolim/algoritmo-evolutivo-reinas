import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Poblacion {
    private int tamano;
    private float proporcionPadres;
    private int numGeneraciones;
    private float probabilidadMutacion;
    private float probabilidadCruza;

    private List<Solucion> poblacion;
    private List<Solucion> padres;
    private List<Solucion> hijos;

    public Poblacion(int tamano, float proporcionPadres, int numGeneraciones, float probabilidadMutacion, float probabilidadCruza) {
        this.tamano = tamano;
        this.proporcionPadres = proporcionPadres;
        this.numGeneraciones = numGeneraciones;
        this.probabilidadMutacion = probabilidadMutacion;
        this.probabilidadCruza = probabilidadCruza;

        poblacion = new ArrayList<>();
        padres = new ArrayList<>();
        hijos = new ArrayList<>();
    }

    public void ejecutar() {
        int numGeneracionesHechas = 0;
        this.llenarPoblacion(this.tamano); // Crear población
        this.ordenarPoblacion(); // Evaluar

        while (numGeneracionesHechas < this.numGeneraciones && this.poblacion.get(0).calcularNumeroAtaques() != 0) {
            asignarProbabilidadDePadre();

            this.padres = seleccionarPadres();

            this.hijos = procrear(this.padres);

            this.poblacion.addAll(this.hijos);
            this.ordenarPoblacion();

            // this.poblacion = this.poblacion.subList(0, tamano); // Seleccionar población de acuerdo al tamaño

            // Seleccionar mitad de población y generar el resto
            this.seleccionarMejorMitadPoblacion();

            numGeneracionesHechas++;

            System.out.println("\nGeneración: " + numGeneracionesHechas + "\nPoblación: " + this.poblacion.size()
                    + "\nAptitud mejor solución: " + this.poblacion.get(0).aptitud());
        }

        // Solución
        System.out.println("\nAptitud de la solución final: " + this.poblacion.get(0).aptitud());

        System.out.println("\nSoluciones con la menor aptitud: " + this.poblacion.stream().filter(solucion -> solucion.calcularNumeroAtaques() == this.poblacion.get(0).calcularNumeroAtaques()).count());

        System.out.println("\nSoluciones con aptitud cero (0) encontradas: " + this.poblacion.stream().filter(solucion -> solucion.calcularNumeroAtaques() == 0).count());

        this.poblacion.forEach(solucion -> {
            if (solucion.aptitud() == 0) {
                solucion.imprimirTablero();
                System.out.println();
            }
        });
    }

    private void llenarPoblacion(int tamano) {
        for (int i = 0; i < tamano; i++) {
            var solucion = new Solucion();
            solucion.evaluarAptitud();
            this.poblacion.add(solucion);
        }
    }

    // Ordena Población por su aptitud
    private void ordenarPoblacion() {
        this.poblacion.sort(Comparator.comparingInt(Solucion::aptitud));
    }


    private void seleccionarMejorMitadPoblacion() {
        List<Solucion> poblacionTemporal = this.poblacion.subList(0, tamano / 2);

        while (poblacionTemporal.size() < tamano) {
            var solucion = new Solucion();
            solucion.evaluarAptitud();

            poblacionTemporal.add(solucion);
        }

        this.poblacion = poblacionTemporal;
    }


    private void asignarProbabilidadDePadre() {
        int incremento = 1;
        double decremento = (double) 1 / (this.poblacion.size() + 1);

        for (Solucion solucion : this.poblacion) {
            double probabilidad = 1 - (incremento * decremento);
            solucion.definirProbabilidadSeleccion(probabilidad);
            incremento++;
        }
    }

    private List<Solucion> seleccionarPadres() {
        var padres = new ArrayList<Solucion>();
        int cantidadPadres = (int) (this.proporcionPadres * this.poblacion.size());
        while (padres.size() < cantidadPadres) {
            int indice = (int) (Math.random() * this.poblacion.size());
            Solucion candidatoPadre = this.poblacion.get(indice);

            if (padres.contains(candidatoPadre)) {
                continue;
            }

            double probabilidad = Math.random();
            if (probabilidad < candidatoPadre.probablidadSelecccion()) {
                padres.add(candidatoPadre);
            }
        }
        return padres;
    }

    private List<Solucion> procrear(List<Solucion> padres) {
        List<Solucion> hijos = new ArrayList<>();

        int cantidadPares = padres.size() % 2 == 0 ? padres.size() : padres.size() - 1;

        for (int i = 0; i < cantidadPares; i = i + 2) {
            var padre1 = padres.get(i);
            var padre2 = padres.get(i + 1);

            var hijo = padre1.cruza(this.probabilidadCruza, padre2); // El método de reparación se ejecuta al final de la cruza
            hijo.evaluarAptitud();

            var hijoMutado = hijo.mutar(this.probabilidadMutacion); // El método de reparación se ejecuta al final de la mutación
            hijoMutado.evaluarAptitud();

            hijos.add(hijo);
            hijos.add(hijoMutado);
        }

        return hijos;
    }


    public static void main(String[] args) {
        // Tamaño: 50
        // Probabilidad de padre: 40%
        // Generaciones: 10,000
        // Probabilidad cruza: 100%
        // Probabilidad muta: 100%

        /*
         * Tarea
         *
         * Probar con los siguientes cambios para mejorarlo
         *
         * Representación: Cambia forma de representar tablero y forma de contar ataques, de una matriz pasa a un arreglo que guarda la posición de la reina
         *
         * Selección de padres:
         *      Si selección del peor al mejor no funcionó:
         *          1. Asignar probabilidad de padre
         *          2. Seleccionar padres
         *          3. Iterar del peor al mejor padre y seleccionarlos
         *          4. Revolver padres
         *
         * Selección de población: Elegí este
         *      Conservar la mejor mitad de la población y generar aleatoreamente el resto de la población
         *
         *
         * Reporte de 30 ejecuciones de primera solución // entrega 18 de abril
         *
         * Después de aplicar cambio volver a ejecutar 30 ejecuciones y reportar // entrega 25 de abril
         * */

        Poblacion poblacion = new Poblacion(50, (float) 1, 10000, 1, 1);
        poblacion.ejecutar();
    }
}
