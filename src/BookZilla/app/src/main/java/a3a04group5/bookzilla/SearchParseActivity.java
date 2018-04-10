package a3a04group5.bookzilla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import org.json.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchParseActivity extends AppCompatActivity implements OnTaskCompleted {

    final static String API_KEY = BuildConfig.APIKey;

    private List<Volumes> results = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_parse);

        Bundle extras = getIntent().getExtras();
        // Retrieving search information from last activity
        String author = extras.getString("author");
        String publisher = extras.getString("publisher");
        String publishedDate = extras.getString("publishedDate");
        String category = extras.getString("category");
        String description= extras.getString("description");

        BookQuery bookQuery = new BookQuery();
        bookQuery.listener = this;
        BookQuery bookQuery_category = new BookQuery();
        BookQuery bookQuery_attribute = new BookQuery();
        BookQuery bookQuery_desc = new BookQuery();

        // Strings for each experts request
        String query = "";
        String categoryQuery = "";
        String attributeQuery = "";
        String descQuery = "";

        // Setting up category query
        if (!TextUtils.isEmpty(category)) {
            categoryQuery = "+subject:" + category;
        }
        // Setting up attribute query (author)
        if (!TextUtils.isEmpty(author)) {
            attributeQuery += "+inauthor:" + author;
        }
        // Setting up attribute query (publisher)
        if (!TextUtils.isEmpty(publisher)) {
            attributeQuery += "+inpublisher" + publisher;
        }
        // Setting up description query
        if (!TextUtils.isEmpty(description)) {
            String[] keywords = description.split(" ");
            for (String s : keywords) {
                descQuery += "+" + s;
            }
        }
        // Assembling final query
        query = descQuery + attributeQuery + categoryQuery;
        System.out.println(query);
        if (TextUtils.isEmpty(query)) {
            Toast toast = Toast.makeText(SearchParseActivity.this, "At least one entry is required", Toast.LENGTH_SHORT);
            toast.show();
            startActivity(new Intent(SearchParseActivity.this, MainActivity.class));
        }
        bookQuery.execute(query);
    }


    // After query has been executed
    @Override
    public void onTaskCompleted(Volumes volumes) {
        results.add(volumes);
        Intent intent = new Intent(SearchParseActivity.this, ResultsActivity.class);
        List<String> urls = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<String> authors = new ArrayList<>();
        List<Double> ratings = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        List<String> moreinfo = new ArrayList<>();
        String tmpUrl = "";
        for (Volume volume : volumes.getItems()) {
            Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
            if (volumeInfo.getImageLinks() != null) {
                if (volumeInfo.getImageLinks().getLarge() != null) {
                    tmpUrl= (volumeInfo.getImageLinks().getLarge());
                }
                else if (volumeInfo.getImageLinks().getMedium() != null) {
                    tmpUrl = volumeInfo.getImageLinks().getMedium();
                }
                else if (volumeInfo.getImageLinks().getThumbnail() != null) {
                    tmpUrl = volumeInfo.getImageLinks().getThumbnail();
                }
            }
            else {
                tmpUrl = "x";
            }
            // Checking results for null and adding if they are okay
            urls.add(tmpUrl);
            if (volumeInfo.getTitle() != null) {
                titles.add(volumeInfo.getTitle());
            } else {
                titles.add("No title listed");
            }
            if (volumeInfo.getAuthors() != null) {
                authors.add(volumeInfo.getAuthors().toString());
            } else {
                authors.add("No authors listed");
            }
            if (volumeInfo.getAverageRating() != null) {
                ratings.add(volumeInfo.getAverageRating());
            } else {
                ratings.add(-1.0);
            }
            if (volumeInfo.getCategories() != null) {
                categories.add(volumeInfo.getCategories().toString());
            }   else {
                categories.add("Unlisted");
            } if (volumeInfo.getInfoLink() != null) {
                moreinfo.add(volumeInfo.getInfoLink());
            } else {
                moreinfo.add("No book link available");
            }

        }
        // Turning lists into arrays so they can be sent as extras
        String[] resultUrls = urls.toArray(new String[0]);
        String[] resultTitles = titles.toArray(new String[0]);
        String[] resultAuthors = authors.toArray(new String[0]);
        double[] resultRatings = new double[ratings.size()];
        for (int i = 0; i < ratings.size(); i++) {
            resultRatings[i] = (double) ratings.get(i);
        }
        String[] resultCategories = categories.toArray(new String[0]);
        String[] resultMoreInfo = moreinfo.toArray(new String[0]);

        // Sending extras along to the next activity to avoid having to parse JSON again
        intent.putExtra("urls", resultUrls);
        intent.putExtra("titles", resultTitles);
        intent.putExtra("authors", resultAuthors);
        intent.putExtra("ratings", resultRatings);
        intent.putExtra("categories", resultCategories);
        intent.putExtra("moreinfo", resultMoreInfo);
        intent.putExtra("volumes", volumes.toString());
        intent.putExtra("totalResults", volumes.getTotalItems());
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SearchParseActivity.this, SearchEntry.class));
    }
}