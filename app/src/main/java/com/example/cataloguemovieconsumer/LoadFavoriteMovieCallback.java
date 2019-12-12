package com.example.cataloguemovieconsumer;

import android.database.Cursor;

public interface LoadFavoriteMovieCallback {
    void postExecute(Cursor movies);
}
