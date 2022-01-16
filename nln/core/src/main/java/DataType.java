public enum DataType {
    DATASET, MODEL, NONE;

    static DataType getTypeFrom(String name) {
        if (name.startsWith("/dataset")) {
            return DATASET;
        } else if (name.startsWith("/model")){
            return  MODEL;
        } else {
            return NONE;
        }
    }
}
