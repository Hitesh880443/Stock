package com.sam_chordas.android.stockhawk.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.data.WidgetDataProvider;

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
