package core.basesyntax.service.strategy.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.model.Fruit;
import core.basesyntax.model.FruitTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PurchaseOperationTest {
    private OperationHandler purchaseOperation;
    private StorageDao storageDao;

    @BeforeEach
    void setUp() {
        storageDao = new StorageDaoImpl();
        purchaseOperation = new PurchaseOperation(storageDao);
    }

    @AfterEach
    void tearDown() {
        Storage.getModifiableLeftovers().clear();
    }

    @Test
    void apply_existingFruit_Ok() {
        Fruit banana = new Fruit("banana");
        storageDao.save(banana, 100);

        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, banana, 30);

        purchaseOperation.apply(transaction);

        assertEquals(70, storageDao.get(banana));
    }

    @Test
    void apply_nonExistingFruit_NotOk() {
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE,
                new Fruit("apple"),
                10);

        assertThrows(IllegalArgumentException.class,
                () -> purchaseOperation.apply(transaction));
    }

    @Test
    void apply_negativeQuantity_NotOk() {
        Fruit banana = new Fruit("banana");
        storageDao.save(banana, 50);

        FruitTransaction invalid = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, banana, -10);

        assertThrows(IllegalArgumentException.class,
                () -> purchaseOperation.apply(invalid));
    }

    @Test
    void apply_insufficientStock_NotOk() {
        Fruit banana = new Fruit("banana");
        storageDao.save(banana, 20);

        FruitTransaction invalid = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, banana, 25);

        assertThrows(IllegalArgumentException.class,
                () -> purchaseOperation.apply(invalid));
    }
}
