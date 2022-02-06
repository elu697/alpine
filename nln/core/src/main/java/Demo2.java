import common.MIB;
import controller.ForwardController;
import net.named_data.jndn.Name;

public class Demo2 {
    public static void main(String[] args) {
        System.out.println("Execute Demo2");
        MIB.shard.set(new Name("/model/A"), new Name("/fashionmnist"));
        ForwardController forwardController = new ForwardController();
        forwardController.listen("/model");
        forwardController.loop();
    }
}
