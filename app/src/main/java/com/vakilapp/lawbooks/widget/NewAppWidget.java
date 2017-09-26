package com.vakilapp.lawbooks.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.vakilapp.lawbooks.R;
import com.vakilapp.lawbooks.models.Chapter;
import com.vakilapp.lawbooks.ui.MainActivity;

public class NewAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Chapter chap) {

        Intent goToRecipeListActivity = new Intent(context,MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, goToRecipeListActivity,
                PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.removeAllViews(R.id.widget_chapters_list);
        views.setTextViewText(R.id.appwidget_text, context.getResources().getString(R.string.appwidget_text,chap.getChapterName()));
        views.setOnClickPendingIntent(R.id.widget_root_view, pendingIntent);

            RemoteViews rvChapter = new RemoteViews(context.getPackageName(),
                    R.layout.chapter_widget_item);
        rvChapter.setTextViewText(R.id.chapter_content,
                    String.valueOf(chap.getContent()));
            views.addView(R.id.widget_chapters_list, rvChapter);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    public static void updateChapterWidgets(Context context, AppWidgetManager appWidgetManager,
                                           Chapter chap, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, chap);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

