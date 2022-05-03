package com.example.android.didyoufeelit;

public class Event {

    public final String title;

    public final String numOfPeople;

    public final String perceivedStrenght;

    public Event(String eventTitle, String eventNumOfPeople, String eventPerceivedStrength) {
        title = eventTitle;
        numOfPeople = eventNumOfPeople;
        perceivedStrenght = eventPerceivedStrength;
    }
}
