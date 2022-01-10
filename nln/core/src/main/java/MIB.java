import java.io.IOException;
import java.time.chrono.ThaiBuddhistChronology;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

import net.named_data.jndn.*;
import net.named_data.jndn.transport.Transport;

public class MIB {
    static class ModelInfo {
        enum Dataset  {
            SELF(), PREFIX();
            Name name;
            void set(Name name) {
                this.name = name;
            }
            Optional<Name> get() {
                switch (this) {
                    case SELF -> {
                        return null;
                    }
                    case PREFIX -> {
                        return Optional.of(this.name);
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + this);
                }
            }
        }

        Dataset dataset;
        Face[] faces;
    }

    public static MIB shard = new MIB();
    LinkedHashMap<Name, ModelInfo> lhm = new LinkedHashMap<>();

    private MIB() {
        Face testFace = new Face("172.20.0.3");
        try {
            testFace.expressInterest(new Name("/nln"), new OnData() {
                @Override
                public void onData(Interest interest, Data data) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
