package a3a04group5.bookzilla;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volumes;

import java.io.IOException;


public class BookQuery extends AsyncTask <String, Void, Volumes>{

    private final static String API_KEY = BuildConfig.APIKey; // APIKey taken from gradle
    public OnTaskCompleted listener = null; // Used for sending result back to main activity
    private final static long maxResults = 40;

    @Override
    protected Volumes doInBackground(String... query) {
        // Google Books Client
        Books books = new Books.Builder(AndroidHttp.newCompatibleTransport(), AndroidJsonFactory.getDefaultInstance(),null)
                .setApplicationName("BookZilla")
                .setGoogleClientRequestInitializer(new BooksRequestInitializer(API_KEY))
                .build();
        Books.Volumes.List volumesList;
        Volumes volumes = null;
        try {
            // Setting then executing query
            System.out.println("Query:" + query[0]);
            volumesList = books.volumes().list(query[0]);
            volumesList.setMaxResults(maxResults);
            volumesList.setKey(API_KEY);
            volumes = volumesList.execute();

        } catch(IOException ex) {
            ex.printStackTrace();
        }


        // Checking if any matching books were found
        if ( volumes == null || volumes.getItems() == null || volumes.getTotalItems() == 0 ) {
            System.exit(0);
        }
        return volumes;
    }

    @Override
    protected void onPostExecute(Volumes volumes) {
        // Allowing the volumes to be retrieved
        listener.onTaskCompleted(volumes);
    }
}
