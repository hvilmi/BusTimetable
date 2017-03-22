package esim.busstopwidget;

import java.util.ArrayList;

/**
 * Created by hvilmi on 22.3.2017.
 */

public class BusLineInfoContainer {

    private ArrayList<BusLineInfo> busLines;

    public BusLineInfoContainer() {
        busLines = new ArrayList<BusLineInfo>();
    }

    public void addBusLine(BusLineInfo newBusLine) {
        busLines.add(newBusLine);
    }

    public int getSize() {
        return busLines.size();
    }

    public BusLineInfo getBusLine(int index) {
        return busLines.get(index);
    }
}
