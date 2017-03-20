package esim.busstopwidget;

/**
 * Created by Hannes on 20.3.2017.
 */

public class BusLineInfo {
    private String shortName;
    private String name;
    private String departureTime;
    private String arrivalTime;

    public BusLineInfo(String shortName, String name, String departureTime, String arrivalTime) {
        this.shortName = shortName;
        this.name = name;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
}
