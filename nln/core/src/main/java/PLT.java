import net.named_data.jndn.Face;
import net.named_data.jndn.Name;

import java.lang.reflect.Array;
import java.util.*;

public class PLT {
    static class PendingInfo extends Hashtable {
        Name prefix;
        Face request;

        public PendingInfo(Name prefix, Face request) {
            this.prefix = prefix;
            this.request = request;
        }
    }

    static class LearningInfo extends Hashtable {
        ArrayList<Face> aliveFaces = new ArrayList<>();

        public LearningInfo(){
        }

        public void pushFace(Face face) {
            aliveFaces.add(face);
        }

        public boolean popFace(Face face) {
            return aliveFaces.remove(face);
        }
    }

    public static PLT shard = new PLT();
    LinkedHashMap<PendingInfo, PLT.LearningInfo> lhm = new LinkedHashMap<>();

    private PLT() {
    }

    public void push(Name prefix, Face requestFrom, Face alive) {
        PendingInfo pi = new PendingInfo(prefix, requestFrom);
        LearningInfo learningInfo = lhm.getOrDefault(pi, new LearningInfo());
        learningInfo.pushFace(alive);
        lhm.put(pi, learningInfo);
    }

    public void pop(Name prefix, Face requestFrom, Face alive) {
        PendingInfo pi = new PendingInfo(prefix, requestFrom);
        LearningInfo learningInfo = lhm.getOrDefault(pi, new LearningInfo());
        learningInfo.popFace(alive);
        if (learningInfo.aliveFaces.isEmpty()) {
            lhm.remove(pi);
        } else {
            lhm.put(pi, learningInfo);
        }
    }

    public ArrayList<PendingInfo> getPending() {
        return new ArrayList<PendingInfo>(lhm.keySet());
    }

    public LearningInfo getLearning(PendingInfo pendingInfo) {
        return lhm.getOrDefault(pendingInfo, new LearningInfo());
    }

    public static void main(String[] args) {
    }
}
