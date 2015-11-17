package by.anatoldeveloper.hallscheme.hall;

/**
 * Created by Moneyman.ru on 28.10.2015.
 * Copyright Moneyman.ru
 */
public interface Seat {

    int id();
    int color();
    String marker();
    String selectedSeat();
    HallScheme.SeatStatus status();
    void setStatus(HallScheme.SeatStatus status);

}