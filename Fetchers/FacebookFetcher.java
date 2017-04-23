package Fetchers;


import Commons.Utils;
import facebook4j.*;
import facebook4j.auth.AccessToken;
import javafx.geometry.Pos;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by parmj on 2/15/17.
 */
public class FacebookFetcher {
<<<<<<< HEAD

    //Input : Facebook Post Id
    public void getReactions(String id) {

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = null;
        Map<String, Integer> reactionsMap= new HashMap<String,Integer>();
        String data = null;

        String url = "https://graph.facebook.com/v2.9/"+id+"/reactions?access_token="+ Utils.FB_ACCESS_TOKEN+"&fields=type";
        do{
             request = new Request.Builder()
                .url(url)
                .get()
                .build();
            try{
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                String json = response.body().string();
                System.out.println(json);
                JSONObject jsonObject = new JSONObject(json);
                JSONArray resultsData = (JSONArray)jsonObject.get("data");
                JSONObject resultsPaging = jsonObject.getJSONObject("paging");
                 for(int i = 0; i<resultsData.length();i++){
                    String type = (String)resultsData.getJSONObject(i).get("type");
                    if(reactionsMap.containsKey(type)){
                        int tmp = reactionsMap.get(type);
                        tmp++;
                        reactionsMap.put(type,tmp);
                    }
                    else{
                        reactionsMap.put(type,1);
                    }

=======

>>>>>>> 84f896d7d28cfe663b9992884e01e1b732734136
                }
                if(resultsPaging.has("next"))
                    url = (String)resultsPaging.get("next");
                else
                    url = null;
                            }catch(Exception e){
                e.printStackTrace();
            }

        }while(url!=null);

//            MongoCRUD mongoCRUD = new MongoCRUD();
//            mongoCRUD.insert(json, "boxoffice", "upcomingMovies");

    }

}


