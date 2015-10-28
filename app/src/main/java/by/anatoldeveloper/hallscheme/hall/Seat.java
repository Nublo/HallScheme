package by.anatoldeveloper.hallscheme.hall;

import android.graphics.Color;

/**
 * Created by Moneyman.ru on 28.10.2015.
 * Copyright Moneyman.ru
 */
public interface Seat {

    int id();
    Color color();
    String marker();
    HallScheme.SeatStatus status();

}