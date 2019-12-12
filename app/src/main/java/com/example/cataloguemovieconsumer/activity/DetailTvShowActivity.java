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
import com.example.cataloguemovieconsumer.model.TvShow;
import com.example.cataloguemovieconsumer.response.ResponseTvShowDetail;
import com.example.cataloguemovieconsumer.viewModel.MainViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class DetailTvShowActivity extends AppCompatActivity {

    public static final String TAG = "tv";
    Integer id;

    ImageView iv_poster_tv, iv_poster_tv_front;
    TextView tv_title_tv_detail, tv_rb_tv, tv_genre, tv_release_date, tv_description, tv_production_company, tv_number_episode, tv_popularity;
    RatingBar rb_tv;

    private ProgressBar progressBar;
    private ConstraintLayout cl_2;

    WebView webView;

    TvShow tvShow;

    private ArrayList<Cast> castArrayList = new ArrayList<>();
    RecyclerView rv_cast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv_show);
        setTitle("Detail Tv Show");

        iv_poster_tv = findViewById(R.id.iv_poster_tv);
        iv_poster_tv_front = findViewById(R.id.iv_poster_tv_front);
        tv_title_tv_detail = findViewById(R.id.tv_title_tv_detail);
        tv_genre = findViewById(R.id.tv_genre);
        tv_release_date = findViewById(R.id.tv_release_date);
        tv_description = findViewById(R.id.tv_description);
        tv_rb_tv = findViewById(R.id.tv_rb_tv);
        tv_production_company = findViewById(R.id.tv_production_company);
        tv_number_episode = findViewById(R.id.tv_number_episode);
        tv_popularity = findViewById(R.id.tv_popularity);
        rb_tv = findViewById(R.id.rb_tv);
        webView = findViewById(R.id.mWebView);
        progressBar = findViewById(R.id.progressbar);
        cl_2 =  findViewById(R.id.cl_2);
        rv_cast = findViewById(R.id.rv_cast);

        progressBar.setVisibility(View.VISIBLE);
        cl_2.setVisibility(View.GONE);

        tvShow = getIntent().getExtras().getParcelable(TAG);

        //get Id
        id = tvShow.getId();

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getListTvVideos(String.valueOf(id)).observe(this, getListTvVideo);

        MainViewModel detailViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        detailViewModel.getDetailTv(String.valueOf(id)).observe(this, getDetailTv);

    }

    private final Observer<ResponseTvShowDetail> getDetailTv = new Observer<ResponseTvShowDetail>() {
        @Override
        public void onChanged(ResponseTvShowDetail responseTvShowDetail) {
            if (responseTvShowDetail != null) {
                tv_title_tv_detail.setText(responseTvShowDetail.getOriginalName());
                rb_tv.setRating(Float.parseFloat(String.valueOf(responseTvShowDetail.getVoteAverage())));
                tv_rb_tv.setText(String.valueOf(responseTvShowDetail.getVoteAverage()));
                tv_release_date.setText(responseTvShowDetail.getFirstAirDate());
                tv_description.setText(responseTvShowDetail.getOverview());
                tv_genre.setText(String.valueOf(responseTvShowDetail.getGenres().get(0).getName()));
                tv_number_episode.setText(String.valueOf(responseTvShowDetail.getNumberOfEpisodes()));
                tv_popularity.setText(String.valueOf(responseTvShowDetail.getPopularity()));
                tv_production_company.setText(responseTvShowDetail.getProductionCompanies().get(0).getName());

                castArrayList.addAll(responseTvShowDetail.getCredits().getCast());

                System.out.println("data "+castArrayList.size());

                CastAdapter adapter = new CastAdapter(getApplicationContext(), castArrayList);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                        RecyclerView.HORIZONTAL, false);
                rv_cast.setLayoutManager(layoutManager);
                rv_cast.setItemAnimator(new DefaultItemAnimator());
                rv_cast.setAdapter(adapter);

                Picasso.with(DetailTvShowActivity.this)
                        .load(Config.IMAGE_W500+responseTvShowDetail.getBackdropPath())
                        .transform(new BlurTransformation(getApplicationContext(), 5,1))
                        .fit()
                        .centerCrop()
                        .into(iv_poster_tv);

                Picasso.with(DetailTvShowActivity.this)
                        .load(Config.IMAGE_W500+responseTvShowDetail.getPosterPath())
                        .fit()
                        .centerCrop()
                        .into(iv_poster_tv_front);

                cl_2.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    private Observer<String> getListTvVideo = new Observer<String>() {
        @SuppressLint("SetJavaScriptEnabled")
        @Override
        public void onChanged(String tvs) {
            if (tvs != null){

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
                webView.loadUrl(tvs);
            }else {
                webView.setVisibility(View.INVISIBLE);
                webView.setVisibility(View.GONE);
            }
        }
    };



}