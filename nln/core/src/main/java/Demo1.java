import common.MIB;
import controller.ForwardController;
import net.named_data.jndn.Face;
import net.named_data.jndn.Name;

public class Demo1 {
    public static void main(String[] args) {
        MIB.shard.set(new Name("/model/A"), new Name("/mnist"));
        MIB.shard.set(new Name("/model/A"), new Face("172.20.0.2"));

        ForwardController forwardController = new ForwardController();
        forwardController.listen("/model");
        forwardController.loop();
    }
}
