import java.util.*;

import net.named_data.jndn.*;

public class MIB {
    static class ModelInfo extends Hashtable {
        Optional<Name> dataName;
        Face forwardFace;

        public ModelInfo(Name name, Face face) {
            this.dataName = Optional.of(name);
            this.forwardFace = face;
        }

        public ModelInfo(Face face) {
            this.dataName = null;
            this.forwardFace = face;
        }
    }

    volatile public static MIB shard = new MIB();
    private LinkedHashMap<Name, ArrayList<ModelInfo>> lhm = new LinkedHashMap<>();

    private MIB() {
    }

    void set(Name prefix, Name dataName, Face face) {
        ArrayList<ModelInfo> infoList = lhm.getOrDefault(prefix, new ArrayList<>());
        infoList.add(new ModelInfo(dataName, face));
        lhm.put(prefix, infoList);
    }

    void set(Name prefix, Face face) {
        ArrayList<ModelInfo> infoList = lhm.getOrDefault(prefix, new ArrayList<>());
        infoList.add(new ModelInfo(face));
        lhm.put(prefix, infoList);
    }

    ArrayList<ModelInfo> get(Name prefix) {
        return lhm.getOrDefault(prefix, new ArrayList<>());
    }

    public static void main(String[] args) {
        MIB.shard.set(new Name("/model/A"), new Name("/dataset/A"), new Face());
        MIB.shard.set(new Name("/model/A"), new Name("/dataset/B"), new Face());
        MIB.shard.set(new Name("/model/B"), new Name("/dataset/A"), new Face());
        MIB.shard.set(new Name("/model/B"), new Face());
        MIB.shard.get(new Name("/model/A")).forEach( i -> {
            System.out.println(i.dataName);
        });
        MIB.shard.get(new Name("/model/B")).forEach( i -> {
            System.out.println(i.dataName);
        });
    }
}
