package Fetchers;

import Commons.Utils;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by parmj on 2/20/17.
 */
public class YouTubeFetcher {
    public String getStats(String videoId){
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = new Request.Builder()
                .url("https://www.googleapis.com/youtube/v3/videos?part=statistics&id="+videoId+"&key=xxxx")
                .get()
                .build();

        return Utils.httpRequest(request);
    }

    public String getVideoId(String searchQuery){

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = new Request.Builder()
                .url("https://www.googleapis.com/youtube/v3/search?part=id&q="+searchQuery+"%20trailer&key=xxxx")
                .get()
                .build();

        String response = Utils.httpRequest(request);
        String videoId = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray results = (JSONArray)jsonObject.get("items");

            JSONObject idObj = new JSONObject(results.get(0).toString());
            System.out.println(results.get(0));
            JSONObject videoIdObj = new JSONObject(idObj.get("id").toString());
            videoId = videoIdObj.get("videoId").toString();
            System.out.println("Video ..."+videoId);

//            for(int i=0; i<results.length(); i++){
//                list.add(results.getJSONObject(i).toString());
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videoId;
    }
}
