import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

/** Bin packing problem. */
public class BinPackingMip {
//    static class DataModel {
//        public final double[] weights = {48, 30, 19, 36, 36, 27, 42, 42, 36, 24, 30};
//        public final int numItems = weights.length;
//        public final int numBins = weights.length;
//        public final int binCapacity = 100;
//    }

    static BinsList getOptimal(DataModel data) throws Exception {
        Loader.loadNativeLibraries();

        // Create the linear solver with the SCIP backend.
        MPSolver solver = MPSolver.createSolver("SCIP");
        if (solver == null) {
            System.out.println("Could not create solver SCIP");
            return new BinsList(null, -1);
        }

        MPVariable[][] x = new MPVariable[data.numItems][data.numBins];
        for (int i = 0; i < data.numItems; ++i) {
            for (int j = 0; j < data.numBins; ++j) {
                x[i][j] = solver.makeIntVar(0, 1, "");
            }
        }
        MPVariable[] y = new MPVariable[data.numBins];
        for (int j = 0; j < data.numBins; ++j) {
            y[j] = solver.makeIntVar(0, 1, "");
        }

        double infinity = java.lang.Double.POSITIVE_INFINITY;
        for (int i = 0; i < data.numItems; ++i) {
            MPConstraint constraint = solver.makeConstraint(1, 1, "");
            for (int j = 0; j < data.numBins; ++j) {
                constraint.setCoefficient(x[i][j], 1);
            }
        }
        // The bin capacity contraint for bin j is
        //   sum_i w_i x_ij <= C*y_j
        // To define this constraint, first subtract the left side from the right to get
        //   0 <= C*y_j - sum_i w_i x_ij
        //
        // Note: Since sum_i w_i x_ij is positive (and y_j is 0 or 1), the right side must
        // be less than or equal to C. But it's not necessary to add this constraint
        // because it is forced by the other constraints.

        for (int j = 0; j < data.numBins; ++j) {
            MPConstraint constraint = solver.makeConstraint(0, infinity, "");
            constraint.setCoefficient(y[j], data.binCapacity);
            for (int i = 0; i < data.numItems; ++i) {
                constraint.setCoefficient(x[i][j], -data.weights.get(i));
            }
        }

        MPObjective objective = solver.objective();
        for (int j = 0; j < data.numBins; ++j) {
            objective.setCoefficient(y[j], 1);
        }
        objective.setMinimization();

        final MPSolver.ResultStatus resultStatus = solver.solve();

        if (resultStatus == MPSolver.ResultStatus.OPTIMAL)
            return new BinsList(x, y, data.weights, data.binCapacity);
        else
            System.err.println("The problem does not have an optimal solution.");
        return new BinsList(null, -1);
    }
    private BinPackingMip() {}
}