package by.anatoldeveloper.hallscheme.example.schemes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import by.anatoldeveloper.hallscheme.example.R;
import by.anatoldeveloper.hallscheme.example.SeatExample;
import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.ScenePosition;
import by.anatoldeveloper.hallscheme.hall.Seat;
import by.anatoldeveloper.hallscheme.hall.SeatListener;
import by.anatoldeveloper.hallscheme.view.ZoomableImageView;

/**
 * Created by Nublo on 05.12.2015.
 * Copyright Nublo
 */
public class SchemeWithSceneFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basic_scheme_fragment, container, false);
        ZoomableImageView imageView = (ZoomableImageView) rootView.findViewById(R.id.zoomable_image);
        HallScheme scheme = new HallScheme(imageView, schemeWithScene(), getActivity());
        scheme.setScenePosition(ScenePosition.SOUTH);
        scheme.setSeatListener(new SeatListener() {

            @Override
            public void selectSeat(int id) {
                Toast.makeText(getActivity(), "select seat " + id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unSelectSeat(int id) {
                Toast.makeText(getActivity(), "unSelect seat " + id, Toast.LENGTH_SHORT).show();
            }

        });
        return rootView;
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