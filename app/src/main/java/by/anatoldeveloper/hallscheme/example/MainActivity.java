package by.anatoldeveloper.hallscheme.example;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new ListFragment()).commit();
    }

    public static class ListFragment extends Fragment {

        private static final String[] halls = {"Basic hall scheme", "Scheme with scene"};

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
                            replaceFragmentAndAddToBackStack(new BasicSchemeFragment());
                            break;
                        default:
                            replaceFragmentAndAddToBackStack(new SchemeWithScene());
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