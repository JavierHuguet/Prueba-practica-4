package es.uji.al426187.machineLearning;

import es.uji.al426187.estructuras.Table;
public interface Algorithm < T extends Table,E,V >{
    void train(T Table) throws KMeansException;
    E estimate(V valor);
}