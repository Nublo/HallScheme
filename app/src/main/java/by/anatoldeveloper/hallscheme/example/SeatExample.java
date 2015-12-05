package by.anatoldeveloper.hallscheme.example;

import android.graphics.Color;

import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.Seat;

/**
 * Created by Moneyman.ru on 05.12.2015.
 * Copyright Moneyman.ru
 */
public class SeatExample implements Seat {

    int id;
    String marker;
    String selectedSeatMarker;
    HallScheme.SeatStatus status;

    @Override
    public int id() {
        return id;
    }

    @Override
    public int color() {
        return Color.RED;
    }

    @Override
    public String marker() {
        return marker;
    }

    @Override
    public String selectedSeat() {
        return selectedSeatMarker;
    }

    @Override
    public HallScheme.SeatStatus status() {
        return status;
    }

    @Override
    public void setStatus(HallScheme.SeatStatus status) {
        this.status = status;
    }

}