package Assembly;

import Assembly.IAssemblyLine;
import Product.IProduct;
import ProductPart.Engine;
import ProductPart.Hull;
import ProductPart.Turrel;
import Step.EngineStep;
import Step.HullStep;
import Step.TurrelStep;

public class AssemblingParts implements IAssemblyLine {
    private final Engine engine;
    private final Turrel tunnel;
    private final Hull hull;

    public AssemblingParts(EngineStep engineStep, HullStep hullStep, TurrelStep tunnelStep) {
        this.engine = (Engine) engineStep.buildProductPart();
        this.hull = (Hull) hullStep.buildProductPart();
        this.tunnel = (Turrel) tunnelStep.buildProductPart();
    }

    @Override
    public IProduct assembleProduct(IProduct product) {
        System.out.println("Начало сборки");
        product.installFirstPart(hull);
        product.installSecondPart(engine);
        product.installThirdPart(tunnel);
        return product;
    }

}