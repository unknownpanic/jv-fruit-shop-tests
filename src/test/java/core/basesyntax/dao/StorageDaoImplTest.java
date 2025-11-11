package core.basesyntax.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.Storage;
import core.basesyntax.model.Fruit;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageDaoImplTest {
    private StorageDao storageDao;

    @BeforeEach
    void setUp() {
        storageDao = new StorageDaoImpl();
    }

    @AfterEach
    void tearDown() {
        Storage.getModifiableLeftovers().clear();
    }

    @Test
    void save_validFruit_Ok() {
        Fruit banana = new Fruit("banana");
        storageDao.save(banana, 100);

        assertEquals(100, storageDao.get(banana));
    }

    @Test
    void get_unknownFruit_Ok() {
        assertNull(storageDao.get(new Fruit("apple")));
    }

    @Test
    void getAll_returnsUnmodifiableMap_Ok() {
        storageDao.save(new Fruit("pear"), 10);
        Map<Fruit, Integer> leftovers = storageDao.getAll();

        assertThrows(UnsupportedOperationException.class,
                () -> leftovers.put(new Fruit("apple"), 5));
    }
}
