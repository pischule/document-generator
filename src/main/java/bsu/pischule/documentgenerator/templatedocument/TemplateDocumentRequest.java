package bsu.pischule.documentgenerator.templatedocument;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class TemplateDocumentRequest {
    @NotBlank
    private String ownerName = "";
    private long accountNumber = 1L;
    @NotNull
    private LocalDate dateFrom = LocalDate.now();
    @NotNull
    private LocalDate dateTo = LocalDate.now();

    private List<Row> rows = List.of();

    private boolean allCapital = false;

    private String templateFile = null;

    @Data
    public static class Row {
        private LocalDate date;
        private long id;
        private String description;
        @NotBlank
        private String currency;
        @NotNull
        private BigDecimal amount;
    }
}
