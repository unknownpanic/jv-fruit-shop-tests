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

class SupplyOperationTest {
    private OperationHandler supplyOperation;
    private StorageDao storageDao;

    @BeforeEach
    void setUp() {
        storageDao = new StorageDaoImpl();
        supplyOperation = new SupplyOperation(storageDao);
    }

    @AfterEach
    void tearDown() {
        Storage.getModifiableLeftovers().clear();
    }

    @Test
    void apply_existingFruit_Ok() {
        Fruit banana = new Fruit("banana");
        storageDao.save(banana, 50);

        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.SUPPLY, banana, 30);

        supplyOperation.apply(transaction);

        assertEquals(80, storageDao.get(banana));
    }

    @Test
    void apply_newFruit_Ok() {
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.SUPPLY,
                new Fruit("apple"),
                100);

        supplyOperation.apply(transaction);

        assertEquals(100, storageDao.get(new Fruit("apple")));
    }

    @Test
    void apply_negativeQuantity_NotOk() {
        FruitTransaction invalid = new FruitTransaction(
                FruitTransaction.Operation.SUPPLY,
                new Fruit("pear"),
                -5);

        assertThrows(IllegalArgumentException.class,
                () -> supplyOperation.apply(invalid));
    }
}
