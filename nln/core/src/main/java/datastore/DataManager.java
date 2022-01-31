package datastore;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.AbstractFileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;

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
        String zipPath = zipFiles(targetDir, tempZip);
        return fileToBase64(new File(zipPath));
    }

    public static String getFolderFromBase64String(String base64, String targetDir) {
        String tempZip = targetDir + "/temp_" + LocalDateTime.now() + ".zip";
        File zip = base64ToFile(new File(tempZip), base64);
        return unzipFile(zip.getPath(), targetDir);
    }

    private static String zipFiles(String srcDir, String targetZip) {
        try (ZipFile zipFile = new ZipFile(targetZip)) {
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionLevel(CompressionLevel.ULTRA);
            zipFile.addFolder(new File(srcDir), zipParameters);
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
        if (!source.exists()) {
            source.deleteOnExit();
            return "";
        }
        try (ZipFile zipFile = new ZipFile(source)) {
            zipFile.extractAll(targetDir);
            zipFile.close();
            String folderName = zipFile.getFileHeaders().stream().filter(AbstractFileHeader::isDirectory).collect(Collectors.toList()).get(0).getFileName();
            folderName = folderName.replace("/", "");
//            source.delete();
            return folderName;
        } catch (IOException e) {
            System.err.println(srcZip);
            System.err.println(targetDir);
            e.printStackTrace();
//            source.delete();
            return "";
        } finally {
            source.delete();
        }
    }

    private static String fileToBase64(File file) {
        byte[] dataB = new byte[0];
        try {
            dataB = Files.readAllBytes(file.toPath());
//            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
//            file.delete();
            return "";
        } finally {
            file.delete();
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
