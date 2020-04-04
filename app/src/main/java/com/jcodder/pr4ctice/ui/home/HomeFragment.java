package com.jcodder.pr4ctice.ui.home;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import nu.xom.ParsingException;

import com.jcodder.pr4ctice.R;

import org.jfugue.integration.MusicXmlParser;
import org.jfugue.pattern.Pattern;
import org.staccato.StaccatoParserListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        textView.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.bravura));
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setTextSize(40f);
                textView.setTextColor(Color.BLACK);
                textView.setText("\uE050 \uE09E\uE084\uE09F\uE084 \uEB90\uE1D5\uE8E0=\uE8E1\uEB90\uE1D5    \uEB90\uE0FB-\uEB90\uE210");        //U+EB90 U+E0A4 U+E210
    }
});

        /*MusicXmlParser parser = null;
        try {
            parser = new MusicXmlParser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        StaccatoParserListener listener = new StaccatoParserListener();
        parser.addParserListener(listener);
        try {
            InputStream is = getActivity().getAssets().open("fpagana.xml");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);

            File targetFile = new File(getActivity().getFilesDir(), "fpagana.xml");
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
            outStream.flush();
            outStream.close();

            parser.parse(targetFile);
            Pattern staccatoPattern = listener.getPattern();
            System.out.println(staccatoPattern);

        } catch (IOException | ParsingException e) {
            e.printStackTrace();
        }*/

        return root;
    }
}
