package a3a04group5.bookzilla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volumes;

import org.json.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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



    @Override
    public void onTaskCompleted(Volumes volumes) {
        System.out.println("MADE IT HERE---------------------------!");
        System.out.println(volumes.isEmpty());
        results.add(volumes);
        System.out.println("Success!");
    }
}