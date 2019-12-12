package com.example.cataloguemovieconsumer.viewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cataloguemovieconsumer.config.Config;
import com.example.cataloguemovieconsumer.model.MovieVideos;
import com.example.cataloguemovieconsumer.model.TvShowVideos;
import com.example.cataloguemovieconsumer.response.ResponseMovieDetail;
import com.example.cataloguemovieconsumer.response.ResponseMovieVideos;
import com.example.cataloguemovieconsumer.response.ResponseTvShowDetail;
import com.example.cataloguemovieconsumer.response.ResponseTvShowVideos;
import com.example.cataloguemovieconsumer.rest.ApiClient;
import com.example.cataloguemovieconsumer.rest.ApiInterface;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private ApiInterface apiService;

    private static final String API_KEY= Config.API_KEY;
    private static final String URL_VIDEO = Config.VIDEO_URL;

    //bagian movie video
    private MutableLiveData<String> StringMovieVideo = new MutableLiveData<>();
    private void setListMovieVideo(final String id){
        System.out.println("cekId"+ id);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService.getMovieVideos(Integer.parseInt(id), API_KEY).enqueue(new Callback<ResponseMovieVideos>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMovieVideos> call, @NonNull Response<ResponseMovieVideos> response) {
                System.out.println("responseIn"+response);
                if (response.isSuccessful()){

                    if (response.body() != null) {
                        if (response.body().getResults().size()>0){
                            System.out.println("testing"+ id);
                            List<MovieVideos> movieVideos = response.body().getResults();
                            String key = movieVideos.get(0).getKey();

                            String path = URL_VIDEO+key;

                            StringMovieVideo.postValue(path);

                        }else {
                            Log.d("response", String.valueOf(response));
                        }
                    }

                }else{
                    Log.d("response", String.valueOf(response));
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseMovieVideos> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.e("error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    public LiveData<String> getListMovieVideos(String id) {
        setListMovieVideo(id);
        return StringMovieVideo;
    }

    //bagian tv video
    private MutableLiveData<String> StringTvVideo = new MutableLiveData<>();
    private void setListTvVideo(final String id){
        System.out.println("cekId"+ id);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService.getTvVideos(Integer.parseInt(id), API_KEY).enqueue(new Callback<ResponseTvShowVideos>() {
            @Override
            public void onResponse(@NonNull Call<ResponseTvShowVideos> call, @NonNull Response<ResponseTvShowVideos> response) {
                System.out.println("responseIn"+response);
                if (response.isSuccessful()){

                    if (response.body() != null) {
                        if (response.body().getResults().size()>0){
                            List<TvShowVideos> tvVideos = response.body().getResults();
                            String key = tvVideos.get(0).getKey();

                            String path = URL_VIDEO+key;

                            StringTvVideo.postValue(path);

                        }else {
                            Log.d("response", String.valueOf(response));
                        }
                    }

                }else{
                    Log.d("response", String.valueOf(response));
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseTvShowVideos> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.e("error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    public LiveData<String> getListTvVideos(String id) {
        setListTvVideo(id);
        return StringTvVideo;
    }

    //bagian detail movie
    private MutableLiveData<ResponseMovieDetail> detailMovie = new MutableLiveData<>();

    private void setDetailMovie(final int id){

        apiService = ApiClient.getClient().create(ApiInterface.class);

        apiService.getMovieDetails(id, API_KEY, Config.EN_LANGUAGE).enqueue(new Callback<ResponseMovieDetail>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMovieDetail> call, @NonNull Response<ResponseMovieDetail> response) {
                System.out.println("responseIn"+response);
                if (response.isSuccessful()){

                    detailMovie.postValue(response.body());
                    if (response.body() != null) {
                        System.out.print("id nya merupakan" + response.body().getId());
                    }

                }else{
                    Log.d("response", String.valueOf(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMovieDetail> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.e("error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    public LiveData<ResponseMovieDetail> getDetailMovie(int id) {
        setDetailMovie(id);
        return detailMovie;
    }

    //bagian detail tvshow
    private MutableLiveData<ResponseTvShowDetail> detailTv = new MutableLiveData<>();

    private void setDetailTv(final String id){

        apiService = ApiClient.getClient().create(ApiInterface.class);

        apiService.getTvDetails(Integer.parseInt(id), API_KEY, Config.EN_LANGUAGE).enqueue(new Callback<ResponseTvShowDetail>() {
            @Override
            public void onResponse(@NonNull Call<ResponseTvShowDetail> call, @NonNull Response<ResponseTvShowDetail> response) {
                System.out.println("responseIn"+response);
                if (response.isSuccessful()){

                    detailTv.postValue(response.body());

                }else{
                    Log.d("response", String.valueOf(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseTvShowDetail> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.e("error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    public LiveData<ResponseTvShowDetail> getDetailTv(String id) {
        setDetailTv(id);
        return detailTv;
    }


}
