package de.lm.proxysync.imported;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main1(String[] args) throws IOException {
        FileCrawler fileCrawler = new FileCrawler(new GoogleDriveUtils());
        fileCrawler.run();
    }

    public static void main(String[] args) {
        Path projectDir = Paths.get("D:\\Videos\\ProxySyncTestOrdner\\Projekt Generating");
        Path fileGenerating = Paths.get("D:\\Videos\\ProxySyncTestOrdner\\Projekt Generating\\Untitled00007893.mov");
        Path fileFinished = Paths.get("D:\\Videos\\ProxySyncTestOrdner\\Projekt Generating\\Untitled00008006.mov");

        String filePath = fileGenerating.toAbsolutePath().toString();
        String proxyFilePath = fileGenerating.getParent().toAbsolutePath() + "\\Proxy\\" + fileGenerating.getFileName().toString();
        String lockFilePath = proxyFilePath.substring(0, proxyFilePath.lastIndexOf(".")) + ".lock";
        System.out.println(filePath);
        System.out.println(proxyFilePath);
        System.out.println(lockFilePath);
        System.out.println(Files.exists(Paths.get(lockFilePath)));

    }

}

