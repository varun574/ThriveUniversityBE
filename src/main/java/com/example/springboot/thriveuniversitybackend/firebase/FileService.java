package com.example.springboot.thriveuniversitybackend.firebase;

import com.example.springboot.thriveuniversitybackend.enums.Attachments;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class FileService {

    @Autowired
    private Environment environment;
    private Storage storage;
    private FileNameMap fileNameMap;
    private String firebaseSDKResourcePath;

    private String DOWNLOAD_URL;

    @EventListener
    public void init(ApplicationReadyEvent event) {
        firebaseSDKResourcePath = environment.getProperty("firebase.adminSDK.resource.path");
        DOWNLOAD_URL = environment.getProperty("firebase.storage.downloadURL");
        ClassPathResource serviceAccount = new ClassPathResource(firebaseSDKResourcePath);
        fileNameMap = URLConnection.getFileNameMap();
        try {
            storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                    .setProjectId(environment.getProperty("firebase.adminSDK.projectId"))
                    .build().getService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void upload(MultipartFile multipartFile, String uploadedFileName) throws IOException {
        String filename = multipartFile.getOriginalFilename();
        String contentType = fileNameMap.getContentTypeFor(filename);
        if(uploadedFileName.equals(Attachments.ANONYMOUS.name()))
        uploadedFileName = generateFileName(filename);
        File file = convertToFile(multipartFile);
        uploadFile(file, uploadedFileName, contentType);
        file.delete();
    }

    public String download(String objectId) throws IOException {
        ClassPathResource serviceAccount = new ClassPathResource(firebaseSDKResourcePath);
        Blob blob = storage.get(BlobId.of(environment.getProperty("firebase.storage.bucket.name"),objectId));

        URL signedUrl = blob.signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.signWith(ServiceAccountCredentials.fromStream(serviceAccount.getInputStream())));
        return signedUrl.toString();
    }

    public String getDownloadURL(String filename){
        return String.format(DOWNLOAD_URL, filename);
    }

    private void uploadFile(File file, String filename, String contentType) throws IOException {
        Map<String, String> metadata= new HashMap<>();
        metadata.put("firebaseStorageDownloadTokens", "123");
        BlobId blobId = BlobId.of(environment.getProperty("firebase.storage.bucket.name"), filename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).setMetadata(metadata).build();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
    }

    public File convertToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(multipartFile.getBytes());
        outputStream.close();
        return file;
    }

    private String generateFileName(String fileName){
        return UUID.randomUUID().toString().concat(extractExtension(fileName));
    }

    private String extractExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
