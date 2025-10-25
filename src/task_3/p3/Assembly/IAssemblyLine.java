package Assembly;

import Product.IProduct;

public interface IAssemblyLine {
    IProduct assembleProduct(IProduct a);
}