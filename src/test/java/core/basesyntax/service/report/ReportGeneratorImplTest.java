package core.basesyntax.service.report;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.model.Fruit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportGeneratorImplTest {
    private ReportGenerator reportGenerator;
    private StorageDao storageDao;

    @BeforeEach
    void setUp() {
        storageDao = new StorageDaoImpl();
        reportGenerator = new ReportGeneratorImpl(storageDao);
    }

    @AfterEach
    void tearDown() {
        Storage.getModifiableLeftovers().clear();
    }

    @Test
    void getReport_validStorage_Ok() {
        storageDao.save(new Fruit("banana"), 100);
        storageDao.save(new Fruit("apple"), 50);

        String report = reportGenerator.getReport();

        assertAll(
                () -> assertTrue(report.contains("fruit,quantity")),
                () -> assertTrue(report.contains("banana,100")),
                () -> assertTrue(report.contains("apple,50"))
        );
    }

    @Test
    void getReport_emptyStorage_Ok() {
        String report = reportGenerator.getReport();
        assertEquals("fruit,quantity", report);
    }
}
