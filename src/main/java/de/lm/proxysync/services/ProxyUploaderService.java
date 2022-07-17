package de.lm.proxysync.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.security.GeneralSecurityException;

@ApplicationScoped
public class ProxyUploaderService {

    @Inject GoogleDriveService googleDriveService;

    public void uploadProxies() throws GeneralSecurityException, IOException {
        googleDriveService.initGoogleDrive();
        //TODO: Scanning
        //TODO: Uploading
    }
}
