package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.model.Fruit;
import core.basesyntax.model.FruitTransaction;
import core.basesyntax.service.strategy.OperationStrategy;
import core.basesyntax.service.strategy.OperationStrategyImpl;
import core.basesyntax.service.strategy.operations.BalanceOperation;
import core.basesyntax.service.strategy.operations.OperationHandler;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShopServiceImplTest {
    private ShopService shopService;
    private StorageDao storageDao;
    private OperationStrategy operationStrategy;
    private Map<FruitTransaction.Operation, OperationHandler> operationHandlers;

    @BeforeEach
    void setUp() {
        storageDao = new StorageDaoImpl();
        operationHandlers = Map.of(FruitTransaction.Operation.BALANCE,
                new BalanceOperation(storageDao));
        operationStrategy = new OperationStrategyImpl(operationHandlers);
        shopService = new ShopServiceImpl(operationStrategy);
    }

    @AfterEach
    void tearDown() {
        Storage.getModifiableLeftovers().clear();
    }

    @Test
    void process_validBalanceTransaction_Ok() {
        FruitTransaction validTransaction = new FruitTransaction(
                FruitTransaction.Operation.BALANCE,
                new Fruit("banana"),
                100
        );
        List<FruitTransaction> transactions = List.of(validTransaction);

        shopService.process(transactions);

        assertEquals(100, storageDao.get(new Fruit("banana")));
    }

    @Test
    void process_emptyList_Ok() {
        List<FruitTransaction> emptyList = List.of();

        assertDoesNotThrow(() -> shopService.process(emptyList));
        assertTrue(storageDao.getAll().isEmpty());
    }

    @Test
    void process_nullValue_NotOk() {
        assertThrows(IllegalArgumentException.class,
                () -> shopService.process(null));
    }

    @Test
    void process_negativeTransactionQuantity_NotOk() {
        FruitTransaction invalidTransaction = new FruitTransaction(
                FruitTransaction.Operation.BALANCE,
                new Fruit("banana"),
                -10
        );
        List<FruitTransaction> transactions = List.of(invalidTransaction);

        assertThrows(IllegalArgumentException.class,
                () -> shopService.process(transactions));
    }
}
