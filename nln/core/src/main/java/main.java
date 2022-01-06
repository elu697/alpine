import net.named_data.jndn.*;
import net.named_data.jndn.impl.*;
import net.named_data.jndn.lp.*;
import net.named_data.jndn.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.named_data.jndn.Data;
import net.named_data.jndn.Face;
import net.named_data.jndn.Interest;
import net.named_data.jndn.InterestFilter;
import net.named_data.jndn.Name;
import net.named_data.jndn.OnData;
import net.named_data.jndn.OnInterestCallback;
import net.named_data.jndn.OnRegisterFailed;
import net.named_data.jndn.OnTimeout;
import net.named_data.jndn.security.KeyChain;
import net.named_data.jndn.security.SafeBag;
import net.named_data.jndn.security.SecurityException;
import net.named_data.jndn.util.Blob;
import net.named_data.jndn.util.Common;
import ndn.Controller;


public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();

        Controller.interest("/localhost/nfd/rib/register", new OnData() {
            public void onData(Interest interest, Data data) {
                System.out.println("AA");
            }
        });
    }
}
