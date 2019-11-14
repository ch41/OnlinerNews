package siergo_o.onlinernews.view;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import siergo_o.onlinernews.R;

public class DescriptionFragment extends AppCompatActivity {

    String description;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_description_fragment);

        TextView descriptionTW = findViewById(R.id.description_tw);
        Bundle extras = getIntent().getExtras();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        final Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        backArrow.setColorFilter(Color.parseColor("#1a6dbb"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#1a6dbb'>Новости </font>"));


        if (extras != null) {
            description = extras.getString("Description");
        }
        descriptionTW.setText(description);

    }
}
