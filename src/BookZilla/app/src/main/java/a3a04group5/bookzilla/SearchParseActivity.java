package a3a04group5.bookzilla;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SearchParseActivity extends AppCompatActivity {

    private String author;
    private String publisher;
    private String publishedDate;
    private String category;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_parse);

        Bundle extras = getIntent().getExtras();
        author = extras.getString("author");
        publisher = extras.getString("publisher");
        publishedDate = extras.getString("publishedDate");
        category = extras.getString("category");
        description= extras.getString("description");
    }
}
