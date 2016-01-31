package by.anatoldeveloper.hallscheme.hall;

/**
 * Created by Nublo on 28.10.2015.
 * Copyright Nublo
 */
public interface Seat {

    int id();
    int color();
    String marker();
    String selectedSeat();
    HallScheme.SeatStatus status();
    void setStatus(HallScheme.SeatStatus status);

}