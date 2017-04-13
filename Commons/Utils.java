package Commons;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by parmj on 2/21/17.
 */
public class Utils {
    public final static String MOVIEDB_API_KEY = "d07387463c2c0d2998d6bd09ec481c17";
    public static String httpRequest(Request request){
        String json= "";
        OkHttpClient client = new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
            json = response.body().string();
            //System.out.print("Stats *********"+json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
