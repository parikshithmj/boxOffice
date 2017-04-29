package Fetchers;

import com.mongodb.*;
import com.mongodb.util.JSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parmj on 2/25/17.
 */
public class MongoCRUD {
    MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://boxoffice:boxoffice@ds155130.mlab.com:55130/boxoffice"));

    public void insert(String json,String dbName, String collectionName, String jsonDataName){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);

            ArrayList<String> list = new ArrayList<String>();
            DB db = mongoClient.getDB( dbName );

            DBCollection collection = db.getCollection(collectionName);

            if(jsonObject.has(jsonDataName)){
                JSONArray results = (JSONArray)jsonObject.get(jsonDataName);
                for(int i=0; i<results.length(); i++){
                    list.add(results.getJSONObject(i).toString());
                }

                System.out.println(list.size());

                for(int i=0; i<list.size(); i++){
                    DBObject dbObject = (DBObject) JSON.parse(list.get(i));
                    collection.insert(dbObject);
                }
            }
            else {
                DBObject dbObject = (DBObject) JSON.parse(jsonObject.toString());
                collection.insert(dbObject);

            }
            }catch(JSONException e) {
                e.printStackTrace();
            }
        }

    public List<String> retrieve(String dbName, String collectionName){

        List<String> upcomingMoviesList = new ArrayList<String>();
        try {
            DB db = mongoClient.getDB( dbName);
            DBCollection collection = db.getCollection(collectionName);

            DBCursor cursor = collection.find();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                String movieName = dbObject.get("title").toString();
                upcomingMoviesList.add(movieName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return upcomingMoviesList;

    }
}
