package by.anatoldeveloper.hallscheme.example.schemes;

import android.graphics.Typeface;
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
 * Created by Nublo on 03.01.2016.
 * Copyright Nublo
 */
public class SchemeCustomTypeface extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basic_scheme_fragment, container, false);
        ZoomableImageView imageView = (ZoomableImageView) rootView.findViewById(R.id.zoomable_image);
        HallScheme scheme = new HallScheme(imageView, basicScheme(), getActivity());
        scheme.setScenePosition(ScenePosition.NORTH);
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
        scheme.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/DroidSansMono.ttf"));
        return rootView;
    }

    public Seat[][] basicScheme() {
        Seat seats[][] = new Seat[12][18];
        int k = 0;
        for (int i = 0; i < 12; i++)
            for(int j = 0; j < 18; j++) {
                SeatExample seat = new SeatExample();
                seat.id = ++k;
                seat.selectedSeatMarker = String.valueOf(i+1);
                seat.status = HallScheme.SeatStatus.BUSY;
                if (j == 0 || j == 17) {
                    seat.status = HallScheme.SeatStatus.EMPTY;
                    if (i > 2 && i < 10) {
                        seat.marker = String.valueOf(i);
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                }
                if (((j > 0 && j < 3) || (j > 14 && j < 17)) && i == 0) {
                    seat.status = HallScheme.SeatStatus.EMPTY;
                    if (j == 2 || j == 15) {
                        seat.marker = String.valueOf(i+1);
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                }
                if (((j > 0 && j < 2) || (j > 15 && j < 17)) && i == 1) {
                    seat.status = HallScheme.SeatStatus.EMPTY;
                    if (j == 1 || j == 16) {
                        seat.marker = String.valueOf(i+1);
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                }
                if (i == 2)
                    seat.status = HallScheme.SeatStatus.EMPTY;
                if (i > 9 && (j == 1 || j == 16)) {
                    seat.status = HallScheme.SeatStatus.INFO;
                    seat.marker = String.valueOf(i);
                }
                seats[i][j] = seat;
            }
        return seats;
    }

}