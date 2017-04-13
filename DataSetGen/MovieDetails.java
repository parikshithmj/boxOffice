package DataSetGen;

import Commons.Utils;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by parmj on 4/6/17.
 */
public class MovieDetails {
    public static void main(String[] args){

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{}");
        int count = 0;
        String[] months = new String[5050];
        try{
            FileReader reader = new FileReader("/home/parmj/BoxOffice/src/DataSetGen/movie_metadata.csv");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            if((line = bufferedReader.readLine())!=null){
                //do nothing
            }
            while ((line = bufferedReader.readLine()) != null) {
                if(count% 39== 0){
                    Thread.sleep(12000);
                }
                else if(count% 400 == 0){
                    Thread.sleep(60000);
                }
                String[] values = line.split(",");
                String movieName = null;
                if(values!=null && values.length >= 11)
                    movieName = values[11];
                Request request = new Request.Builder()
                        .url("https://api.themoviedb.org/3/search/movie?api_key="+ Utils.MOVIEDB_API_KEY +"&language=en-US&query="+movieName+"&page=1&include_adult=false\n")
                        .get()
                        .build();
                Response response = client.newCall(request).execute();

                String json = response.body().string();
                JSONObject jsonObject = new JSONObject(json);
                if(!jsonObject.has("results")){
                    System.out.println("Not found for : Movie name is "+movieName);
                    System.out.println("***********************************************");
                    System.out.println(json);
                    System.out.println("***********************************************");

                    continue;
                }
                JSONArray results = (JSONArray)jsonObject.get("results");
                if(results.length() ==0 ){
                    System.out.println("Not found for : Movie name is "+movieName);
                    continue;
                }
                JSONObject dateObj = new JSONObject(results.get(0).toString());
                String date = dateObj.getString("release_date");
                String month = null;
                if(date!=null && date.split("-").length >=2){
                    month = getMonthName(Integer.parseInt(date.split("-")[1]));
                }
                months[count] = month;
                count++;
                //Rate limit 40 per 10 second.



            }
            reader.close();

            BufferedReader file = new BufferedReader(new FileReader("/home/parmj/BoxOffice/src/DataSetGen/movie_metadata.csv"));
            StringBuffer inputBuffer = new StringBuffer();

            file.readLine(); // skip first line
            count = 0;

            while ((line = file.readLine()) != null) {
                inputBuffer.append(line);
                inputBuffer.append(","+months[count]);
                inputBuffer.append('\n');
                count++;
            }
            String inputStr = inputBuffer.toString();
            file.close();
            // write the new String with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream("/home/parmj/BoxOffice/src/DataSetGen/movie_metadata.csv");
            fileOut.write(inputStr.getBytes());
            fileOut.close();


            // mongoCRUD.insert(json, "boxoffice", "upcomingMovies");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static String getMonthName(int month){
        String monthStr = null;
        switch (month){
            case 1 : monthStr = "January";
                    break;
            case 2 : monthStr = "Febraury";
                break;
            case 3 : monthStr = "March";
                break;
            case 4 : monthStr = "April";
                break;
            case 5 : monthStr = "May";
                break;
            case 6 : monthStr = "June";
                break;
            case 7 : monthStr = "July";
                break;
            case 8 : monthStr = "August";
                break;
            case 9 : monthStr = "September";
                break;
            case 10 : monthStr = "October";
                break;
            case 11 : monthStr = "November";
                break;
            case 12 : monthStr = "December";
                break;
            default:
                break;

        }
        return monthStr;
    }
}
