import java.util.List;

public final class Main {

    public static void main(String[] args) throws Exception {
        DataModel dataModel = new DataModel();
        BinPacking binPacking = new BinPacking(dataModel);
        System.out.println(BinUtilities.getLowerBound(dataModel));

        long startTime = System.nanoTime();
//        try {
//            BinPackingMip.main(dataModel);
//        }
//        catch (Exception ignored) {
//
//        }
        List<Bin> binsListFirstFitDecreasing = binPacking.firstFit(binPacking.getDecreasingArray());
        System.out.println(binsListFirstFitDecreasing + ", with " + binsListFirstFitDecreasing.size() + " bins");


        List<Bin> binsListRandomOneToOne = binPacking.getOneToOneBin(binPacking.getRandomArray());
        System.out.println(binsListRandomOneToOne + ", with " + binsListRandomOneToOne.size() + " bins");


        List<Bin> binsListFirstFitRandom = binPacking.firstFit(binPacking.getRandomArray());
        System.out.println(binsListFirstFitRandom + ", with " + binsListFirstFitRandom.size() + " bins");

        long duration = (System.nanoTime() - startTime) / 1000000;
        System.out.println("Executed in " + duration + " ms");
        System.exit(0); // close filedialog
    }
}

//Loader.loadNativeLibraries();
//// Create the linear solver with the GLOP backend.
//MPSolver solver = MPSolver.createSolver("GLOP");
//
//// Create the variables x and y.
//MPVariable x = solver.makeNumVar(0.0, 1.0, "x");
//MPVariable y = solver.makeNumVar(0.0, 2.0, "y");
//
//System.out.println("Number of variables = " + solver.numVariables());
//
//// Create a linear constraint, 0 <= x + y <= 2.
//MPConstraint ct = solver.makeConstraint(0.0, 2.0, "ct");
//ct.setCoefficient(x, 1);
//ct.setCoefficient(y, 1);
//
//System.out.println("Number of constraints = " + solver.numConstraints());
//
//// Create the objective function, 3 * x + y.
//MPObjective objective = solver.objective();
//objective.setCoefficient(x, 3);
//objective.setCoefficient(y, 1);
//objective.setMaximization();
//
//solver.solve();
//
//System.out.println("Solution:");
//System.out.println("Objective value = " + objective.value());
//System.out.println("x = " + x.solutionValue());
//System.out.println("y = " + y.solutionValue());