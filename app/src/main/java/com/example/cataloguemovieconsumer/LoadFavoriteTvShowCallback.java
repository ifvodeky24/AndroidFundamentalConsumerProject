package com.example.cataloguemovieconsumer;

import android.database.Cursor;

public interface LoadFavoriteTvShowCallback {
    void postExecute(Cursor tvshows);
}
