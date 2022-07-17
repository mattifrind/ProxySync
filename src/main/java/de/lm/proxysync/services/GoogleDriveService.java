package de.lm.proxysync.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* class to demonstarte use of Drive files list API */
@ApplicationScoped
@Startup
public class GoogleDriveService {

    @Inject StatusService statusService;

    /** Application name. */
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /** Directory to store authorization tokens for this application. */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static final String ROOT_FOLDER_ID = "1K2wBvCt5XooMV63ER9hTbqLpd1238vW7";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleDriveService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            statusService.addError("Get Drive Credentials failed");
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        //returns an authorized Credential object.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void initGoogleDrive() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        statusService.setDriveWorking(true);
        /*
        String projectFolderId = createDir(service, "Testprojekt", ROOT_FOLDER_ID);
        String proxiesFolderId = createDir(service, "Proxies", projectFolderId);
        java.io.File movFile = new java.io.File("D:\\Videos\\ProxySyncTestOrdner\\Projekt Full\\Proxy\\Untitled00007893.mov");
        uploadVideoFile(service, proxiesFolderId, movFile);
        uploadVideoFile(service, projectFolderId, movFile);*/

        System.out.println(folderExists(ROOT_FOLDER_ID, "Laufende Projekte", service));

        System.out.println("Test Finished");
    }

    private void listFiles(Drive service) throws IOException {
        FileList result = service.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("\n\n--------------------------------------------\nFiles:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
            System.out.println("-------------------FINISHED----------------------------------------");
        }
    }

    private void uploadVideoFile(Drive service, String folderId, java.io.File file) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(file.getName());
        fileMetadata.setParents(Collections.singletonList(folderId));
        // Specify media type and file-path for file.
        FileContent mediaContent = new FileContent("video/mov", file);
        try {
            File uploadFile = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            System.out.println("File ID: " + uploadFile.getId());
        }catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }

    private String createDir(Drive service, String folderName, String parentFolderId) throws IOException{
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setParents(Collections.singletonList(parentFolderId));
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        try {
            File file = service.files().create(fileMetadata)
                    .setFields("id")
                    .execute();
            System.out.println("Folder ID: " + file.getId());
            return file.getId();
        }catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to create folder: " + e.getDetails());
            throw e;
        }
    }

    private boolean folderExists(String parentFolderId, String foldername, Drive service) throws IOException {

        List<File> files = new ArrayList<>();

        String pageToken = null;
        do {
            FileList result = service.files().list().setQ("mimeType = 'application/vnd.google-apps.folder'")
                    .execute();
            for (File file : result.getFiles()) {
                System.out.printf("Found file: %s (%s)\n",
                        file.getName(), file.getId());
            }

            files.addAll(result.getFiles());

            pageToken = result.getNextPageToken();
        } while (pageToken != null);


        for(File f : files){
            if(f.getName().equals(foldername))
                return true;
        }
        return false;
    }



    void onStart(@Observes StartupEvent ev) throws IOException, GeneralSecurityException {
        //initGoogleDrive();
        //uploadBasic();
    }
}

