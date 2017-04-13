package DataSetGen;

import Commons.Utils;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * Created by parmj on 4/8/17.
 */
public class FateClassGen {
    public static void main(String[] args){


        int count = 0;
        String[] fate = new String[5050];
        try{
            FileReader reader = new FileReader("/home/parmj/BoxOffice/src/DataSetGen/movie_metadata.csv");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            if((line = bufferedReader.readLine())!=null){
                //do nothing
            }
            while ((line = bufferedReader.readLine()) != null) {

                String[] values = line.split(",");
                float imdbScore = 0;
                if(values!=null && values.length >= 25 &&  values[25].length() >=1){
                    System.out.println("a"+values[25]+"a");

                    imdbScore = Float.parseFloat(values[25]);

                    fate[count] = getFate(imdbScore);
                    count++;
                }
                else
                    fate[count] = "NA";
            }
            reader.close();

            BufferedReader file = new BufferedReader(new FileReader("/home/parmj/BoxOffice/src/DataSetGen/movie_metadata.csv"));
            StringBuffer inputBuffer = new StringBuffer();

            file.readLine(); // skip first line
            count = 0;

            while ((line = file.readLine()) != null) {
                inputBuffer.append(line);
                inputBuffer.append(","+fate[count]);
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
    private static String getFate(float imdbScore){
        String fate = null;
            if(imdbScore >= 8)
                fate = "Superhit";
            else if(imdbScore >= 7)
                fate = "Hit";
            else if(imdbScore >= 6)
                fate = "Average";
            else
                fate = "Flop";

        return fate;
    }


}
