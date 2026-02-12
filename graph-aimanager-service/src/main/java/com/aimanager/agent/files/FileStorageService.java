package com.aimanager.agent.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import org.slf4j.Logger;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Value("${com.aimanager.files.location}")
	private String location;

    @Value("${com.aimanager.server.attachments}")
    private String serverAttachments;

    /**
     * Uploads a file to the target directory with a unique name.
     *
     * @param file the MultipartFile to upload
     * @return the generated filename
     * @throws IOException if the file can't be saved
     */
    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload an empty file.");
        }

        // Get original file extension
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Generate unique name
        String uniqueFileName = UUID.randomUUID().toString() + extension;

      //save the file
      // Setting up the path of the file 
      String filePath = location+File.separator+uniqueFileName;  
        
      // Try block to check exceptions 
      try { 
        logger.info("Uploading file to: {}", filePath);
        Files.createFile(Paths.get(filePath));
        File destinationFile = new File(filePath);
        logger.info("File created at: {}", destinationFile.getAbsolutePath());
            
          // Creating an object of FileOutputStream class   
          FileOutputStream fout = new FileOutputStream(destinationFile); 
          fout.write(file.getBytes()); 
            
          // Closing the connection  
          fout.close(); 
          logger.info("File uploaded successfully");
      }  
      
      // Catch block to handle exceptions 
      catch (Exception e) { 
          e.printStackTrace(); 
          throw new RuntimeException("Failed to upload file: " + e.getMessage());
      } 
      return serverAttachments+File.separator+uniqueFileName;
    }

    /**
     * Deletes a file from the target directory.
     *
     * @param fileName the name of the file to delete
     */
    public void deleteFile(String fileName) {
        File file = new File(location+File.separator+fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Gets the media type for an attachment file.
     *
     * @param file the file to get the media type for
     * @return the media type
     */
    public MediaType getAttachmentFileMediaType(File file) {
        try {
            Path path = file.toPath();
    
            // Detect content type (based on file extension or magic numbers, OS dependent)
            String contentType = Files.probeContentType(path);
    
            if (contentType != null) {
                return MediaType.parseMediaType(contentType);
            } else {
                // Default if unknown
                return MediaType.APPLICATION_OCTET_STREAM;
            }
        } catch (IOException e) {
            // Log the exception and return a generic binary type
            e.printStackTrace();
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    /**
     * Gets the headers for an attachment file.
     *
     * @param file the file to get the headers for
     * @return the headers
     */

    public HttpHeaders getAttachmentFileHeaders(File file) {
        HttpHeaders headers = new HttpHeaders();
        if(file==null)
            return headers;
        headers.setContentType(getAttachmentFileMediaType(file));
        headers.setContentDispositionFormData("attachment", file.getName());
       // headers.set("fileName", attachment.getFileName());
        headers.setContentLength(file.length());
        return headers;
    }

    /**
     * Gets a file from the target directory.
     *
     * @param fileName the name of the file to get
     * @return the file
     * @throws IllegalArgumentException if the file is not found
     */

    public File getFile(String fileName) {
        File file = new File(location+File.separator+fileName);
        if (file.exists()) {
            return file;
        }
        throw new IllegalArgumentException("File not found: " + fileName);
    }
    
}

