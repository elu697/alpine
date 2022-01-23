import jdk.nashorn.internal.parser.JSONParser;
import ndn.Controller;
import net.named_data.jndn.*;
import net.named_data.jndn.util.Blob;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;

public class ForwardController {
    Controller ndnController;
    LearningController learningController;

    public ForwardController() {
        ndnController = new Controller();
    }

    private void listener(Name prefix, Interest interest, Face face, long interestFilterId, InterestFilter filter) {
        MIB.ModelInfo modelInfo = MIB.shard.get(prefix);
        final Integer[] shouldResponseCount = {modelInfo.getDatasetNames().size() + modelInfo.getFaces().size()};
        final ArrayList<Data> responseData = new ArrayList<>();
        final Boolean[] isResponse = {false};

        // Response
        double delay = interest.getInterestLifetimeMilliseconds()*0.75;
        TimerTask task = new TimerTask() {
            public void run() {
                if (!isResponse[0]) {
                    isResponse[0] = true;
                    response(prefix, interest, face, responseData);
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, (long)delay);

        AsyncBlock asyncBlock = new AsyncBlock();
        asyncBlock.setDaemonThread(() -> {
            while (shouldResponseCount[0] > 0) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!isResponse[0]) {
                isResponse[0] = true;
                response(prefix, interest, face, responseData);
            }
            asyncBlock.killThread();
        });

        // Learning
        for (Name datasetName : modelInfo.getDatasetNames()) {
            Controller datasetController = new Controller();
            datasetController.interest(datasetName.toString(), false, false, (datasetInterest, data) -> {
                shouldResponseCount[0]--;
                responseData.add(data);
            }, datasetInterest -> {
                shouldResponseCount[0]--;
            }, (datasetInterest, networkNack) -> {
                shouldResponseCount[0]--;
            });
        }

        // Forward
        for (Face forwardFace : modelInfo.getFaces()) {
            Controller forwardController = new Controller(forwardFace);
            Interest forwardInterest = new Interest(interest);
            Name forwardName = prefix.append(Controller.getTsf(interest.getName()));
            forwardInterest.setName(forwardName);
            forwardController.interest(forwardInterest, (datasetInterest, data) -> {
                shouldResponseCount[0]--;
                responseData.add(data);
            }, datasetInterest -> {
                shouldResponseCount[0]--;
            }, (datasetInterest, networkNack) -> {
                shouldResponseCount[0]--;
            });
        }
    }

    private void response(Name prefix, Interest originInterest, Face face, ArrayList<Data> data) {
        System.out.println(data.toString());
        ResponseData responseData = new ResponseData();
        responseData.set("Data", "AAA");
        System.out.println(responseData.toJsonObj());
//        new JSONParser()
        Controller.responseParam(originInterest, face, new Blob(responseData.toJsonObj().toString()));
    }

    private void datasetInterest() {

    }

    private void forwardInterest() {

    }

    public void listen(String name) {
        ndnController.register(name, this::listener, prefix -> {
            // Fail
        }, (prefix, registeredPrefixId) -> {
            // Success
        });
    }

    public static void main(String[] args) {
        MIB.shard.set(new Name("/model/A"), new Name("/mnist"));
        MIB.shard.set(new Name("/model/A"), new Name("/mnist2"));
        MIB.shard.set(new Name("/model/A"), new Name("/mnist3"));
        MIB.shard.set(new Name("/model/B"), new Name("/mnist"));
        MIB.shard.set(new Name("/model/C"), new Name("/mnist"));

        ForwardController fController = new ForwardController();
        fController.listen("/model");
        new AsyncBlock().setDaemonThread(() -> fController.ndnController.runLoop());

        Controller controller = new Controller();
        controller.interest("/model/A", true, true, new OnData() {
            @Override
            public void onData(Interest interest, Data data) {
                String dataStr = data.getContent().toString();
                String jsonStr = new JSONObject(dataStr).get("Data").toString();
                System.out.println(jsonStr);

            }
        }, new OnTimeout() {
            @Override
            public void onTimeout(Interest interest) {

            }
        }, new OnNetworkNack() {
            @Override
            public void onNetworkNack(Interest interest, NetworkNack networkNack) {

            }
        });
        controller.runLoop();
    }
}


