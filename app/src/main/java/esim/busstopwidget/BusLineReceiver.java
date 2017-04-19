package esim.busstopwidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Created by hvilmi on 4/19/17.
 */

public class BusLineReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            BusLineInfoContainer busLines = (BusLineInfoContainer) intent.getSerializableExtra(BusLineService.BUSLINES);
            BusLineInfo busLineInfo = busLines.getBusLine(0);
            String busLineText = busLineInfo.getDepartureStop() + "[" + busLineInfo.getShortName() + "]->" + busLineInfo.getArrivalStop() + "\n";

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bus_stop_widget);
            views.setTextViewText(R.id.busLineText1, busLineText);
        }
    }
}
