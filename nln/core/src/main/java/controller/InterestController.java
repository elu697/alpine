package controller;

import model.ResponseData;
import ndn.Controller;
import net.named_data.jndn.*;

import java.util.function.Consumer;

public class InterestController {
    Controller ndnController;

    public InterestController() {
        ndnController = new Controller();
    }

    public void request(String uri, Consumer<ResponseData> responseDataConsumer) {
        ndnController.interest(uri, true, true, new OnData() {
            @Override
            public void onData(Interest interest, Data data) {
                ResponseData responseData= new ResponseData(data.getContent().toString());
                responseData.getPojo().setOptions(String.valueOf(data.getContent().size()));
                responseDataConsumer.accept(responseData);
                ndnController.endLoop();
            }
        }, new OnTimeout() {
            @Override
            public void onTimeout(Interest interest) {
                responseDataConsumer.accept(ResponseData.returnEmpty());
                ndnController.endLoop();
            }
        }, new OnNetworkNack() {
            @Override
            public void onNetworkNack(Interest interest, NetworkNack networkNack) {
                responseDataConsumer.accept(ResponseData.returnEmpty());
                ndnController.endLoop();
            }
        });
        Controller.setDaemonThread(ndnController::runLoop);
    }


}