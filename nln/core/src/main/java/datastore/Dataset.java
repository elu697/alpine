package datastore;

public final class Dataset {
    private static String DATASETS_ROOT = "/home/docker/datasets";

    public final String datasetsPath;
    public final String trainingImagesArchivePath;
    public final String trainingLabelsArchivePath;
    public final String testImagesArchivePath;
    public final String testLabelsArchivePath;

    private Dataset(String datasetsPath, String trainingImagesArchivePath, String trainingLabelsArchivePath, String testImagesArchivePath, String testLabelsArchivePath) {
        this.datasetsPath = datasetsPath;
        this.trainingImagesArchivePath = trainingImagesArchivePath;
        this.trainingLabelsArchivePath = trainingLabelsArchivePath;
        this.testImagesArchivePath = testImagesArchivePath;
        this.testLabelsArchivePath = testLabelsArchivePath;
    }

    public static Dataset initMnist() {
        String name = DATASETS_ROOT + "/mnist";
        String trainImage = name + "/train-images-idx3-ubyte.gz";
        String trainLabel = name + "/train-labels-idx1-ubyte.gz";
        String testImage =  name + "/t10k-images-idx3-ubyte.gz";
        String testLabel = name + "/t10k-labels-idx1-ubyte.gz";

        Dataset dataset = new Dataset(name, trainImage, trainLabel, testImage, testLabel);
        return dataset;
    }

    public static Dataset initFrom(String datasetName) {
        if (datasetName.startsWith("/")) {
            datasetName = datasetName.replaceFirst("/", "");
        }
        datasetName = datasetName.replaceAll("/", "_");

        String name = DATASETS_ROOT + "/" + datasetName;
        String trainImage = name + "/train-images-idx3-ubyte.gz";
        String trainLabel = name + "/train-labels-idx1-ubyte.gz";
        String testImage =  name + "/t10k-images-idx3-ubyte.gz";
        String testLabel = name + "/t10k-labels-idx1-ubyte.gz";
        Dataset dataset = new Dataset(name, trainImage, trainLabel, testImage, testLabel);
        return dataset;
    }

    public static Dataset initFromZipBase64String(String base64) {
        String folderName = DataManager.getFolderFromBase64String(base64, DATASETS_ROOT);
        return initFrom(folderName);
    }

    public String getZipDatasetsBase64String() {
        return DataManager.getBase64String(datasetsPath);
    }

    public static void main(String[] args) {
        Dataset dataset = initMnist();
        String base64 = dataset.getZipDatasetsBase64String();
//        System.out.println(base64);
        System.out.println(dataset.datasetsPath);
        System.out.println(dataset.trainingImagesArchivePath);
        System.out.println(dataset.trainingLabelsArchivePath);
        System.out.println(dataset.testImagesArchivePath);
        System.out.println(dataset.testLabelsArchivePath);

        Dataset dataset2 = initFromZipBase64String(base64);
        String base642 = dataset2.getZipDatasetsBase64String();
//        System.out.println(base642);
        System.out.println(dataset2.datasetsPath);
        System.out.println(dataset2.trainingImagesArchivePath);
        System.out.println(dataset2.trainingLabelsArchivePath);
        System.out.println(dataset2.testImagesArchivePath);
        System.out.println(dataset2.testLabelsArchivePath);

        System.out.println("Copy test: " + base64.equals(base642));
    }
}
