package core.basesyntax.service.file.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileReaderImplTest {
    private FileReader fileReader;

    @BeforeEach
    void setUp() {
        fileReader = new FileReaderImpl();
    }

    @Test
    void read_validFile_Ok() {
        String filePath = "src/test/resources/lines.csv";

        List<String> expectedList = List.of("firstLine", "secondLine");
        List<String> actualList = fileReader.read(filePath);

        assertFalse(actualList.isEmpty());
        assertEquals(actualList.size(), expectedList.size(),
                "Size of result List should be:" + expectedList.size()
                        + ", but was: " + actualList.size());

        assertEquals(expectedList, actualList,
                "Result List expected to be: " + expectedList
                        + ", but was: " + actualList);
    }

    @Test
    void read_fileDoesNotExist_NotOk() {
        String invalidFilePath = "src/test/resources/fwasfa.csv";

        assertThrows(IllegalArgumentException.class,
                () -> fileReader.read(invalidFilePath));
    }

    @Test
    void read_nullPath_NotOk() {
        assertThrows(IllegalArgumentException.class,
                () -> fileReader.read(null));
    }

    @Test
    void read_emptyFile_NotOk() {
        String emptyFilePath = "src/test/resources/empty.txt";

        assertThrows(IllegalArgumentException.class,
                () -> fileReader.read(emptyFilePath));
    }
}
