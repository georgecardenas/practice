package com.jcodder.pr4ctice.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.jcodder.pr4ctice.R;
import com.jcodder.pr4ctice.model.Part;
import com.jcodder.pr4ctice.parser.MusicXMLParser;
import com.jcodder.pr4ctice.ui.view.MusicScoreView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        try {
            MusicXMLParser mXMLParser = new MusicXMLParser();
            final Part part = mXMLParser.parse(getActivity().getResources().openRawResource(R.raw.fpagana));

            ScrollView scroll = root.findViewById(R.id.scroll);
            MusicScoreView msView = new MusicScoreView(getActivity());
            msView.setPart(part);
            scroll.addView(msView);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return root;
    }
}
