package es.uji.al426187.lectores;

import es.uji.al426187.estructuras.Table;
import es.uji.al426187.estructuras.TableWithLabels;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CSVLableledFileReader extends CSVUnlabeledFileReader{


    public CSVLableledFileReader(String fichero){
        super(fichero);

    }
    @Override
    public Table tableCreator(){ return new TableWithLabels();    }


    @Override
    public void processData(String data) {
        String[] elementos = data.split(",");
        String[] datos = Arrays.copyOfRange(elementos, 0, elementos.length - 1 );
        List<Double> datas = new LinkedList<>();

        for (String s : datos) {
            datas.add(Double.parseDouble(s));
        }

        String etiqueta = elementos[elementos.length - 1];
        tabla.addRow(datas, etiqueta);
    }
}
