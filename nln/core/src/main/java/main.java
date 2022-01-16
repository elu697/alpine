import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.CoderMalfunctionError;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import ndn.Controller;
import net.named_data.jndn.*;
import net.named_data.jndn.util.Blob;
import org.tensorflow.ConcreteFunction;
import org.tensorflow.Signature;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;
import org.tensorflow.op.Ops;
import org.tensorflow.op.core.Placeholder;
import org.tensorflow.op.math.Add;
import org.tensorflow.types.TInt32;

public class main {
    public static void main(String[] args) {
        System.out.println("Hello TensorFlow " + TensorFlow.version());
        System.out.println("Hello TensorFlow " + TensorFlow.version());

        try (ConcreteFunction dbl = ConcreteFunction.create(main::dbl);
             TInt32 x = TInt32.scalarOf(10);
             Tensor dblX = dbl.call(x)) {
            System.out.println(x.getInt() + " doubled is " + ((TInt32)dblX).getInt());
        }

        String name = "/model/A";
        if (Objects.equals(args[0], "C")) {
//            Face face = new Face("172.20.0.3");
            Face face = new Face();
            Controller controller = new Controller(face);
            controller.interest(name, true,true, (interest, data) -> {
                System.out.println("Date Coming");
                System.out.println(name);
                System.out.println(interest.getName());
                System.out.println(data.getContent());
            }, interest -> {
                System.out.println("Interest Timeout");
            }, (interest, networkNack) -> {
                System.out.println("NetworkError");
            });
        } else {
            Controller controller = new Controller();
            controller.register(name, (prefix, interest, face, interestFilterId, filter) -> {
                System.out.println("Interest Coming");
                System.out.println(interest.toUri());
                Data data = new Data(interest.getName());
                data.setContent(new Blob("Echo" + interest.getName().toString()));
                try {
                    face.putData(data);
                } catch (Exception e) {

                }
            }, prefix -> {
                System.out.println("Register failed");
                System.out.println(prefix);
            }, (prefix, registeredPrefixId) -> {
                System.out.println("Register success");
                System.out.println(prefix);
            });
            controller.runLoop();
        }
    }

    private static Signature dbl(Ops tf) {
        Placeholder<TInt32> x = tf.placeholder(TInt32.class);
        Add<TInt32> dblX = tf.math.add(x, x);
        return Signature.builder().input("x", x).output("dbl", dblX).build();
    }

    public static void setDaemonThread(Runnable block) {
        Thread thread = new Thread(block);
        thread.setDaemon(true);
        thread.start();
    }
}
