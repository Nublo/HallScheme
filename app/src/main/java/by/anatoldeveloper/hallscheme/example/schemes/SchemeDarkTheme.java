package by.anatoldeveloper.hallscheme.example.schemes;

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
 * Created by Nublo on 06.12.2015.
 * Copyright Nublo
 */
public class SchemeDarkTheme extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basic_scheme_fragment, container, false);
        rootView.setBackgroundColor(getActivity().getResources().getColor(R.color.dark_grey));
        ZoomableImageView imageView = (ZoomableImageView) rootView.findViewById(R.id.zoomable_image);
        HallScheme scheme = new HallScheme(imageView, basicScheme(), getActivity());
        scheme.setBackgroundColor(getActivity().getResources().getColor(R.color.dark_grey));
        scheme.setScenePosition(ScenePosition.EAST);
        scheme.setScenePaintColor(getActivity().getResources().getColor(R.color.dark_grey));
        scheme.setSelectedSeatTextColor(getActivity().getResources().getColor(R.color.dark_grey));
        scheme.setSceneBackgroundColor(getActivity().getResources().getColor(R.color.white));
        scheme.setChosenColor(getActivity().getResources().getColor(R.color.white));
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

    public Seat[][] basicScheme() {
        Seat seats[][] = new Seat[10][10];
        for (int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++) {
                SeatExample seat = new SeatExample();
                seat.id = i * 10 + (j+1);
                seat.selectedSeatMarker = String.valueOf(i+1);
                seat.status = HallScheme.SeatStatus.BUSY;
                if (j < 2) {
                    seat.status = HallScheme.SeatStatus.FREE;
                }
                if (i > 3 && i < 6) {
                    seat.status = HallScheme.SeatStatus.EMPTY;
                }
                seat.color = getActivity().getResources().getColor(R.color.dark_green);
                seats[i][j] = seat;
            }
        return seats;
    }

}