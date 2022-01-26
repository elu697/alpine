package common;

import java.util.*;

import net.named_data.jndn.*;

public class MIB {
    public static final class ModelInfo extends Hashtable {
        private ArrayList<Name> datasetName;
        private ArrayList<Face> forwardFace;

        public ModelInfo() {
            datasetName = new ArrayList<>();
            forwardFace = new ArrayList<>();
        }

        public void add(Name name) {
            datasetName.add(name);
        }

        public void add(Face forward) {
            forwardFace.add(forward);
        }

        public void remove(Name name) {
            datasetName.remove(name);
        }

        public void remove(Face face) {
            forwardFace.remove(face);
        }

        public ArrayList<Name> getDatasetNames() {
            return datasetName;
        }

        public ArrayList<Face> getFaces() {
            return forwardFace;
        }
    }

    volatile public static MIB shard = new MIB();
    private LinkedHashMap<Name, ModelInfo> lhm = new LinkedHashMap<>();

    private MIB() {
    }

    public void set(Name prefix, Name datasetName) {
        ModelInfo modelInfo = lhm.getOrDefault(prefix, new ModelInfo());
        modelInfo.add(datasetName);
        lhm.put(prefix, modelInfo);
    }

    public void set(Name prefix, Face forwardFace) {
        ModelInfo modelInfo = lhm.getOrDefault(prefix, new ModelInfo());
        modelInfo.add(forwardFace);
        lhm.put(prefix, modelInfo);
    }

    public void unset(Name prefix, Name datasetName) {
        ModelInfo modelInfo = lhm.getOrDefault(prefix, new ModelInfo());
        modelInfo.remove(datasetName);
        lhm.put(prefix, modelInfo);
    }

    public void unset(Name prefix, Face forwardFace) {
        ModelInfo modelInfo = lhm.getOrDefault(prefix, new ModelInfo());
        modelInfo.remove(forwardFace);
        lhm.put(prefix, modelInfo);
    }

    public ModelInfo get(Name prefix) {
        return lhm.getOrDefault(prefix, new ModelInfo());
    }

    public static void main(String[] args) {
        MIB.shard.set(new Name("/model/A"), new Name("/dataset/ABC"));
        MIB.shard.set(new Name("/model/A"), new Name("/dataset/AB"));
        MIB.shard.set(new Name("/model/A"), new Face());
        MIB.shard.get(new Name("/model/A")).datasetName.forEach(i-> {
            System.out.println(i);
        });
        MIB.shard.get(new Name("/model/A")).forwardFace.forEach(i-> {
            System.out.println(i);
        });
    }
}
