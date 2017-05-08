package DataSetGen;

import Commons.Utils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;


/**
 * Created by parmj on 4/26/17.
 */
public class StringEncoder {
    public static void main(String[] args) {

        String csvFile = "cleaned_movie.csv";
        String line = "";
        String cvsSplitBy = ",";

            try {

                BufferedReader br = new BufferedReader(new FileReader(csvFile));
                StringBuffer inputBuffer = new StringBuffer();
                while ((line = br.readLine()) != null) {

                    // use comma as separator
                    String[] attributes = line.split(cvsSplitBy);
                    String color = attributes[0];//
                    String director_name = attributes[1];
                    String actor_2_name = attributes[5];
                    String actor_1_name = attributes[7];
                    String actor_3_name = attributes[9];
                    String language = attributes[11];
                    String country = attributes[12];
                    String content_rating = attributes[13];
                    String month = attributes[19];

                    inputBuffer.append(Utils.hashString(color));
                    inputBuffer.append(",");
                    inputBuffer.append(Utils.hashString(director_name));
                    inputBuffer.append(",");
                    inputBuffer.append(Utils.hashString(actor_2_name));
                    inputBuffer.append(",");
                    inputBuffer.append(Utils.hashString(actor_1_name));
                    inputBuffer.append(",");
                    inputBuffer.append(Utils.hashString(actor_3_name));
                    inputBuffer.append(",");
                    inputBuffer.append(Utils.hashString(language));
                    inputBuffer.append(",");
                    inputBuffer.append(Utils.hashString(country));
                    inputBuffer.append(",");
                    inputBuffer.append(Utils.hashString(language));
                    inputBuffer.append(",");
                    inputBuffer.append(Utils.hashString(month));
                    inputBuffer.append(",");
                    inputBuffer.append("\n");

                }
                String inputStr = inputBuffer.toString();
                // write the new String with the replaced line OVER the same file
                FileOutputStream fileOut = new FileOutputStream("/home/parmj/BoxOffice/src/DataSetGen/encoded_cleaned_movie.csv");
                fileOut.write(inputStr.getBytes());
                fileOut.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
