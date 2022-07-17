package de.lm.proxysync.imported;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileCrawler {

    private ArrayList<Path> rootFolders;
    private String drivePath;
    private String altDrivePath;

    private GoogleDriveUtils driveUtils;

    public FileCrawler(GoogleDriveUtils driveUtils) {
        this.driveUtils = driveUtils;
        try {
            this.readConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() throws IOException {
        for (Path root : rootFolders){
            syncRootFolder(root);
        }
    }

    /**
     * Synchronisiert ein Root Directory
     * */
    private void syncRootFolder(Path rootDir) throws IOException {
        for (Path path : Files.newDirectoryStream(rootDir)) {
            if (Files.isDirectory(path)) {
                syncProject(path);
            }
        }
    }

    private void syncProject(Path projectDir) throws IOException {

        for (Path videoFile : Files.newDirectoryStream(projectDir)){
            if(Files.isRegularFile(videoFile) && ! lockFileExists(videoFile)){
                //TODO: Im Drive nachschauen ob das Projekt existiert
                //      wenn nein erstellen in primärpfad,
                // wenn ja:
                //      schauen ob es fertig synchronisiert ist
                //          dazu einzelne Dateien scannen
                //Wenn Projekt in AltPfad existiert, skippen
            }
        }
    }



    private boolean lockFileExists(Path videoFile) throws IOException {
        String proxyFilePath = videoFile.getParent().toAbsolutePath() + "\\Proxy\\" + videoFile.getFileName().toString();
        String lockFilePath = proxyFilePath.substring(0, proxyFilePath.lastIndexOf(".")) + ".lock";
        return Files.exists(Paths.get(lockFilePath));
    }

    private void readConfig() throws IOException {
        Path configFilePath = Paths.get(System.getenv("appdata") + "\\ProxySync\\config.txt");
        if(Files.notExists(configFilePath)){
            Files.createDirectories(configFilePath.getParent());
            Files.createFile(configFilePath);
            Files.writeString(configFilePath, "DriveDirectory: TeamHard - Video/Laufende Projekte/" +
                    "\nAltDriveDirectory: TeamHard - Video/Abgeschlossene Projekte/\nDirectories:\n-"
                    + System.getProperty("user.home") + "\\Videos\\ProxySyncTestOrdner");
            System.out.println("Created Config File with default Data!");
        }

        rootFolders = new ArrayList<>();
        String content = Files.readString(configFilePath);
        content = content.substring(content.indexOf("Directories:\n")).replace("Directories:\n", "");
        for (String s : content.split("\n-")){
            s = s.substring(1);

            Path path = Paths.get(s);
            if(Files.notExists(path)) {
                System.out.println("!!!CONFIG ERROR!!!\nInvalid Path: " + s);
                continue;
            }
            rootFolders.add(path);
            System.out.println("Added " + s + " to config!");
        }
    }
}
