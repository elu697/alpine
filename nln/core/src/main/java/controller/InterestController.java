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
                responseDataConsumer.accept(responseData);
                ndnController.runLoop();
            }
        }, new OnTimeout() {
            @Override
            public void onTimeout(Interest interest) {
                responseDataConsumer.accept(ResponseData.returnEmpty());
                ndnController.runLoop();
            }
        }, new OnNetworkNack() {
            @Override
            public void onNetworkNack(Interest interest, NetworkNack networkNack) {
                responseDataConsumer.accept(ResponseData.returnEmpty());
                ndnController.runLoop();
            }
        });
        Controller.setDaemonThread(ndnController::runLoop);
    }

}