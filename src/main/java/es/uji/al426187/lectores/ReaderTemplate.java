package es.uji.al426187.lectores;

import es.uji.al426187.estructuras.Table;

import java.io.IOException;
import java.util.Scanner;

public abstract class ReaderTemplate {

    protected String fichero;

    protected Table tabla;
    protected Scanner sc;


    public ReaderTemplate(String fichero){
        this.fichero = fichero;
        this.tabla = tableCreator();
    }
    public abstract Table tableCreator();

    public abstract void openSource(String source) throws IOException;

    public abstract void processHeaders(String headers);

    public abstract void processData(String data);

    public abstract void closeSource() throws IOException;

    public abstract boolean hasMoreData() throws IOException;

    public abstract String getNextData() throws IOException;

    public final Table readTableFromSource() throws IOException{
        openSource(fichero);
        processHeaders(getNextData());
        while(hasMoreData()){
            processData(getNextData());
        }
        closeSource();
        return tabla;
    }
}
