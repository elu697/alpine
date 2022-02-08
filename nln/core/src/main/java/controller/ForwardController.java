package controller;

import common.MIB;
import datastore.Dataset;
import datastore.Model;
import model.LearningInfo;
import model.ResponseData;
import ndn.Controller;
import net.named_data.jndn.*;
import net.named_data.jndn.util.Blob;
import util.AsyncBlock;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ForwardController {
    Controller ndnController;

    public ForwardController() {
        ndnController = new Controller();
    }

    private void listener(Name prefix, Interest interest, Face face, long interestFilterId, InterestFilter filter) {
        MIB.ModelInfo modelInfo = MIB.shard.get(prefix);
        final Integer[] shouldResponseCount = {modelInfo.getDatasetNames().size() + modelInfo.getFaces().size()};
        final ArrayList<ResponseData> responseData = new ArrayList<>();
        final Boolean[] isResponse = {false};

        if (shouldResponseCount[0] == 0) {
            Controller.responseNack(interest, face, new NetworkNack().setReason(NetworkNack.Reason.NO_ROUTE));
            return;
        }

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
            while (shouldResponseCount[0] > 0 && !isResponse[0]) {
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
            // 本当はPLTかまして非同期でやる．時間ないので単純にタイムアウトを1分ぐらいにする．
            Thread thread = new Thread(() -> {
                try {
                    LearningInfo learningInfo = learning(prefix.toString(), datasetName.toString());
                    ResponseData.POJO pojo = new ResponseData.POJO();
                    pojo.setName(prefix.toString());
                    pojo.addLearningInfo(learningInfo);
                    ResponseData responseDataObject = new ResponseData(pojo);
                    responseData.add(responseDataObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                shouldResponseCount[0]--;
            });
            thread.start();
//            Controller datasetController = new Controller();
//            datasetController.interest(datasetName.toString(), false, false, (datasetInterest, data) -> {
//                // returned datasets
//                // will be learning by datasets
//                shouldResponseCount[0]--;
//                responseData.add(data);
//            }, datasetInterest -> {
//                shouldResponseCount[0]--;
//            }, (datasetInterest, networkNack) -> {
//                shouldResponseCount[0]--;
//            });
        }

        // Forward
        for (Face forwardFace : modelInfo.getFaces()) {
            Thread thread = new Thread(() -> {
                Controller forwardController = new Controller(forwardFace);
                Interest forwardInterest = new Interest(interest);
                forwardController.interest(forwardInterest, (datasetInterest, data) -> {
                    // returned model
                    ResponseData responseDataObject = new ResponseData(data.getContent().toString());
                    System.out.println(data.getContent().size());
                    int totalPacket = Integer.parseInt(responseDataObject.getPojo().getOptions(), 0);
                    responseDataObject.getPojo().setOptions(String.valueOf(data.getContent().size()+totalPacket));
                    responseData.add(responseDataObject);
                    shouldResponseCount[0]--;
                }, datasetInterest -> {
                    shouldResponseCount[0]--;
                }, (datasetInterest, networkNack) -> {
                    shouldResponseCount[0]--;
                });
            });
            thread.start();
        }
    }

    private void response(Name prefix, Interest originInterest, Face face, ArrayList<ResponseData> data) {
//        Controller.responseParam(originInterest, face, new Blob("DATA"));
        ResponseData responseData = new ResponseData(prefix.toString(), data);
        Controller.responseParam(originInterest, face, new Blob(responseData.toJsonObj().toString()));
//        ndnController.responseSegment(originInterest, face, new Blob(responseData.toJsonObj().toString()));
    }

    public void listen(String name) {
        ndnController.register(name, this::listener, prefix -> {
            // Fail
        }, (prefix, registeredPrefixId) -> {
            // Success
        });
    }

    public void loop() {
        ndnController.runLoop();
    }

    private static LearningInfo learning(String modelName, String datasetName) {
        Model model = Model.initModel();
        Dataset dataset = Dataset.initFrom(datasetName);
        LearningInfo learningInfo = LearningController.shard.simpleLearning(modelName+datasetName, model, dataset);
        return learningInfo;
    }

    public static void main(String[] args) {
        MIB.shard.set(new Name("/model/MAIN"), new Name("/mnist"));
//        MIB.shard.set(new Name("/model/A"), new Name("/mnist2"));
//        MIB.shard.set(new Name("/model/A"), new Name("/mnist3"));
//        MIB.shard.set(new Name("/model/A"), new Face("192.168.1.2"));
//        MIB.shard.set(new Name("/model/B"), new Name("/mnist"));
//        MIB.shard.set(new Name("/model/C"), new Name("/mnist"));

        ForwardController fController = new ForwardController();
        fController.listen("/model");
        new AsyncBlock().setDaemonThread(() -> fController.ndnController.runLoop());

        InterestController interestController = new InterestController();
        interestController.request("/model/MAIN", responseData -> {
            System.out.println(responseData.toJsonObj());
        });
    }
}


