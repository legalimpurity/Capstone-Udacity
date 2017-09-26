package com.vakilapp.lawbooks.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.vakilapp.lawbooks.models.Chapter;

/**
 * Created by rajatkhanna on 27/08/17.
 */

public class ChapterWidgetService extends IntentService {
    public static final String CHAPTER_WIDGET_ACTION_UPDATE = "61c69de8ac396552da39deb2b3727f07";
    private static final String BUNDLE_CHAPTER_WIDGET_DATA = "5ec9a6e55294f2d217d08b10aebbee6f";

    public ChapterWidgetService() {
        super("ChapterWidgetService");
    }

    public static void startActionUpdateRecipeWidgets(Context context, Chapter chapter) {
        Intent intent = new Intent(context, ChapterWidgetService.class);
        intent.setAction(CHAPTER_WIDGET_ACTION_UPDATE);
        intent.putExtra(BUNDLE_CHAPTER_WIDGET_DATA, chapter);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (CHAPTER_WIDGET_ACTION_UPDATE.equals(action) &&
                    intent.getParcelableExtra(BUNDLE_CHAPTER_WIDGET_DATA) != null) {
                handleActionUpdateWidgets((Chapter)intent.getParcelableExtra(BUNDLE_CHAPTER_WIDGET_DATA));
            }
        }
    }

    private void handleActionUpdateWidgets(Chapter chapter) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, NewAppWidget.class));
        NewAppWidget.updateChapterWidgets(this, appWidgetManager, chapter, appWidgetIds);
    }
}