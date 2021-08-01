package ir.MostafaSafaeipour.rapid;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScooterDetailBlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScooterDetailBlankFragment extends Fragment {

    private TextView Batterytxt;
    private TextView Nearbytxt;
    private TextView Clocktxt;

    public Button Startbtn;
    public Button GrayExitbtn;

    private String ScooterID;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScooterDetailBlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScooterDetailBlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScooterDetailBlankFragment newInstance(String param1, String param2) {
        ScooterDetailBlankFragment fragment = new ScooterDetailBlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scooter_detail_blank, container, false);
        Startbtn = v.findViewById(R.id.StartButton);
        GrayExitbtn = v.findViewById(R.id.GrayExitbutton);

        Batterytxt = v.findViewById(R.id.BatteryText);
        Nearbytxt = v.findViewById(R.id.NearbyText);
        Clocktxt = v.findViewById(R.id.TimerText);

        Typeface thisFont = Typeface.createFromAsset(getActivity().getAssets(),"fonts/avenir.ttc");

        Batterytxt.setTypeface(thisFont);
        Nearbytxt.setTypeface(thisFont);
        Clocktxt.setTypeface(thisFont);
        return v;
    }

    public void setBatteryTextViewText(String Percentage){
        Batterytxt.setText(Percentage + '%');
    }

    public void setNearbyTextViewText(String Kilometer){
        Nearbytxt.setText(Kilometer + " Km");
    }

    public void setClockTextViewText(String Minute,String Second){
        Clocktxt.setText(Minute + ':' + Second);
    }

    public void setScooterIDOfScooterDetailFragment(String scooterid){
        ScooterID = scooterid;
    }
    public String getScooterIDOfScooterDetailFragment(){
        return ScooterID;
    }
}