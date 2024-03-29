package com.example.cataloguemovieconsumer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.cataloguemovieconsumer.MainActivity;
import com.example.cataloguemovieconsumer.R;
import com.example.cataloguemovieconsumer.adapter.CastAdapter;
import com.example.cataloguemovieconsumer.config.Config;
import com.example.cataloguemovieconsumer.model.Cast;
import com.example.cataloguemovieconsumer.model.Movie;
import com.example.cataloguemovieconsumer.response.ResponseMovieDetail;
import com.example.cataloguemovieconsumer.viewModel.MainViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class DetailMovieActivity extends AppCompatActivity {

    public static final String TAG = "movie";

    Integer id;

    ImageView iv_poster_movie, iv_poster_movie_front;
    TextView tv_title_movie_detail, tv_rb_movie, tv_genre, tv_release_date, tv_description, tv_production_company, tv_tag, tv_popularity;
    RatingBar rb_movie;

    private ProgressBar progressBar;
    private ConstraintLayout cl_1;

    WebView webView;

    Movie movie;

    private ArrayList<Cast> castArrayList = new ArrayList<>();
    RecyclerView rv_cast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        setTitle("Detail Movie");

        iv_poster_movie = findViewById(R.id.iv_poster_movie);
        iv_poster_movie_front = findViewById(R.id.iv_poster_movie_front);
        tv_title_movie_detail = findViewById(R.id.tv_title_movie_detail);
        tv_genre = findViewById(R.id.tv_genre);
        tv_release_date = findViewById(R.id.tv_release_date);
        tv_description = findViewById(R.id.tv_description);
        tv_rb_movie = findViewById(R.id.tv_rb_movie);
        tv_production_company = findViewById(R.id.tv_production_company);
        tv_tag = findViewById(R.id.tv_tag);
        tv_popularity = findViewById(R.id.tv_popularity);
        rb_movie = findViewById(R.id.rb_movie);
        webView = findViewById(R.id.mWebView);
        progressBar = findViewById(R.id.progressbar);
        cl_1 =  findViewById(R.id.cl_1);
        rv_cast = findViewById(R.id.rv_cast);

        progressBar.setVisibility(View.VISIBLE);
        cl_1.setVisibility(View.GONE);

        movie = getIntent().getExtras().getParcelable(TAG);
        //get Id
        id = movie.getId();

        Uri uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null) {

                if (cursor.moveToFirst()) movie = new Movie(cursor);
                cursor.close();
            }
        }

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getListMovieVideos(String.valueOf(id)).observe(this, getListMovieVideo);

        MainViewModel detailViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        detailViewModel.getDetailMovie(id).observe(this, getDetailMovie);
    }


    private final Observer<ResponseMovieDetail> getDetailMovie = new Observer<ResponseMovieDetail>() {
        @Override
        public void onChanged(ResponseMovieDetail responseMovieDetail) {
            if (responseMovieDetail != null) {
                tv_title_movie_detail.setText(responseMovieDetail.getTitle());
                rb_movie.setRating(Float.parseFloat(String.valueOf(responseMovieDetail.getVoteAverage())));
                tv_rb_movie.setText(String.valueOf(responseMovieDetail.getVoteAverage()));
                tv_release_date.setText(responseMovieDetail.getReleaseDate());
                tv_description.setText(responseMovieDetail.getOverview());
                tv_genre.setText(String.valueOf(responseMovieDetail.getGenres().get(0).getName()));
                tv_tag.setText(responseMovieDetail.getTagline());
                tv_popularity.setText(String.valueOf(responseMovieDetail.getPopularity()));
                tv_production_company.setText(responseMovieDetail.getProductionCompanies().get(0).getName());

                castArrayList.addAll(responseMovieDetail.getCredits().getCast());

                CastAdapter adapter = new CastAdapter(getApplicationContext(), castArrayList);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                        RecyclerView.HORIZONTAL, false);
                rv_cast.setLayoutManager(layoutManager);
                rv_cast.setItemAnimator(new DefaultItemAnimator());
                rv_cast.setAdapter(adapter);


                if (responseMovieDetail.getBackdropPath() != null){
                    Picasso.with(DetailMovieActivity.this)
                            .load(Config.IMAGE_W500+responseMovieDetail.getBackdropPath())
                            .transform(new BlurTransformation(getApplicationContext(), 5,1))
                            .fit()
                            .centerCrop()
                            .into(iv_poster_movie);
                }else {
                    Picasso.with(DetailMovieActivity.this)
                            .load(Config.IMAGE_W500+responseMovieDetail.getPosterPath())
                            .transform(new BlurTransformation(getApplicationContext(), 5,1))
                            .fit()
                            .centerCrop()
                            .into(iv_poster_movie);
                }

                Picasso.with(DetailMovieActivity.this)
                        .load(Config.IMAGE_W500+responseMovieDetail.getPosterPath())
                        .fit()
                        .centerCrop()
                        .into(iv_poster_movie_front);

                cl_1.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }
    };



    private Observer<String> getListMovieVideo = new Observer<String>() {
        @SuppressLint("SetJavaScriptEnabled")
        @Override
        public void onChanged(String movies) {
            if (movies != null){
                String frameVideo = "<html><body>Video From YouTube<br><iframe width=\"320\" height=\"250\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
                webView.setVisibility(View.VISIBLE);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }
                });

                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadData(frameVideo, "text/html", "utf-8");
                webView.loadUrl(movies);
            }else {
                webView.setVisibility(View.INVISIBLE);
                webView.setVisibility(View.GONE);
            }
        }
    };

    private void goToDaftarKontrakanKosActivity() {
        Intent intent = new Intent(DetailMovieActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToDaftarKontrakanKosActivity();
    }

}