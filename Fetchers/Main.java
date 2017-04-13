package Fetchers;

import okhttp3.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {


    //    Fetchers.YouTubeFetcher yf = invokeYouTubeFetcher();

      //  FacebookFetcher fb = new FacebookFetcher();
      //  fb.getReactions("1789404601333511");
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/upcoming?page=1&language=en-US&api_key=d07387463c2c0d2998d6bd09ec481c17")
                .get()
                .build();
        try{
            Response response = client.newCall(request).execute();
            String json = response.body().string();
           // System.out.println(json);
            MongoCRUD mongoCRUD = new MongoCRUD();

            mongoCRUD.insert(json, "boxoffice", "upcomingMovies");
        }catch(Exception e){
            e.printStackTrace();
        }
        

    }

    private static YouTubeFetcher invokeYouTubeFetcher() {
        YouTubeFetcher yf = new YouTubeFetcher();
        MongoCRUD mongoCRUD = new MongoCRUD();

        List<String> upcomingMoviesList = mongoCRUD.retrieve("boxoffice","upcomingMovies");

        for(int i =0; i <upcomingMoviesList.size(); i++){
            String movieName = upcomingMoviesList.get(i);
            System.out.println("Youtube stats for the movie name"+movieName);
            String youtubeVideoId = yf.getVideoId(movieName);
            System.out.print("Id:"+youtubeVideoId);
            System.out.print(yf.getStats(youtubeVideoId));

        }
        return yf;
    }
}
