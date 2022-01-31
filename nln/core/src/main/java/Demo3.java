import model.ResponseData;
import ndn.Controller;
import net.named_data.jndn.*;

public class Demo3 {

    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.interest("/model/A", true, true, (interest, data) -> {
            ResponseData responseDataObject = new ResponseData(data.getContent().toString());
            System.out.println("CLIENT DATA");
            System.out.println(responseDataObject.toJsonObj());
        }, interest -> System.out.println("CLIENT TIMEOUT"), (interest, networkNack) -> System.out.println("CLIENT GET NACK"));
        controller.runLoop();
    }
}
