package bsu.pischule.documentgenerator.templatedocument;

import bsu.pischule.documentgenerator.simpledocument.DocumentRequest;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class TemplateDocumentController {
    @Autowired
    TemplateDocumentService documentService;

    @GetMapping("/api/template-document")
    public String greetingForm(Model model) {
        model.addAttribute("documentTemplateRequest", new TemplateDocumentRequestDto());
        return "template-document";
    }

    @PostMapping("/api/template-document")
    @ResponseBody
    public ResponseEntity<Resource> getDocument(@ModelAttribute(name = "documentTemplateRequest") TemplateDocumentRequestDto request) throws IOException, XmlException {
        byte[] document = documentService.fillDocument(request);
        ByteArrayResource resource = new ByteArrayResource(document);
        ContentDisposition contentDisposition = ContentDisposition
                .builder("inline")
                .filename("document.docx")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(document.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
