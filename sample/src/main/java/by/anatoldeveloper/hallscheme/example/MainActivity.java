package by.anatoldeveloper.hallscheme.example;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import by.anatoldeveloper.hallscheme.example.schemes.SchemeBasic;
import by.anatoldeveloper.hallscheme.example.schemes.SchemeCustomTypeface;
import by.anatoldeveloper.hallscheme.example.schemes.SchemeDarkTheme;
import by.anatoldeveloper.hallscheme.example.schemes.SchemeMaxSelectedSeats;
import by.anatoldeveloper.hallscheme.example.schemes.SchemeWithMarkers;
import by.anatoldeveloper.hallscheme.example.schemes.SchemeWithScene;
import by.anatoldeveloper.hallscheme.example.schemes.SchemeWithZones;

/**
 * Created by Nublo on 06.12.2015.
 * Copyright Nublo
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new ListFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_on_github:
                openOnGithub();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openOnGithub() {
        Intent openBrowser = new Intent(Intent.ACTION_VIEW);
        openBrowser.setData(Uri.parse("https://github.com/Nublo/HallScheme"));
        startActivity(openBrowser);
    }

    public static class ListFragment extends Fragment {

        private static final String[] halls = {"Basic hall scheme", "Scheme with scene", "Scheme with markers",
                "Custom colors scheme", "Custom typeface", "Maximum selected seats", "Scheme with zones"};

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.halls_fragment, container, false);
            ListView hallListView = (ListView) rootView.findViewById(R.id.hall_examples);
            ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, halls);
            hallListView.setAdapter(listAdapter);

            hallListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0 :
                            replaceFragmentAndAddToBackStack(new SchemeBasic());
                            break;
                        case 1 :
                            replaceFragmentAndAddToBackStack(new SchemeWithScene());
                            break;
                        case 2 :
                            replaceFragmentAndAddToBackStack(new SchemeWithMarkers());
                            break;
                        case 3 :
                            replaceFragmentAndAddToBackStack(new SchemeDarkTheme());
                            break;
                        case 4 :
                            replaceFragmentAndAddToBackStack(new SchemeCustomTypeface());
                            break;
                        case 5 :
                            replaceFragmentAndAddToBackStack(new SchemeMaxSelectedSeats());
                            break;
                        case 6 :
                            replaceFragmentAndAddToBackStack(new SchemeWithZones());
                            break;
                    }
                }
            });
            return rootView;
        }

        public void replaceFragmentAndAddToBackStack(Fragment fragment) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

}