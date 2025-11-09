package core.basesyntax.service.strategy;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.model.FruitTransaction;
import core.basesyntax.service.strategy.operations.BalanceOperation;
import core.basesyntax.service.strategy.operations.OperationHandler;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OperationStrategyImplTest {
    private OperationStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new OperationStrategyImpl(Map.of(
                FruitTransaction.Operation.BALANCE, new BalanceOperation(new StorageDaoImpl())
        ));
    }

    @Test
    void get_nullValue_NotOk() {
        assertThrows(IllegalArgumentException.class,
                () -> strategy.get(null));
    }

    @Test
    void get_existingOperation_Ok() {
        OperationHandler handler = strategy.get(FruitTransaction.Operation.BALANCE);
        assertNotNull(handler);
    }

    @Test
    void get_unknownOperation_NotOk() {
        assertThrows(IllegalArgumentException.class,
                () -> strategy.get(FruitTransaction.Operation.RETURN));
    }
}
