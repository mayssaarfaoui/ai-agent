package com.aimanager.agent.controller;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import com.aimanager.agent.files.FileStorageService;    

@RestController
@RequestMapping("/attachments")
@CrossOrigin("http://localhost:3000")
public class AttachmentController implements IBaseController{

    private static final Logger logger = LoggerFactory.getLogger(AttachmentController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Operation(summary = "Download attachment's file.",
    description = "Download attachment file. This endpoint will be used in order to display the original file uploaded.")
@RequestMapping(value = "/download", params = "uf", method = RequestMethod.GET)
public ResponseEntity<byte[]> getAttachmentFile(
    @RequestParam("uf") @Parameter(description = "Attachment file") String uf){

        logger.info("Downloading attachment file: {}", uf);

if(uf == null || uf.isEmpty())
    return ResponseEntity.badRequest().build();

try {
    File file = fileStorageService.getFile(uf);
    HttpHeaders headers = fileStorageService.getAttachmentFileHeaders(file);
    byte[] fileBytes = Files.readAllBytes(file.toPath());
    return ResponseEntity.ok()
            .headers(headers)
            .body(fileBytes);
} catch (Exception e) {
    logger.error("Error while getting attachment file", e);
    return ResponseEntity.badRequest().build();
}
}
}
