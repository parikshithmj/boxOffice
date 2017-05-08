package DataSetGen;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by parmj on 4/18/17.
 */

//Input :  Dataset from Kaggle with Genre seperated by '|'
//Output : Dataset with Genre columns for each Genre which will be 0 or 1
public class GenreColumnGen {

    public static void main(String args[]){
        int maxAttrLen = 0;
        try{
            FileReader reader = new FileReader("/home/parmj/BoxOffice/src/DataSetGen/movie_metadata.csv");
            BufferedReader file = new BufferedReader(new FileReader("/home/parmj/BoxOffice/src/DataSetGen/movie_metadata.csv"));
            StringBuffer inputBuffer = new StringBuffer();
            file.readLine(); // skip first line
            String line = null;
            while ((line = file.readLine()) != null) {
                inputBuffer.append(line);
                maxAttrLen = Math.max(getMaxLenmultiAttributeSplit(line, 9), maxAttrLen);

                line = multiAttributeSplit(line, 9,maxAttrLen);
                line = genreColumnFiller(line);
                inputBuffer.append(",");
                inputBuffer.append(line);
                inputBuffer.append("\n");
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

    private static int getMaxLenmultiAttributeSplit(String line, int position){
        String[] attr = line.split(",");
        StringBuffer sb = new StringBuffer();
        if(attr!= null && attr.length > position && attr[position]!=null){
            String[] splitValues = attr[position].split("\\|");
            return splitValues.length;

        }
        return 0;

    }
    private static String genreColumnFiller(String line){
        String[] attr = line.split(",");
        int[] positions = new int[21];
        StringBuffer sb = new StringBuffer();
        Map<String,Integer> genreMap = new HashMap<String, Integer>();
        genreMap.put("Action",1);
        genreMap.put("Adventure",2);
        genreMap.put("Animation",3);
        genreMap.put("Biography",4);
        genreMap.put("Comedy",5);
        genreMap.put("Crime",6);
        genreMap.put("Documentary",7);
        genreMap.put("Drama",8);
        genreMap.put("Family",9);
        genreMap.put("Fantasy",10);
        genreMap.put("History",11);
        genreMap.put("Horror",12);
        genreMap.put("Musical",13);
        genreMap.put("Mystery",14);
        genreMap.put("Romance",15);
        genreMap.put("Sci-Fi",16);
        genreMap.put("Sport",17);
        genreMap.put("Thriller",18);
        genreMap.put("Western",19);
        genreMap.put("War",20);

        int i =0;
        for(String genre : attr){
            if(genreMap.containsKey(genre)){
                positions[i] = genreMap.get(genre);
                i++;
            }
        }

         i = 0;
        for(int k=1; k<=20;k++){
            if(positions[i] == k){
                sb.append("1,");
                i++;
            }
            else
                sb.append("0,");
        }

        //Action, Adventure,Animation,Biography,Comedy,Crime,Documentary,Drama,Family,Fantasy,History,Horror,Musical,Mystery,Romance,Sci-Fi,Sport,Thriller,Western,War


        return sb.toString();

    }
    private static String multiAttributeSplit(String line, int position, int maxAttrLen){
        String[] attr = line.split(",");
        StringBuffer sb = new StringBuffer();
        if(attr!= null && attr.length > position && attr[position]!=null){
            String[] splitValues = attr[position].split("\\|");
            int count = 0;
            for(int i =0 ; i<maxAttrLen; i++){
                if(count == 0)
                    sb.append(",");
                if(i < splitValues.length)
                    sb.append(splitValues[i]);
                else
                    sb.append("");
                if(count == splitValues.length - 1)
                    continue;
                sb.append(",");
                count++;

            }
        }
        return sb.toString();

    }
}
