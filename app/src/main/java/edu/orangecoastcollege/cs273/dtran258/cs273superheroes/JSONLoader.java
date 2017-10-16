package edu.orangecoastcollege.cs273.dtran258.cs273superheroes;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class loads Superhero data from a formatted JSON (JavaScript Object Notation) file. Populates
 * data model (Superhero) with data.
 *
 * @author Derek Tran
 * @version 1.0
 * @since October 15, 2017
 */

public class JSONLoader
{
    /**
     * Loads JSON data from a file in the assets directory.
     *
     * @param context The activity from which the data is loaded.
     * @return A list of <code>Superhero</code> objects loaded from the JSON file.
     * @throws IOException If there is an error reading from the JSON file.
     */
    public static List<Superhero> loadJSONFromAsset(Context context) throws IOException
    {
        List<Superhero> allSuperherosList = new ArrayList<>();
        // Reads JSON file to an input stream
        InputStream is = context.getAssets().open("cs273superheroes.json");
        // Read from the input stream w/o blocking by next caller of method for this input stream
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        // Convert byte[] into a String
        String json = new String(buffer, "UTF-8");

        try
        {
            // Retrieve the JSON array from the JSON string
            JSONObject jsonRootObject = new JSONObject(json);
            JSONArray allSuperheroesJSON = jsonRootObject.getJSONArray("CS273Superheroes");

            // Convert each JSON object in the JSON array into a Superhero object and add each
            // Superhero into the list of all Superheroes
            int length = allSuperheroesJSON.length();
            for (int i = 0; i < length; i++)
            {
                JSONObject superHeroJSON = allSuperheroesJSON.getJSONObject(i);
                Superhero superhero = new Superhero(superHeroJSON.getString("Username"), superHeroJSON.getString("Name"), superHeroJSON.getString("Superpower"), superHeroJSON.getString("OneThing"));
                allSuperherosList.add(superhero);
            }
        } catch (JSONException e)
        {
            Log.e("CS 273 Superheroes", e.getMessage());
        }

        return allSuperherosList;

    }
}
