package core.basesyntax.service.file.parser;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.model.FruitTransaction;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DataConverterImplTest {
    private DataConverter dataConverter;

    @BeforeEach
    void setUp() {
        dataConverter = new DataConverterImpl();
    }

    @Test
    void convertToTransaction_validInput_Ok() {
        List<String> lines = List.of("operation,fruit,quantity", "b,banana,100", "s,apple,50");
        List<FruitTransaction> result = dataConverter.convertToTransaction(lines);

        assertAll(
                () -> assertEquals(2,
                        result.size()),
                () -> assertEquals(FruitTransaction.Operation.BALANCE,
                        result.get(0).getOperation()),
                () -> assertEquals("banana",
                        result.get(0).getFruit().getFruitName()),
                () -> assertEquals(100,
                        result.get(0).getQuantity())
        );
    }

    @Test
    @DisplayName("Лише два значення")
    void convertToTransaction_invalidLine_NotOk() {
        List<String> invalid = List.of("operation,fruit,quantity", "b,banana");

        assertThrows(IllegalArgumentException.class,
                () -> dataConverter.convertToTransaction(invalid));
    }

    @Test
    @DisplayName("Неіснуюча операція.")
    void convertToTransaction_invalidOperation_NotOk() {
        List<String> invalid = List.of("operation,fruit,quantity", "x,banana,100");

        assertThrows(IllegalArgumentException.class,
                () -> dataConverter.convertToTransaction(invalid));
    }

    @Test
    void convertToTransaction_nullValue_NotOk() {
        assertThrows(IllegalArgumentException.class,
                () -> dataConverter.convertToTransaction(null));
    }

    @Test
    void convertToTransaction_invalidQuantity_NotOk() {
        List<String> invalid = List.of("operation,fruit,quantity", "b,banana,abc");

        assertThrows(IllegalArgumentException.class,
                () -> dataConverter.convertToTransaction(invalid));
    }

    @Test
    void convertToTransaction_emptyInput_NotOk() {
        List<String> empty = List.of();

        assertThrows(IllegalArgumentException.class,
                () -> dataConverter.convertToTransaction(empty));
    }
}
