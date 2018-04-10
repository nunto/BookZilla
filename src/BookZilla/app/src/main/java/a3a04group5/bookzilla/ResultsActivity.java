package a3a04group5.bookzilla;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Arrays;

public class ResultsActivity extends AppCompatActivity {

    private PopupWindow popupWindow;
    private ConstraintLayout cLayout;
    private TextView popupText;
    private String[] titles;
    private String[] authors;
    private double[] ratings;
    private String[] categories;
    private String[] moreInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Retrieving volume information returned from last activity
        Bundle extras = getIntent().getExtras();
        String volumes = extras.getString("volumes");
        String[] urls = extras.getStringArray("urls");
        titles = extras.getStringArray("titles");
        authors = extras.getStringArray("authors");
        ratings = extras.getDoubleArray("ratings");
        categories = extras.getStringArray("categories");
        moreInfo = extras.getStringArray("moreinfo");

        System.out.println("-----------------------\nArrays Length:\n--------------------------");
        System.out.println(Arrays.toString(titles));
        System.out.println(Arrays.toString(authors));
        System.out.println(Arrays.toString(ratings));
        System.out.println(Arrays.toString(categories));
        System.out.println(Arrays.toString(moreInfo));
        // Initializing LinearLayout used for scrolling images
        LinearLayout layout = (LinearLayout) findViewById(R.id.imageLinear);
        try {
            JSONObject books = new JSONObject(volumes);
            JSONArray booksArray = books.getJSONArray("items");

            for (int i = 0; i < booksArray.length(); i++) {
                final ImageView imageView = new ImageView(this);

                if (urls!= null && !urls[i].equals("x")) {
                    DownloadImage di = new DownloadImage(imageView);
                    if (urls.length > i) {
                        di.execute(urls[i]);
                    }
                } else {
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_no_imageurl));
                }
                imageView.setId(i);
                imageView.setPadding(2, 2, 2, 2);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                layout.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bookSelectPopup(v, imageView.getId());
                    }
                });
            }

        } catch(JSONException e) {
            Toast toast = Toast.makeText(ResultsActivity.this, "JSON object could not be created.", Toast.LENGTH_SHORT);
            toast.show();

            System.err.println(e.getMessage());
        }

    }

    private void bookSelectPopup(View V, final int id) {

        cLayout = (ConstraintLayout) findViewById(R.id.constraint_layout_results);
        // Initializing LayoutInflater to inflate popup_layout as a popup
        LayoutInflater inflater = (LayoutInflater) ResultsActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }

        // Button to close popup
        ImageButton done = (ImageButton) popupView.findViewById(R.id.popup_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close popup
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(cLayout, Gravity.CENTER, 0, 0);
        String textInfo = "Title: " + titles[id] + "\nAuthor(s): " + authors[id] + "\nCategories: "
                + categories[id] +"\nRating: " + ratings[id] +"\nMore Info";
        SpannableString result_info = new SpannableString(textInfo);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(moreInfo[id]));
                startActivity(intent);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        result_info.setSpan(clickableSpan, textInfo.indexOf("More Info"), textInfo.indexOf("More Info") + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ((TextView) popupWindow.getContentView().findViewById(R.id.book_info)).setText(result_info);
        ((TextView) popupWindow.getContentView().findViewById(R.id.book_info)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) popupWindow.getContentView().findViewById(R.id.book_info)).setHighlightColor(Color.rgb(92, 196, 234));
    }
}

// Class provided via Android Developers
class DownloadImage extends AsyncTask<String, Void, Bitmap> {

    ImageView bmImage;

    public DownloadImage(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urlDisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch(Exception e) {
            System.out.println("No thumbnail");
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}