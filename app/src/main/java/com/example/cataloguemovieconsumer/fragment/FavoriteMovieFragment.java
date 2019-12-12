package com.example.cataloguemovieconsumer;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cataloguemovieconsumer.adapter.FavoriteMovieAdapter;
import com.example.cataloguemovieconsumer.model.Movie;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.cataloguemovieconsumer.db.DatabaseContract.FavoriteColumn.CONTENT_URI_MOVIE;
import static com.example.cataloguemovieconsumer.helper.MappingHelper.mapCursorToArrayListMovie;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment implements LoadFavoriteMovieCallback{
    private FavoriteMovieAdapter adapter;

    private ProgressBar progressBar;


    public FavoriteMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_movie, container, false);

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setSubtitle("Favorite");

        progressBar = view.findViewById(R.id.progressbar);

        RecyclerView recyclerView = view.findViewById(R.id.rvMovies);
        adapter = new FavoriteMovieAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getActivity());
        getActivity().getContentResolver().registerContentObserver(CONTENT_URI_MOVIE, true, myObserver);
        new getData(getActivity(), this).execute();

        return view;
    }

    @Override
    public void postExecute(Cursor movies) {

        ArrayList<Movie> movieArrayList = mapCursorToArrayListMovie(movies);
        if (movieArrayList.size() > 0) {
            adapter.setListMovies(movieArrayList);
            progressBar.setVisibility(View.GONE);
        } else {
            Toast.makeText(getActivity(), "Tidak Ada data saat ini", Toast.LENGTH_SHORT).show();
            adapter.setListMovies(new ArrayList<Movie>());
            progressBar.setVisibility(View.GONE);
        }
    }

    private static class getData extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavoriteMovieCallback> weakCallback;


        private getData(Context context, LoadFavoriteMovieCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return weakContext.get().getContentResolver().query(CONTENT_URI_MOVIE, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor data) {
            super.onPostExecute(data);
            weakCallback.get().postExecute(data);
        }

    }

    static class DataObserver extends ContentObserver {

        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new getData(context, (LoadFavoriteMovieCallback) context).execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new getData(getActivity(), this).execute();
    }

}
