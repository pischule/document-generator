package bsu.pischule.documentgenerator.templatedocument;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.wp.usermodel.Paragraph;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.springframework.stereotype.Service;

import org.apache.poi.xwpf.usermodel.*;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TemplateDocumentService {
    public InputStream getDocument() {
        return getClass().getClassLoader().getResourceAsStream("template.docx");
    }


    public byte[] fillDocument(TemplateDocumentRequestDto requestDto) throws IOException, XmlException {

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        Map<String, String> words = Map.of(
                "{OWNER_NAME}", requestDto.getOwnerName(),
                "{ACCOUNT_NUMBER}", requestDto.getAccountNumber() + "",
                "{DATE_FROM}", df.format(requestDto.getDateFrom()),
                "{DATE_TO}", df.format(requestDto.getDateTo())
        );

        List<Map<String, String>> data = requestDto.getRows().stream()
                .map(r -> Map.of(
                        "{DATE}", df.format(r.getDate()),
                        "{ID}", r.getId() + "",
                        "{DESCRIPTION}", r.getDescription(),
                        "{CURRENCY}", r.getCurrency(),
                        "{AMOUNT}", r.getAmount().toString()
                )).collect(Collectors.toList());

        XWPFDocument document = new XWPFDocument(getDocument());
        substituteParagraphWords(document, words);
        substituteTable(document, data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        return outputStream.toByteArray();
    }

    public void substituteTable(XWPFDocument document, List<Map<String, String>> replaceRows) throws XmlException, IOException {
        for (Iterator<XWPFTable> it = document.getTablesIterator(); it.hasNext(); ) {
            XWPFTable tbl = it.next();
            List<XWPFTableRow> rows = tbl.getRows();
            if (rows.size() != 2) continue;

            XWPFTableRow rowTemplate = copyRow(tbl.getRow(1), tbl);
            tbl.removeRow(1);

            for (Map<String, String> dataRow : replaceRows) {
                XWPFTableRow row = copyRow(rowTemplate, tbl);
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            String text = r.getText(0);
                            if (text == null) continue;
                            for (String k: dataRow.keySet()) {
                                text = text.replace(k, dataRow.get(k));
                            }
                            r.setText(text, 0);
                        }
                    }
                }
                tbl.addRow(row);
            }
        }
    }

    private XWPFTableRow copyRow(XWPFTableRow row, XWPFTable tbl) throws XmlException, IOException {
        CTRow ctrow = CTRow.Factory.parse(row.getCtRow().newInputStream());
        return new XWPFTableRow(ctrow, tbl);
    }

    public void substituteParagraphWords(XWPFDocument document, Map<String, String> replaceWords) {
        for (Iterator<XWPFParagraph> it = document.getParagraphsIterator(); it.hasNext(); ) {
            XWPFParagraph p = it.next();
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text == null) continue;
                    for (String k : replaceWords.keySet()) {
                        if (!text.contains(k)) continue;
                        text = text.replace(k, replaceWords.get(k));
                    }
                    r.setText(text, 0);
                }
            }
        }
    }
}
