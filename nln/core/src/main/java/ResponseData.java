import net.named_data.jndn.Data;
import net.named_data.jndn.util.Blob;

import java.util.ArrayList;

public class ResponseData {
    String dummyString;

    ResponseData() {
    }

    ResponseData(Data data) {
        dummyString = data.getContent().toString();
    }

    ResponseData(ArrayList<Data> data) {
        data.forEach(i-> {
            dummyString += i.getContent().toString();
        });
    }

    Blob makeBlob() {
        return new Blob(dummyString);
    }
}
