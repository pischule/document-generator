package bsu.pischule.documentgenerator.templatedocument;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class TemplateDocumentRequestDto {
    @NotBlank
    private String ownerName = "Иван Пупкин";
    private long accountNumber = 69L;
    @NotNull
    private LocalDate dateFrom = LocalDate.now();
    @NotNull
    private LocalDate dateTo = LocalDate.now();

    private List<Row> rows = List.of();

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
