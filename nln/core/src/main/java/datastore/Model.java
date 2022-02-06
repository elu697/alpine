package datastore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public final class Model {
    private static String MODEL_ROOT = "/home/docker/models";

    public final String modelDir;
    public final String modelNamePath;
    public final Boolean isModelValid;

    private Model(String modelDir, String modelNamePath, Boolean isModelValid) {
        try {
            Files.createDirectory(Path.of(MODEL_ROOT));
        } catch (IOException e) {
        }
        this.modelDir = modelDir;
        this.modelNamePath = modelNamePath;
        this.isModelValid = isModelValid;
    }
    public static Model initModel() {

        String modelDir = MODEL_ROOT + "/model_" + LocalDateTime.now() + "_dir";
        String modelName = modelDir + "/model";
        return new Model(modelDir, modelName, false);
    }

    public static Model initFrom(String dir) {
        String modelDir = MODEL_ROOT + "/" + dir;
        String modelName = modelDir + "/model";
        return new Model(modelDir, modelName, true);
    }

    public static Model initFromZipBase64String(String base64) {
        String folderName = DataManager.getFolderFromBase64String(base64, MODEL_ROOT);
        return initFrom(folderName);
    }

    public String getZipModelBase64String() {
        return DataManager.getBase64String(modelDir);
    }

    public static void main(String[] args) {
        Model model = Model.initFrom("model_2022-01-26T19:34:25.045_dir");
        String base64 = model.getZipModelBase64String();
        System.out.println(base64);

        Model model2 = Model.initFromZipBase64String(base64);
        String base642 = model2.getZipModelBase64String();

        System.out.println("Copy test: " + base64.equals(base642));
    }
}
