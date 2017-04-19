package esim.busstopwidget;

import java.io.Serializable;

/**
 * Created by Hannes on 20.3.2017.
 */

public class BusLineInfo implements Serializable {
    private String shortName;
    private String name;
    private String departureTime;
    private String arrivalTime;
    private String departureStop;
    private String arrivalStop;

    public BusLineInfo(String shortName, String name, String departureTime, String arrivalTime, String departureStop, String arrivalStop) {
        this.shortName = shortName;
        this.name = name;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureStop = departureStop;
        this.arrivalStop = arrivalStop;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureStop() {
        return departureStop;
    }

    public void setDepartureStop(String departureStop) {
        this.departureStop = departureStop;
    }

    public String getArrivalStop() {
        return arrivalStop;
    }

    public void setArrivalStop(String arrivalStop) {
        this.arrivalStop = arrivalStop;
    }

    public void addSwitch(String newBus) {
        shortName = shortName + "/" + newBus;
    }
}
