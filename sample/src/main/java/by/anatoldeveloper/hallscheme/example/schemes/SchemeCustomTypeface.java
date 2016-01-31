package by.anatoldeveloper.hallscheme.example.schemes;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    boolean doubleTap = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.scheme_with_button, container, false);
        final ZoomableImageView imageView = (ZoomableImageView) rootView.findViewById(R.id.zoomable_image);
        final Button zoomButton = (Button) rootView.findViewById(R.id.scheme_button);
        zoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doubleTap = !doubleTap;
                imageView.setZoomByDoubleTap(doubleTap);
                if (doubleTap) {
                    zoomButton.setText(getString(R.string.double_tap_enabled));
                } else {
                    zoomButton.setText(getString(R.string.double_tap_disabled));
                }
            }
        });
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
        Seat seats[][] = new Seat[7][14];
        int k = 0;
        for (int i = 0; i < 7; i++)
            for(int j = 0; j < 14; j++) {
                SeatExample seat = new SeatExample();
                seat.id = ++k;
                seat.selectedSeatMarker = String.valueOf(j+1);
                seat.status = HallScheme.SeatStatus.BUSY;
                if (j == 0 || j == 13) {
                    seat.marker = String.valueOf(i+1);
                    seat.status = HallScheme.SeatStatus.INFO;
                }
                if (j > 5 && j < 8) {
                    seat.status = HallScheme.SeatStatus.EMPTY;
                }
                if (((j > 0 && j < 6) || (j > 7 && j < 13)) && i < 2) {
                    seat.status = HallScheme.SeatStatus.FREE;
                    seat.color = Color.RED;
                }
                if (((j > 0 && j < 6) || (j > 7 && j < 13)) && i > 4) {
                    seat.status = HallScheme.SeatStatus.FREE;
                    seat.color = Color.GREEN;
                }
                seats[i][j] = seat;
            }
        return seats;
    }

}