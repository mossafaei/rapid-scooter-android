package ir.MostafaSafaeipour.rapid;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Time;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripDetailBlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripDetailBlankFragment extends Fragment {

    private TextView MasafatText;
    private TextView TimeText;
    private TextView PriceText;

    public Button ExitButton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TripDetailBlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TripDetailBlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TripDetailBlankFragment newInstance(String param1, String param2) {
        TripDetailBlankFragment fragment = new TripDetailBlankFragment();
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
        View v = inflater.inflate(R.layout.fragment_trip_detail_blank, container, false);

        ExitButton = (Button) v.findViewById(R.id.GrayCloseButtton);

        MasafatText = (TextView) v.findViewById(R.id.MasafatText);
        TimeText = (TextView) v.findViewById(R.id.TimeText);
        PriceText = (TextView) v.findViewById(R.id.PriceText);

        Typeface englishFont = Typeface.createFromAsset(getActivity().getAssets(),"fonts/avenir.ttc");

        MasafatText.setTypeface(englishFont);
        TimeText.setTypeface(englishFont);
        PriceText.setTypeface(englishFont);

        TextView masafatnametxt = (TextView) v.findViewById(R.id.MasafatNameText);
        TextView modatnametxt = (TextView) v.findViewById(R.id.ModatZamanNameText);
        TextView hazinetxt = (TextView) v.findViewById(R.id.HazineNameText);

        Typeface persianFont = Typeface.createFromAsset(getActivity().getAssets(),"fonts/B Yekan.ttf");

        masafatnametxt.setTypeface(persianFont);
        modatnametxt.setTypeface(persianFont);
        hazinetxt.setTypeface(persianFont);
        return v;
    }

    public void setMasafatTextViewText(String Kilometer){
        MasafatText.setText(Kilometer + " Km");
    }

    public  void setTimeTextViewText(String Minutes,String Second){
        TimeText.setText(Minutes + ':' + Second);
    }

    public void setPriceTextViewText(String Price){
        PriceText.setText(Price + 'T');
    }
}