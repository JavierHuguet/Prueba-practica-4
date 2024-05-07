import es.uji.al426187.distancias.EuclideanDistance;
import es.uji.al426187.estructuras.Table;
import es.uji.al426187.lectores.CSV;
import es.uji.al426187.machineLearning.Algorithm;
import es.uji.al426187.machineLearning.KMeans;
import es.uji.al426187.machineLearning.KNN;
import es.uji.al426187.recomendaciones.RecSys;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
class SongRecSys {
    private RecSys recsys;


    SongRecSys(String method) throws Exception {
        String sep = System.getProperty("file.separator");
        String ruta = "src"+sep+"main"+sep+"resources"+sep+"songs_files";

        // File names (could be provided as arguments to the constructor to be more general)
        Map<String,String> filenames = new HashMap<>();
        filenames.put("knn"+"train",ruta+sep+"songs_train.csv");
        filenames.put("knn"+"test",ruta+sep+"songs_test.csv");
        filenames.put("kmeans"+"train",ruta+sep+"songs_train_withoutnames.csv");
        filenames.put("kmeans"+"test",ruta+sep+"songs_test_withoutnames.csv");



        Map<String, Algorithm> algorithms = new HashMap<>();
        algorithms.put("knn",new KNN(new EuclideanDistance()));
        algorithms.put("kmeans", (Algorithm) new KMeans(15, 200, 4321, new EuclideanDistance()));

        // Tables
        Map<String, Table> tables = new HashMap<>();
        String [] stages = {"train", "test"};
        CSV csv = new CSV();
        for (String stage : stages) {
            tables.put("knn" + stage, csv.readTableWithLabels(filenames.get("knn" + stage)));
            tables.put("kmeans" + stage, csv.readTable(filenames.get("kmeans" + stage)));
        }

        // Names of items
        List<String> names = readNames(ruta+sep+"songs_test_names.csv");

        // Start the RecSys
        this.recsys = new RecSys(algorithms.get(method));
        this.recsys.train(tables.get(method+"train"));
        this.recsys.run(tables.get(method+"test"), names);

        // Given a liked item, ask for a number of recomendations
        String liked_name = "Lootkemia";
        String liked_name2 = "FASTER";
        String liked_name3 = "Techno / Minimal / Hardcore/ Melodic / Dark";

        List<String> recommended_items = this.recsys.recommend(liked_name,5);
        reportRecommendation(liked_name,recommended_items);

        List<String> recommended_items2 = this.recsys.recommend(liked_name2,5);
        reportRecommendation(liked_name2,recommended_items2);

        List<String> recommended_items3 = this.recsys.recommend(liked_name3,5);

        // Display the recommendation text (to be replaced with graphical display with JavaFX implementation)

        reportRecommendation(liked_name3,recommended_items3);

    }

    private List<String> readNames(String fileOfItemNames) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileOfItemNames));
        String line;
        List<String> names = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            names.add(line);
        }
        br.close();
        return names;
    }

    private void reportRecommendation(String liked_name, List<String> recommended_items) {
        System.out.println("If you liked \""+liked_name+"\" then you might like:");
        for (String name : recommended_items)
        {

            System.out.println("\t * "+name);
        }
    }

    public static void main(String[] args) throws Exception {
        new SongRecSys("knn");

        new SongRecSys("kmeans");
    }
}