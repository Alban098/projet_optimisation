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
        DataModel dataModel;
        wpExcel.append("files");
        wpExcel.append(',');
        wpExcel.append("nombre de bins SA");
        wpExcel.append(',');
        wpExcel.append("fitness random firstfit SA");
        wpExcel.append(',');
        wpExcel.append("nombre de bins SA");
        wpExcel.append(',');
        wpExcel.append("fitness random one2one SA");
        wpExcel.append(',');
        wpExcel.append("nombre de bins TS");
        wpExcel.append(',');
        wpExcel.append("fitness random firstfit TS");
        wpExcel.append(',');
        wpExcel.append("nombre de bins TS");
        wpExcel.append(',');
        wpExcel.append("fitness random one2one TS");
        wpExcel.append(',');
        wpExcel.append("nombre de bins");
        wpExcel.append(',');
        wpExcel.append("fitness random firstfit TS");
        wpExcel.append(',');
        wpExcel.append("borne inférieure");
        wpExcel.append('\n');

        try (Stream<Path> walk = Files.walk(Paths.get("D:\\Shared\\COURS\\S3\\Optimisation Discrète\\projet_optimisation\\data"))) {
            List<String> result = walk.map(Path::toString)
                    .filter(f -> f.endsWith(".txt")).collect(Collectors.toList());

//            Main.graphe = new ArrayList<>();
            for (String fileName : result) {
                List<String> temp = Arrays.asList(fileName.split("\\\\"));
                String name = temp.get(temp.size() - 1);
                wpExcel.append(name);
                wpExcel.append(',');

                dataModel = new DataModel(fileName);

                long nff = 0, ntsff = 0, nsaff = 0, ntsoo = 0, nsaoo = 0;
                long fff = 0, ftsff = 0, fsaff = 0, ftsoo = 0, fsaoo = 0;
                long tff = 0, ttsff = 0, tsaff = 0, ttsoo = 0, tsaoo = 0;


                for (int i = 0; i < 50; i++){
                    dataModel.randomizeArray();
                    BinsList binsListff = BinUtilities.firstFit(dataModel);
                    dataModel.randomizeArray();
                    BinsList binsListoo = BinUtilities.oneToOneBin(dataModel);
                    tff -= System.currentTimeMillis();
                    BinsList ff = BinUtilities.firstFit(dataModel);
                    tff += System.currentTimeMillis();
                    ttsff -= System.currentTimeMillis();
                    BinsList tsff = binsListff.tabuSearch(100, 5);
                    ttsff += System.currentTimeMillis();
                    dataModel.randomizeArray();
                    binsListff = BinUtilities.firstFit(dataModel);
                    tsaff -= System.currentTimeMillis();
                    BinsList saff = binsListff.simulatedAnnealing(50, 50, 1000, 0.9);
                    tsaff += System.currentTimeMillis();
                    ttsoo -= System.currentTimeMillis();
                    BinsList tsoo = binsListoo.tabuSearch(100, 5);
                    ttsoo += System.currentTimeMillis();
                    dataModel.randomizeArray();
                    binsListoo = BinUtilities.oneToOneBin(dataModel);
                    tsaoo -= System.currentTimeMillis();
                    BinsList saoo = binsListoo.simulatedAnnealing(50, 50, 1000, 0.9);
                    tsaoo += System.currentTimeMillis();
                    nff += ff.getSize();
                    ntsff += tsff.getSize();
                    nsaff += saff.getSize();
                    ntsoo += tsoo.getSize();
                    nsaoo += saoo.getSize();

                    fff += ff.calculate();
                    ftsff += tsff.calculate();
                    fsaff += saff.calculate();
                    ftsoo += tsoo.calculate();
                    fsaoo += saoo.calculate();

                }

                wpExcel.append(nff/50.).append(",");
                wpExcel.append(fff/50.).append(",");
                wpExcel.append(ntsff/50.).append(",");
                wpExcel.append(ftsff/50.).append(",");
                wpExcel.append(nsaff/50.).append(",");
                wpExcel.append(fsaff/50.).append(",");
                wpExcel.append(ntsoo/50.).append(",");
                wpExcel.append(ftsoo/50.).append(",");
                wpExcel.append(nsaoo/50.).append(",");
                wpExcel.append(fsaoo/50.).append(",");
                wpExcel.append(BinUtilities.getLowerBound(dataModel)).append(",");

                wpExcel.append('\n');
                System.out.println(name + " " + nff/50.+ " " +fff/50.+ " " +ntsff/50.+ " " +ftsff/50.+ " " +nsaff/50.+ " " +fsaff/50.+ " " +ntsoo/50.+ " " +ftsoo/50.+ " " +nsaoo/50.+ " " +fsaoo/50.);
                System.out.println(tff/50. + " " + ttsff/50. + " " + tsaff/50. + " " + ttsoo/50. + " " + tsaoo/50.);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter("test_comparaison.csv")) {
            writer.write(wpExcel.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("done!");
    }
}
