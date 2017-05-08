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

    public final static String NLTK_BASE_URL = "http://text-processing.com/api/";
    public final static HashMap<Integer, String> genreIdtoName = new HashMap<Integer, String>()
    {{
                put( 28, "Action");
            
                put( 12, "Adventure");
            
                put( 16, "Animation");
            
                put( 35, "Comedy");
            
                put( 80, "Crime");
            
                put( 99, "Documentary");
            
                put( 18, "Drama");
            
                put( 10751, "Family");
            
                put( 14,"Fantasy");
            
                put( 36,"History");
            
                put( 27, "Horror");
            
                put( 10402,"Music");
            
                put( 9648, "Mystery");
            
                put( 10749, "Romance");
            
                put( 878, "Science Fiction");
            
                put( 10770, "TV Movie");
            
                put( 53, "Thriller");
            
                put( 10752, "War");
            
                put( 37, "Western");

    }};

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
    //Utilised in Hashing the String to a numeric value
    public static long hashString(String str ){
        long hash = 7;
        int strlen = str.length();
        for (int i = 0; i < strlen; i++) {
            if(hash*31 + str.charAt(i) > 0)
                hash = hash*31 + str.charAt(i);
            else
                break;
        }
        return hash/100000;
    }

    public static String getGenrebyId(int genreId){
        return genreIdtoName.get(genreId);
    }

}
