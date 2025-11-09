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

class ReturnOperationTest {
    private OperationHandler returnOperation;
    private StorageDao storageDao;

    @BeforeEach
    void setUp() {
        storageDao = new StorageDaoImpl();
        returnOperation = new ReturnOperation(storageDao);
    }

    @AfterEach
    void tearDown() {
        Storage.getModifiableLeftovers().clear();
    }

    @Test
    void apply_existingFruit_Ok() {
        Fruit apple = new Fruit("apple");
        storageDao.save(apple, 40);

        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.RETURN, apple, 10);

        returnOperation.apply(transaction);

        assertEquals(50, storageDao.get(apple));
    }

    @Test
    void apply_newFruit_Ok() {
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.RETURN,
                new Fruit("banana"),
                15);

        returnOperation.apply(transaction);

        assertEquals(15, storageDao.get(new Fruit("banana")));
    }

    @Test
    void apply_negativeQuantity_NotOk() {
        FruitTransaction invalid = new FruitTransaction(
                FruitTransaction.Operation.RETURN,
                new Fruit("pear"),
                -1);

        assertThrows(IllegalArgumentException.class,
                () -> returnOperation.apply(invalid));
    }
}
