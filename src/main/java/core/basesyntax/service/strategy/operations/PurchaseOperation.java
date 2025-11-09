package core.basesyntax.service.strategy.operations;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.model.Fruit;
import core.basesyntax.model.FruitTransaction;

public class PurchaseOperation implements OperationHandler {
    private final StorageDao storageDao;

    public PurchaseOperation(StorageDao storageDao) {
        this.storageDao = storageDao;
    }

    @Override
    public void apply(FruitTransaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction was null.");
        }
        if (transaction.getQuantity() < 0) {
            throw new IllegalArgumentException("Transaction quantity can't be negative.");
        }
        Fruit fruit = transaction.getFruit();
        int availableQuantity = storageDao.getAll().getOrDefault(fruit, 0);
        Integer newQuantity = availableQuantity - transaction.getQuantity();
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Not enough " + fruit.getFruitName()
                    + " in the storage");
        }
        storageDao.save(fruit, newQuantity);
    }
}
