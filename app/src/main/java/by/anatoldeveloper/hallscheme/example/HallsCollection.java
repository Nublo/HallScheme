package by.anatoldeveloper.hallscheme.example;

import android.graphics.Color;

import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.Seat;

/**
 * Created by Moneyman.ru on 06.12.2015.
 * Copyright Moneyman.ru
 */
public class HallsCollection {

    public static Seat[][] basicScheme() {
        Seat seats[][] = new Seat[10][10];
        for (int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++) {
                SeatExample seat = new SeatExample();
                seat.id = i * 10 + (j+1);
                seat.selectedSeatMarker = String.valueOf(i+1);
                seat.status = HallScheme.SeatStatus.FREE;
                seats[i][j] = seat;
            }
        return seats;
    }

    public static Seat[][] schemeWithScene() {
        Seat seats[][] = new Seat[16][29];
        int k = 0;
        for (int i = 0; i < 16; i++)
            for(int j = 0; j < 29; j++) {
                SeatExample seat = new SeatExample();
                seat.id = ++k;
                seat.selectedSeatMarker = String.valueOf(j+1);
                seat.status = HallScheme.SeatStatus.EMPTY;
                seats[i][j] = seat;
                if (i < 5 && j < 4) {
                    seat.status = HallScheme.SeatStatus.BUSY;
                    if (i < 4) {
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.color = Color.argb(255, 60, 179, 113);
                    }
                }
                if (j > 1 && j < 5 && i > 4) {
                    seat.status = HallScheme.SeatStatus.BUSY;
                    if (i > 13) {
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.color = Color.argb(255, 148, 51, 145);
                    }
                }
                if (j == 4 && i > 1 && i < 5)
                    seat.status = HallScheme.SeatStatus.BUSY;
                if (i < 5 && j > 24) {
                    seat.status = HallScheme.SeatStatus.BUSY;
                    if (i < 4) {
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.color = Color.argb(255, 60, 179, 113);
                    }
                }
                if (j > 23 && j < 27 && i > 4) {
                    seat.status = HallScheme.SeatStatus.BUSY;
                    if (i > 13) {
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.color = Color.argb(255, 148, 51, 145);
                    }
                }
                if (j == 24 && i > 1 && i < 5)
                    seat.status = HallScheme.SeatStatus.BUSY;
                if (i > 3 && j > 6 && j < 22) {
                    seat.status = HallScheme.SeatStatus.BUSY;
                    if (i > 13) {
                        seat.status = HallScheme.SeatStatus.FREE;
                        seat.color = Color.argb(255, 43, 108, 196);
                    }
                }

            }
        return seats;
    }

}