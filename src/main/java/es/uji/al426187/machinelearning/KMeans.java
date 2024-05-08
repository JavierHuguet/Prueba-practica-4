// COMENTADO
//
// - ninguna de las pruebas ejecuta (por varios problemas, no solo la ruta), menos kMeans,
// pero las pruebas en kMeanTest no son válidas para comprobar kMeans (ver código)
// - Para hacer bien las pruebas de kMeans, tienes que crear una table con unos pocos datos distribuidos en unos pocos grupos
// claramente distinguibles - y comprobar con 2 (nuevos) puntos muy claramente dentro de un grupo (el estimate tiene que ser igual) o muy
// claramente en diferentes grupos (el estimate tiene que ser diferente); ver enunciado
// - faltan pruebas para la excepción (era opcional - entonces eso no es esencial)
//
// - En resumen: las pruebas no ejecutan (menos kMeans, pero estas pruebas no comprueban el algoritmo bien), hay un problema de rendimiento, y varios otros errores.

package es.uji.al426187.machinelearning;

import es.uji.al426187.distancias.Distance;
import es.uji.al426187.estructuras.Row;
import es.uji.al426187.estructuras.Table;

import java.util.*;


public class KMeans implements Algorithm<Table, Integer, List<Double>> {
    private final int numClusters; //numero de grupos
    private final int numIterations;
    private final long seed;

    private Distance distance;


    private final List<Row> prototipos = new ArrayList<>();

    public KMeans(int numClusters, int numIterations, long seed, Distance distance) {
        this.numClusters = numClusters;
        this.numIterations = numIterations;
        this.seed = seed;
        this.distance = distance;

    }



    @Override
    public void train(Table datos) throws KMeansException {

        if (numClusters > datos.getNumRows()) {
            throw new KMeansException("El número de grupos es mayor que el número en la tabla");
        } else {
            seleccionarPrototiposIniciales(datos); //tantos como grupos
            List<Integer> asignaciones;

            for (int i = 0; i < numIterations; i++) {

                asignaciones = asignarGrupos(datos);
                actualizarCentroides(datos, asignaciones);
            }
        }
    }

    @Override
    public Integer estimate(List<Double> dato) {
        return asignarGrupo(dato);
    }

    public void seleccionarPrototiposIniciales(Table datos) {

        Random rand = new Random(seed);

        for (int i = 0; i < numClusters; i++) {
            int j = rand.nextInt(datos.getNumRows());
            while(prototipos.contains(datos.getRowAt(j))){
                j = rand.nextInt(datos.getNumRows());
            }
            prototipos.add(datos.getRowAt(j));
        }

    }

    public void actualizarCentroides(Table datos, List<Integer> asignaciones) {

        List<Double> zeros = new ArrayList<>(Collections.nCopies(datos.getNumCols(), 0.0));
        List<Double> contprototipos = new ArrayList<>(Collections.nCopies(prototipos.size(), 0.0));

        for (int j = 0; j < prototipos.size(); j++) {
            prototipos.set(j, new Row(zeros));
        }

        int j;
        Row suma;

        for (int i = 0; i < datos.getNumRows(); i++) {
            j = asignaciones.get(i);
            suma = new Row(sumar(prototipos.get(j).getData(), datos.getRowAt(i).getData()));
            prototipos.set(j, suma);

            contprototipos.set(j, contprototipos.get(j) + 1);
        }

        for (int k = 0; k < prototipos.size(); k++) {
            Row prod = new Row(multiplicar(prototipos.get(k).getData(), 1 / contprototipos.get(k)));
            prototipos.set(k, prod);
        }

    }

    List<Double> sumar(List<Double> a, List<Double> b) {

        List<Double> suma = new ArrayList<>();

        for (int i = 0; i < b.size(); i++) {
            suma.add(i, a.get(i) + b.get(i));
        }
        return suma;
    }

    List<Double> multiplicar(List<Double> a, Double factor) {

        ArrayList<Double> multiplica = new ArrayList<>(a.size());

        for (Double aDouble : a) {
            multiplica.add(aDouble * factor);
        }

        return multiplica;
    }


    private List<Integer> asignarGrupos(Table datos) {
        List<Integer> asignaciones = new ArrayList<>();

        for (int i = 0; i < datos.getNumRows(); i++) {

            List<Double> dato_actual = datos.getRowAt(i).getData();

            asignaciones.add(asignarGrupo(dato_actual));
        }
        return asignaciones;
    }


    private Integer asignarGrupo(List<Double> dato){
        double distMin = Double.POSITIVE_INFINITY;
        int grupo = 0;
        Row row = new Row(dato);

        for (int j = 0; j < prototipos.size(); j++) {

            Double dist = distance.calculateDistance(row.getData(), prototipos.get(j).getData());
            if (dist < distMin) {
                grupo = j;
                distMin = dist;

            }
        }
        return grupo;

    }
}
