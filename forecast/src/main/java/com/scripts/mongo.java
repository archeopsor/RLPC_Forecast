package com.scripts;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

public class mongo {
    static Scanner file;
    private static String URI; 
    static {
        try {
            file = new Scanner(new File("forecast\\src\\main\\resources\\mongo_uri.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to find File in static block.");
        }
        URI = file.useDelimiter("\\Z").next();
        file.close();
    }
    
    public static HashMap<String, Integer> getElo() {
        MongoClient client = MongoClients.create(URI);
        HashMap<String, Integer> ratings = new HashMap<String, Integer>();
        MongoDatabase database = client.getDatabase("rlpc-news");
        MongoCollection<Document> teams = database.getCollection("teams");

        MongoCursor<Document> cursor = teams.find().iterator();

        while (cursor.hasNext()) {
            Document team = cursor.next();
            Document elo = (Document) team.get("elo");
            ratings.put(team.get("team").toString(), Integer.parseInt(elo.get("elo").toString()));
        }
        return ratings;
    }

    // public static void main(String[] args) {
    //     System.out.println(getElo().size());
    // }

}
