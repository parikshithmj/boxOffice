package Commons;

import Fetchers.MongoCRUD;
import com.mongodb.DBObject;
import com.sun.org.apache.xpath.internal.SourceTree;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Exchanger;

/**
 * Created by parmj on 5/5/17.
 */
public class DataAggregrator {

    public static void main(String args[]) throws IOException {
        DataAggregrator dataAggregrator = new DataAggregrator();

        MongoCRUD mongoCRUD = new MongoCRUD();
        List<String> upcomingMovies = mongoCRUD.retrieveUpcomingMovie("boxoffice","upcomingMovies");
        for(String movieName : upcomingMovies)
            dataAggregrator.getMovieDetails(movieName);
    }
    //given a movie name, get all the social media information.
    //Accumulates from multiple collections and dumps it in the database.
    public void getMovieDetails(String movieName){
        MongoCRUD mongoCRUD = new MongoCRUD();
        StringBuffer result = new StringBuffer();
        Map<String,String> map = new HashMap<String,String>();

        List<DBObject> fbReactionDbObjectList = mongoCRUD.retrieve("boxoffice","fbreactions");
        DBObject dbObjectResult = null;
        for(DBObject dbObjectItr : fbReactionDbObjectList){
            if(dbObjectItr.get("movieName").toString().trim().equals(movieName)){
                dbObjectResult = dbObjectItr;
                break;
            }
        }

        Set<String> itr = dbObjectResult.keySet();
        for(String tmpStr : itr){
            map.put(tmpStr,dbObjectResult.get(tmpStr).toString());
        }

        List<DBObject> ytReactionDbObjectList = mongoCRUD.retrieve("boxoffice","youtubeStats");
        for(DBObject dbObjectItr : ytReactionDbObjectList){
            if(dbObjectItr.get("name").toString().trim().equals(movieName)){
                dbObjectResult = dbObjectItr;
                break;
            }
        }
        itr = dbObjectResult.keySet();
        for(String tmpStr : itr){
            map.put(tmpStr,dbObjectResult.get(tmpStr).toString());
        }

        System.out.println(obtainSocialStats(map)+"For movie:"+movieName);
      //  dbObjectResult.putAll(map);
      //  mongoCRUD.insertDbObject("boxoffice","movieDashBoard",dbObjectResult);
    }

    public double obtainSocialStats(Map<String,String> map){
        DataAggregrator dataAggregrator = new DataAggregrator();

        double numFbLike = 0;
        double numFbSad = 0;
        double numFbAnger = 0;
        double numFbWow = 0;
        double numFbLaugh = 0;
        double numFbLove = 0;

        int numYbLikes = 0;
        int numYbDislikes = 0;
        int numYbViewCount = 0;
        double numYbCommentCount = 0;
        double numYbFavoriteCount = 0;
        double commentSentimentScore = 0;

        Set<String> genres = new HashSet<String>();
        Set<String> keyset = map.keySet();
        for(String tmpStr : keyset){
            if(tmpStr.equals("like"))
                numFbLike += Integer.parseInt(map.get(tmpStr));
            else if(tmpStr.equals("love"))
                numFbLove += Integer.parseInt(map.get(tmpStr));
            else if(tmpStr.equals("wow"))
                numFbWow += Integer.parseInt(map.get(tmpStr));
            else if(tmpStr.equals("haha"))
                numFbLaugh += Integer.parseInt(map.get(tmpStr));
            else if(tmpStr.equals("anger"))
                numFbAnger += Integer.parseInt(map.get(tmpStr));
            else if(tmpStr.equals("sad"))
                numFbSad += Integer.parseInt(map.get(tmpStr));
            else if(tmpStr.equals("statistics")){
                try {
                    JSONObject ytJson = new JSONObject(map.get(tmpStr));
                    numYbLikes += Integer.parseInt(ytJson.get("likeCount").toString());
                    numYbDislikes += Integer.parseInt(ytJson.get("dislikeCount").toString());
                    numYbViewCount += Integer.parseInt(ytJson.get("viewCount").toString());
                    if(ytJson.has("commentCount"))
                    numYbCommentCount += Integer.parseInt(ytJson.get("commentCount").toString());
                    numYbFavoriteCount += Integer.parseInt(ytJson.get("favoriteCount").toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(tmpStr.equals("genreIds")){
                StringTokenizer stk=new StringTokenizer(map.get("genreIds"),"[,]");
                while (stk.hasMoreTokens()){
                    int genreId = Integer.parseInt(stk.nextToken().toString().trim());
                    genres.add(Utils.getGenrebyId(genreId));
                }
            }
            else if(tmpStr.equals("videocomments")){
                String comments = map.get("videocomments");
                try{
                   // commentSentimentScore = dataAggregrator.getSentimentAnalysisScore(comments);
                    int numComments = comments.split("\\$").length;
                    int sentiScore = 0;
                    for(String str: comments.split("\\$")){
                        sentiScore += dataAggregrator.getSentimentAnalysisScore(str);
                    }

                    System.out.println("Sent fpr"+sentiScore);
                }catch (Exception e){
                    System.out.println("Exception while sentiment analysis "+e.getMessage());
                }
            }

        }

        // Considering strong reactions to the posts.
        numFbLike = numFbLike + numFbLove * 1.1;
        numFbLike = numFbLike + numFbWow * 1.2;
        numFbLike = numFbLike - numFbAnger*1.2;
        if(genres.contains("Comedy"))
            numFbLike = numFbLike + numFbLaugh * 2; //to increase the weightage.
        else if(genres.contains("Drama")||genres.contains("Documentary") || genres.contains("History")|| genres.contains("War"))
            numFbSad = numFbSad * 1.2; //to increase the weightage.
        //if it is not a comedy movie, but receives "haha"
        else if(!genres.contains("Comedy") && (genres.contains("Drama")||genres.contains("Documentary") || genres.contains("History")|| genres.contains("War"))){}
            numFbLike = numFbLike - numFbLaugh * 1.2; //to decrease the weightage.

        double fb = numFbLike*100/(numFbAnger+numFbLaugh+numFbLike+numFbLove+numFbWow+numFbLove+numFbSad);
        double yt = ((commentSentimentScore+numYbLikes+numYbFavoriteCount*1.2)*90)/(commentSentimentScore+numYbLikes+numYbDislikes)+ ytViewScore(numYbViewCount);

        return (fb+yt)/2;

    }


    //Score of 1-10 based on number of views.
    private double ytViewScore(int numYtviews){
        double score = 0;
        if(numYtviews < 800000)
            score = 0;
        else if(numYtviews < 1600000)
            score = 1;
        else if(numYtviews < 3200000)
            score = 2;
        else if(numYtviews < 4800000)
            score = 3;
        else if(numYtviews < 6400000)
            score = 4;
        else if(numYtviews < 8000000)
            score = 5;
        else if(numYtviews < 9600000)
            score = 6;
        else if(numYtviews < 11200000)
            score = 7;
        else if(numYtviews < 12800000)
            score = 8;
        else if(numYtviews < 1440000)
            score = 9;
        else if(numYtviews < 1600000)
            score = 10;

        return score;
    }

        private  int getSentimentAnalysisScore(String comments) throws IOException {
            String text = "text="+comments;
            int score = 0;
            double result = 0;
            URL obj = new URL(Utils.NLTK_BASE_URL +"sentiment/");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(text.getBytes());
            os.flush();
            os.close();

            int responseCode = con.getResponseCode();
         //   System.out.println("POST Response Code :: " + responseCode);
            try {
                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result

                    JSONObject jsonObject = new JSONObject(response.toString());
                    score = returnLabelValue(new JSONObject(response.toString()).get("label").toString());
//                    result = Double.parseDouble(new JSONObject(new JSONObject(response.toString()).
//                            get("probability").toString()).get("pos").toString());

                }
                else {
                Thread.sleep(2000);
                System.out.println("POST request not worked");
            }

        } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return score;
        }

    private int returnLabelValue(String sentiment){
            if(sentiment.equals("neg"))
                return -1;
            else if(sentiment.equals("pos"))
                return 1;
            else
                return 0;

        }



//    private int getSentimentAnalysisScore(String comment){
//
//        OkHttpClient client = new OkHttpClient();
//
//        String url = Utils.NLTK_BASE_URL +"sentiment/";
//        MediaType MEDIA_TYPE_MARKDOWN
//                = MediaType.parse("text/plain");
//        try{
//            RequestBody formBody = new FormBody.Builder()
//                    .add("message", "Your message"+"\n")
//                    .build();
////            Request request = new Request.Builder()
////                    .url(url)
////                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, "text=great"))
////                    .build();
//
//            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),"text=great");
//
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(body)
//                    .build();
//            Response response = client.newCall(request).execute();
//
//            System.out.println(""+response);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//
//        //print result
//        return 0;
//
//    }

}

