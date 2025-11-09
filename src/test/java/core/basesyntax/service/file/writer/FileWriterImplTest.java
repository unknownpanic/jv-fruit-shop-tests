package core.basesyntax.service.file.writer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileWriterImplTest {
    private FileWriterImpl fileWriter;
    private Path tempFile;

    @BeforeEach
    void setUp() {
        fileWriter = new FileWriterImpl();
        tempFile = Path.of("src/test/resources/temp.csv");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    @DisplayName("Should write valid content to file")
    void write_validInput_Ok() throws IOException {
        String content = "fruit,quantity" + System.lineSeparator() + "banana,100";
        fileWriter.write(content, tempFile.toString());

        String actual = Files.readString(tempFile);
        assertTrue(actual.contains("banana,100"));
    }

    @Test
    void write_nullPath_NotOk() {
        assertThrows(IllegalArgumentException.class,
                () -> fileWriter.write("data", null));
    }

    @Test
    void write_invalidPath_NotOk() {
        String invalidPath = "/this/path/does/not/exist/file.csv";
        assertThrows(RuntimeException.class,
                () -> fileWriter.write("data", invalidPath));
    }

    @Test
    void write_emptyContent_Ok() throws IOException {
        fileWriter.write("", tempFile.toString());

        String actual = Files.readString(tempFile);
        assertTrue(actual.isEmpty());
    }
}
