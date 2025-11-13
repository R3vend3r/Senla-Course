import Assembly.AssemblingParts;
import Product.Product;
import Step.EngineStep;
import Step.HullStep;
import Step.TurrelStep;

public class Main {
    public static void main(String[] args) {
        System.out.println("Запуск производственного процесса");
        EngineStep engineStep = new EngineStep();
        TurrelStep turrelStep = new TurrelStep();
        HullStep hullStep = new HullStep();
        Product tank = new Product();
        AssemblingParts parts = new AssemblingParts(engineStep,hullStep,turrelStep);
        parts.assembleProduct(tank);


    }
}
