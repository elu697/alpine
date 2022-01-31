package datastore;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.AbstractFileHeader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.stream.Collectors;

public final class DataManager {
    public static String getBase64String(String targetDir) {
        String tempZip = new File(targetDir).getParent() + "/temp_" + LocalDateTime.now() + ".zip";
        String zipPath = DataManager.zipFiles(targetDir, tempZip);
        return fileToBase64(new File(zipPath));
    }

    public static String getFolderFromBase64String(String base64, String targetDir) {
        String tempZip = targetDir + "/temp_" + LocalDateTime.now() + ".zip";
        File zip = base64ToFile(new File(tempZip), base64);
        return unzipFile(zip.getPath(), targetDir);
    }

    private static String zipFiles(String srcDir, String targetZip) {
        try (ZipFile zipFile = new ZipFile(targetZip)) {
            zipFile.addFolder(new File(srcDir));
            zipFile.close();
            return zipFile.toString();
        } catch (IOException e) {
            System.err.println(srcDir);
            System.err.println(targetZip);
            e.printStackTrace();
            return "";
        }
    }

    private static String unzipFile(String srcZip, String targetDir) {
        File source = new File(srcZip);
        if (!source.exists()) return "";

        try (ZipFile zipFile = new ZipFile(source)){
            zipFile.extractAll(targetDir);
            zipFile.close();
            String folderName = zipFile.getFileHeaders().stream().filter(AbstractFileHeader::isDirectory).collect(Collectors.toList()).get(0).getFileName();
            folderName = folderName.replace("/", "");
            return folderName;
        } catch (IOException e) {
            System.err.println(srcZip);
            System.err.println(targetDir);
            e.printStackTrace();
            return "";
        } finally {
            source.deleteOnExit();
        }
    }

    private static String fileToBase64(File file) {
        byte[] dataB = new byte[0];
        try {
            dataB = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            file.deleteOnExit();
        }
        // byte[]をbase64文字列に変換する(java8)
        return Base64.getEncoder().encodeToString(dataB);
    }

    private static File base64ToFile(File file, String string) {
        Path path;
        try {
            path = Files.write(file.toPath(), Base64.getDecoder().decode(string));
            return path.toFile();
        } catch (IOException e) {
            e.printStackTrace();
            return file;
        }
    }
}
