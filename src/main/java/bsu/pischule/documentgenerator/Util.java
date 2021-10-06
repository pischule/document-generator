package bsu.pischule.documentgenerator;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class Util {
    public static ResponseEntity<Resource> getResourceResponseEntity(byte[] document) {
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
