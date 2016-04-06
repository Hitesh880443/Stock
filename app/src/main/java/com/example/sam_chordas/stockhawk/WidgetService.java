package com.example.sam_chordas.stockhawk;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by 880443 on 3/30/2016.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        WidgetDataProvider dataProvider = new WidgetDataProvider(
                getApplicationContext(), intent);
        return dataProvider;
    }
}
