package a3a04group5.bookzilla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SearchEntry extends AppCompatActivity implements View.OnClickListener {

    private Button search_button;
    //private TextView output;
    private EditText author, publisher, publishedDate, category, description;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_entry);

        search_button = (Button) findViewById(R.id.search_button);
        author = (EditText) findViewById(R.id.author);
        publisher = (EditText) findViewById(R.id.publisher);
        publishedDate = (EditText) findViewById(R.id.publishedDate);
        category = (EditText) findViewById(R.id.category);
        description = (EditText) findViewById(R.id.description);

        //output = (TextView) findViewById(R.id.attributes_title);

        search_button.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(SearchEntry.this, SearchParseActivity.class);
                        intent.putExtra("author", author.getText().toString());
                        intent.putExtra("publisher", publisher.getText().toString());
                        intent.putExtra("publishedDate", publishedDate.getText().toString());
                        intent.putExtra("category", category.getText().toString());
                        intent.putExtra("description", description.getText().toString());

                        startActivity(intent);
                    }
                });


    }

    public void onClick(View v){
        /*THIS IS TO TEST WHETHER THE INPUT FIELDS DISPLAY CORRECTLY
        if (v.getId() == search_button.getId()){
            text = author.getText().toString();
            output.setText(text);
        }
        */


    }


    public void buttonOnClick(View v) {

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0) ;
        toast.makeText(SearchEntry.this, author.getText(), toast.LENGTH_SHORT).show();

    }
}
