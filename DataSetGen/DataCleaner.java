package DataSetGen;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * Created by parmj on 4/14/17.
 */
public class DataCleaner {
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
