package com.sam_chordas.android.stockhawk.ui;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.service.WidgetService;
import com.sam_chordas.android.stockhawk.R;

/**
 * Implementation of App Widget functionality.
 */
public class StockWidget extends AppWidgetProvider {


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stock_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int widgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
            RemoteViews mView = initViews(context, appWidgetManager, widgetId);
            appWidgetManager.updateAppWidget(widgetId, mView);


            Intent intent = new Intent(context, MyStocksActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            mView.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);




            Intent clickIntentTemplate = new Intent(context, StockDetail.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mView.setPendingIntentTemplate(R.id.widgetCollectionList, clickPendingIntentTemplate);
            appWidgetManager.updateAppWidget(widgetId, mView);

        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private RemoteViews initViews(Context context,
                                  AppWidgetManager widgetManager, int widgetId) {

        RemoteViews mView = new RemoteViews(context.getPackageName(),
                R.layout.stock_widget);

        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        mView.setRemoteAdapter(widgetId, R.id.widgetCollectionList, intent);

        return mView;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

