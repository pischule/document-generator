package bsu.pischule.documentgenerator.templatedocument;

import bsu.pischule.documentgenerator.Util;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class TemplateDocumentController {
    final TemplateDocumentService documentService;

    @PostMapping("/api/template-document")
    @ResponseBody
    public ResponseEntity<Resource> getDocument(@RequestBody TemplateDocumentRequest request) {
        try {
            byte[] document = documentService.fillDocument(request);
            return Util.getResourceResponseEntity(document);
        } catch (Exception e) {
            log.error("template document error", e);
            return ResponseEntity.notFound().build();
        }
    }
}
