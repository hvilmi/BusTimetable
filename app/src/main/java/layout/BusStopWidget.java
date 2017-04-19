package layout;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import esim.busstopwidget.BusLineInfo;
import esim.busstopwidget.BusLineInfoContainer;
import esim.busstopwidget.BusLineReceiver;
import esim.busstopwidget.BusLineService;
import esim.busstopwidget.MainActivity;
import esim.busstopwidget.R;

/**
 * Implementation of App Widget functionality.
 */
public class BusStopWidget extends AppWidgetProvider {

    public String busLineString;
    public int counter = 0;



    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {



        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bus_stop_widget);
        String tempString = "";
        if(busLineString != null) {
            views.setTextViewText(R.id.busLineText1, busLineString);
            Log.d("Text Written", "updateAppWidget: " + busLineString);
        }
        else {
            views.setTextViewText(R.id.busLineText1, "No line found"+Integer.toString(counter));
            Log.d("Text Written", "updateAppWidget: No line found"+Integer.toString(counter));
            counter += 1;

        }

        Intent intent = new Intent(context, BusStopWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.updateButton, pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(new ComponentName(context.getPackageName(), BusStopWidget.class.getName()), views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.busLineText1);
        super.onUpdate(context,appWidgetManager,appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Intent busLineService = new Intent(context, BusLineService.class);
        busLineService.putExtra(BusLineService.DESTINATION, "oulu keskusta");
        context.startService(busLineService);
    }


    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled

    }

    private void displayBusLines(BusLineInfoContainer busLines) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if(bundle != null) {

            BusLineInfoContainer busLines = (BusLineInfoContainer) intent.getSerializableExtra(BusLineService.BUSLINES);
            if (busLines != null) {
                BusLineInfo busLineInfo = busLines.getBusLine(0);
                busLineString = busLineInfo.getDepartureStop() + "[" + busLineInfo.getShortName() + "]->" + busLineInfo.getArrivalStop() + "\n";
                Log.d("widget", "onReceive: " + busLineString);
                /*RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bus_stop_widget);
                views.setTextViewText(R.id.busLineText1, busLineText);*/
            }
            else {
                Intent busLineService = new Intent(context, BusLineService.class);
                busLineService.putExtra(BusLineService.DESTINATION, "oulu keskusta");
                context.startService(busLineService);
            }
        }
        /*
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), BusStopWidget.class.getName());
        int[] ids = appWidgetManager.getAppWidgetIds(thisAppWidget);
        //onUpdate(context, appWidgetManager, ids);
        */
        super.onReceive(context, intent);

    }
}
