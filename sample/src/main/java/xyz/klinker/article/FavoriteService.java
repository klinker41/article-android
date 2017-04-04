package xyz.klinker.article;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import xyz.klinker.android.article.data.Article;

public class FavoriteService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Article article = new Article(intent);
        Log.v("SavedArticle", article.title);
        return super.onStartCommand(intent, flags, startId);
    }
}
