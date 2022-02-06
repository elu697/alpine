import controller.InterestController;
import model.ResponseData;
import ndn.Controller;
import net.named_data.jndn.*;

import java.util.function.Consumer;

public class Demo3 {
    public static void main(String[] args) {
        InterestController interestController = new InterestController();
        interestController.request("/model/A", responseData -> {
            System.out.println(responseData.getPojo().getLearningInfo()
            );
        });
    }
}
