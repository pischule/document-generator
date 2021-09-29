package bsu.pischule.documentgenerator.Document;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class DocumentController {
    private DocumentService documentService;

    @GetMapping("/document")
    public String greetingForm(Model model) {
        model.addAttribute("documentRequest", new DocumentRequest());
        return "document";
    }

    @PostMapping("/document")
    @ResponseBody
    public ResponseEntity<Resource> getDocument(@ModelAttribute(name = "documentRequest") DocumentRequest request) {
        byte[] document = documentService.createDocument(request);
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
