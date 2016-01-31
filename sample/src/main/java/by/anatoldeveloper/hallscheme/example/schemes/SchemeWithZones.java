package by.anatoldeveloper.hallscheme.example.schemes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import by.anatoldeveloper.hallscheme.example.R;
import by.anatoldeveloper.hallscheme.example.SeatExample;
import by.anatoldeveloper.hallscheme.example.ZoneExample;
import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.ScenePosition;
import by.anatoldeveloper.hallscheme.hall.Seat;
import by.anatoldeveloper.hallscheme.hall.SeatListener;
import by.anatoldeveloper.hallscheme.hall.Zone;
import by.anatoldeveloper.hallscheme.hall.ZoneListener;
import by.anatoldeveloper.hallscheme.view.ZoomableImageView;

/**
 * Created by Nublo on 31.01.2016.
 * Copyright Nublo
 */
public class SchemeWithZones extends Fragment {

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
        scheme.setZones(zones());
        scheme.setZoneListener(new ZoneListener() {
            @Override
            public void zoneClick(int id) {
                Toast.makeText(getActivity(), "Zone click " + id, Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    public Seat[][] schemeWithScene() {
        Seat seats[][] = new Seat[23][26];
        int k = 0;
        for (int i = 0; i < 23; i++)
            for(int j = 0; j < 26; j++) {
                SeatExample seat = new SeatExample();
                seat.id = ++k;
                seat.selectedSeatMarker = String.valueOf(j+1);
                seat.status = HallScheme.SeatStatus.EMPTY;
                seats[i][j] = seat;
                if (i == 22 && (j < 5 || j > 20)) {
                    seat.status = HallScheme.SeatStatus.BUSY;
                }
                if (i == 21 && (j < 6 || j > 19)) {
                    seat.status = HallScheme.SeatStatus.BUSY;
                }
                if (i == 20 && (j < 7 || j > 18)) {
                    seat.status = HallScheme.SeatStatus.BUSY;
                }
                if (i > 16 && i < 21 && (j < 7 || j > 18)) {
                    seat.status = HallScheme.SeatStatus.BUSY;
                }
                if (i > 7 && i < 16 && (j < 6 || j > 19)) {
                    seat.status = HallScheme.SeatStatus.BUSY;
                }
                if (i == 4 && (j < 5 || j > 20)) {
                    seat.status = HallScheme.SeatStatus.BUSY;
                }
                if (i > 4 && i < 8 && (j < 3 || j > 22)) {
                    seat.status = HallScheme.SeatStatus.FREE;
                    seat.color = Color.GREEN;
                }

            }
        return seats;
    }

    public List<Zone> zones() {
        List<Zone> zones = new ArrayList<>();
        ZoneExample zone1 = new ZoneExample(1, 8, 17, 10, 6, getActivity().getResources().getColor(R.color.dark_green), "Not used in current version");
        ZoneExample zone2 = new ZoneExample(2, 8, 4, 10, 12, Color.DKGRAY, "Not used in current version");
        ZoneExample zone3 = new ZoneExample(3, 0, 0, 26, 2, getActivity().getResources().getColor(R.color.dark_purple), "Not used in current version");
        zones.add(zone1);
        zones.add(zone2);
        zones.add(zone3);
        return zones;
    }

}