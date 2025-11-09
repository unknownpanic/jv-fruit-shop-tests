package core.basesyntax.service.report;

import core.basesyntax.dao.StorageDao;

public class ReportGeneratorImpl implements ReportGenerator {
    public static final String REPORT_HEADER = "fruit,quantity";
    public static final String COMMA_SEPARATOR = ",";
    private final StorageDao storageDao;

    public ReportGeneratorImpl(StorageDao storageDao) {
        this.storageDao = storageDao;
    }

    @Override
    public String getReport() {
        StringBuilder report = new StringBuilder(REPORT_HEADER);
        storageDao.getAll().forEach(
                (fruit, quantity) ->
                report.append(System.lineSeparator())
                        .append(fruit.getFruitName())
                        .append(COMMA_SEPARATOR)
                        .append(quantity)
        );
        return report.toString();
    }
}
