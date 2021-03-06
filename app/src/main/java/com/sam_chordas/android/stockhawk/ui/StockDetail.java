package com.sam_chordas.android.stockhawk.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StockDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    @Bind(R.id.stockLineChart)
    LineChartView stockLineChart;

    private String selectedSymbol = "";
    private LineSet lineSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        ButterKnife.bind(this);
        if (getIntent().getExtras() != null && !TextUtils.isEmpty
                (getIntent().getExtras().getString(getString(R.string.symbol)))) {
            selectedSymbol = getIntent().getExtras().getString(getString(R.string.symbol));
        }

        setupUI();

        Bundle stockBundle = new Bundle();
        stockBundle.putString(getString(R.string.symbol), selectedSymbol);
        getLoaderManager().initLoader(0, stockBundle, this);
    }


    public void setupUI() {

        lineSet = new LineSet();
        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.RED);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(1f));
        stockLineChart.setBorderSpacing(1)
                .setAxisBorderValues(-100, 800, 50)
                .setXLabels(AxisController.LabelPosition.OUTSIDE)
                .setYLabels(AxisController.LabelPosition.OUTSIDE)
                .setLabelsColor(Color.RED)
                .setXAxis(false)
                .setYAxis(false)
                .setBorderSpacing(Tools.fromDpToPx(5))
                .setGrid(ChartView.GridType.HORIZONTAL, gridPaint);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // This narrows the return to only the stocks that are most current.
      /*    return new CursorLoader(this, QuoteProvider.Quotes.CONTENT_URI,
              new String[]{ QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{args.getString(getResources().getString(R.string.string_symbol))},
                null);
*/


        return new CursorLoader(this, QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns.BIDPRICE},
                QuoteColumns.SYMBOL + " = ?",
                new String[]{args.getString(getResources().getString(R.string.string_symbol))},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("Load Done", "YES");
        putDataToGraph(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void putDataToGraph(Cursor dataCoursor) {
        dataCoursor.moveToFirst();
        for (int i = 0; i < dataCoursor.getCount(); i++) {
            float price = Float.parseFloat(dataCoursor.getString(dataCoursor.getColumnIndex(QuoteColumns.BIDPRICE)));
            lineSet.addPoint("" + i, price);
            dataCoursor.moveToNext();
        }
        lineSet.setColor(Color.RED)
                .setDotsStrokeThickness(Tools.fromDpToPx(2))
                .setDotsStrokeColor(Color.BLUE)
                .setDotsColor(Color.CYAN);
        stockLineChart.addData(lineSet);
        stockLineChart.show();
    }
}
