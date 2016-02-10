package emerikbedouin.demonetiktpe;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class WelcomeActivityFragment extends Fragment {

    public WelcomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);

       Button btnTransaction = (Button) rootView.findViewById(R.id.btnNewTransaction);

        btnTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TPEActivity.class);
                //Lancement de l'activité client
                startActivity(intent);

            }
        });


        return rootView;
    }
}
