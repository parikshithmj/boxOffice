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
    public final static String FB_ACCESS_TOKEN = "EAACEdEose0cBAKZCKElT1C34t9n4Hw3hdNGdZAZCzkZCfA3BkC2YJL0Fy4VsqbGDkd9DVhSQ5ST7KrDna5SCLEjZBLcbeZByhZAUUq5jLqW7MzLcROWjUZAtoD8bEuaBC06YnJ1mIZAiwTB0R1mL7ILrirulY2gXLRJmrliS6aZA7gV2FomhJogS09OOiosg0NiHYZD";

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
