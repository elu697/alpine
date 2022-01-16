import jdk.javadoc.doclet.Doclet;
import ndn.Controller;
import net.named_data.jndn.*;
import net.named_data.jndn.util.Blob;

import java.net.http.HttpResponse;
import java.nio.charset.CoderMalfunctionError;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Forwarder {
    interface ForwarderDelegate {
        default void onData(Interest interest, Data data) {

        }

        default void onTimeout(Interest interest) {

        }

        default void onNetworkNack(Interest interest, NetworkNack networkNack) {

        }
    }

    Controller controller;
    ForwarderDelegate delegate;

    public Forwarder() {
        controller = new Controller();
        delegate = new ForwarderDelegate() {
        };
    }

    private void forward(Name prefix, Interest interest, Face face, long interestFilterId, InterestFilter filter) {
        ArrayList<MIB.ModelInfo> modelInfos = MIB.shard.get(prefix);


        for (MIB.ModelInfo modelInfo : modelInfos) {
            Name dataName;
            // nullをのときにprefixに対してリクエストを送る(FaceがLocalなので関係ない)
            if (modelInfo.dataName.isEmpty()) {
                dataName = new Name(prefix.toString());
            } else {
                dataName = new Name(modelInfo.dataName.toString());
            }

            Controller forwardController = new Controller(modelInfo.forwardFace);
            switch (DataType.getTypeFrom(dataName.toString())) {
                case MODEL, NONE -> {
                    Interest forwardInterest = new Interest(interest);
                    if (dataName != prefix) {
                        String forwardSting = dataName + Controller.getTsf(interest.getName());
                        forwardInterest.setName(new Name(forwardSting));
                    }

                    forwardController.interest(interest, delegate::onData, delegate::onTimeout, delegate::onNetworkNack);
                }
                case DATASET -> {
                    forwardController.interest(dataName.toString(), false, false, delegate::onData, delegate::onTimeout, delegate::onNetworkNack);
                }
//                case NONE -> {
//                    return;
//                }
                default -> throw new IllegalStateException("Unexpected value: " + DataType.getTypeFrom(prefix.toString()));
            }
        }
    }

    public void listenName(String name) {
        controller.register(name, this::forward, prefix -> {
            // Fail
        }, (prefix, registeredPrefixId) -> {
            // Success
        });
    }

    public static void main(String[] args) {
        Forwarder forwarder = new Forwarder();
        MIB.shard.set(new Name("/model/A"), new Name("/nln"), new Face("172.20.0.3"));
        forwarder.listenName("/model/A");
        forwarder.controller.runLoop();
    }
}


