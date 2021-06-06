import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainCSV {
    public static void main(String[] args) {
        StringBuilder wpExcel = new StringBuilder();
        wpExcel.append("files");
        wpExcel.append(',');
        wpExcel.append("max time");
        wpExcel.append(',');
        wpExcel.append("min time");
        wpExcel.append(',');
        wpExcel.append("avg time");
        wpExcel.append(',');
        wpExcel.append("max time");
        wpExcel.append(',');
        wpExcel.append("min time");
        wpExcel.append(',');
        wpExcel.append("avg time");
        wpExcel.append(',');
        wpExcel.append("max time");
        wpExcel.append(',');
        wpExcel.append("min time");
        wpExcel.append(',');
        wpExcel.append("avg time");
        wpExcel.append(',');
        wpExcel.append("degree");
        wpExcel.append(',');
        wpExcel.append("weight");
        wpExcel.append('\n');

        try (Stream<Path> walk = Files.walk(Paths.get("C:\\PythonScripts\\projet_optimisation\\data"))) {
            List<String> result = walk.map(Path::toString)
                    .filter(f -> f.endsWith(".txt")).collect(Collectors.toList());

//            Main.graphe = new ArrayList<>();
            for (String fileName : result) {
                List<String> temp = Arrays.asList(fileName.split("\\\\"));
                String name = temp.get(temp.size() - 1);
                System.out.println(name);
                wpExcel.append(name);
                wpExcel.append(',');

//                wpExcel.append(maxt_kruskal1).append(",");
//                wpExcel.append(mint_kruskal1).append(",");
//                wpExcel.append(avgt_kruskal1/a).append(",");
//                wpExcel.append(maxt_kruskal2).append(",");
//                wpExcel.append(mint_kruskal2).append(",");
//                wpExcel.append(avgt_kruskal2/a).append(",");
//                wpExcel.append(maxt_prim).append(",");
//                wpExcel.append(mint_prim).append(",");
//                wpExcel.append(avgt_prim/a).append(",");
//                wpExcel.append((int) d_kruskal1).append(",");
//                wpExcel.append((int) w_kruskal1).append(",");
                wpExcel.append('\n');
                }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter("test_k1k2prim.csv")) {
            writer.write(wpExcel.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("done!");
    }
}
