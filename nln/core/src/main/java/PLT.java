import net.named_data.jndn.Face;
import net.named_data.jndn.Name;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;

public class PLT {
    static final class PendingInfo {
        Name prefix;
        Face request;

        public PendingInfo(Name prefix, Face request) {
            this.prefix = prefix;
            this.request = request;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PendingInfo that = (PendingInfo) o;
            return Objects.equals(prefix, that.prefix) && Objects.equals(request, that.request);
        }

        @Override
        public int hashCode() {
            return Objects.hash(prefix, request);
        }
    }

    static class LearningInfo {
        LocalDateTime lastMonitoredTime;
        ArrayList<Face> aliveFaces = new ArrayList<>();

        public LearningInfo(){
            monitored();
        }

        public void monitored() {
            lastMonitoredTime = LocalDateTime.now();
        }

        public void pushFace(Face face) {
            aliveFaces.add(face);
        }

        public boolean popFace(Face face) {
            return aliveFaces.remove(face);
        }
    }

    volatile public static PLT shard = new PLT();
    private LinkedHashMap<PendingInfo, PLT.LearningInfo> lhm = new LinkedHashMap<>();

    private PLT() {

    }

    public void push(Name prefix, Face requestFrom, Face alive) {
        PendingInfo pi = new PendingInfo(prefix, requestFrom);
        LearningInfo learningInfo = lhm.getOrDefault(pi, new LearningInfo());
        learningInfo.pushFace(alive);
        lhm.putIfAbsent(pi, learningInfo);
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
        Face face =  new Face("localhost");
        Face face2 =  new Face("localhost");
        PLT.shard.push(new Name("/model/A"), face, face2);
        PLT.shard.push(new Name("/model/A"), new Face("localhost"), new Face("1"));
        PLT.shard.getPending().forEach(i -> System.out.println(i.prefix + "," + PLT.shard.getLearning(i).aliveFaces));


        System.out.println("\n");
        PLT.shard.push(new Name("/model/B"), new Face("localhost"), new Face());
        PLT.shard.pop(new Name("/model/A"), face, face2);
        PLT.shard.getPending().forEach(i -> System.out.println(i.prefix + "," + PLT.shard.getLearning(i).aliveFaces));

        System.out.println("\n");
        PLT.shard.push(new Name("/model/A"), new Face("localhost"), new Face("1"));
        PLT.shard.getPending().forEach(i -> System.out.println(i.prefix + "," + PLT.shard.getLearning(i).aliveFaces));
    }
}
