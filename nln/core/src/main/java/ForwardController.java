import ndn.Controller;
import net.named_data.jndn.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ForwardController {
    Controller ndnController;
    LearingController learingController;

    public ForwardController() {
        ndnController = new Controller();
    }

    private void listener(Name prefix, Interest interest, Face face, long interestFilterId, InterestFilter filter) {
        MIB.ModelInfo modelInfo = MIB.shard.get(prefix);
        final Integer[] shouldResponseCount = {modelInfo.getDatasetNames().size() + modelInfo.getFaces().size()};
        final ArrayList<Data> responseData = new ArrayList<>();
        final Boolean[] isResponse = {false};

        // Response
        double delay = interest.getInterestLifetimeMilliseconds()*0.8;
        TimerTask task = new TimerTask() {
            public void run() {
                if (!isResponse[0]) {
                    isResponse[0] = true;
                    response(prefix, interest, responseData);
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
                response(prefix, interest, responseData);
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

    private void response(Name prefix, Interest originInterest, ArrayList<Data> data) {
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
    }
}


