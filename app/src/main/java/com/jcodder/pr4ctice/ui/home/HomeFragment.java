package com.jcodder.pr4ctice.ui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jcodder.pr4ctice.R;
import com.jcodder.pr4ctice.model.Part;
import com.jcodder.pr4ctice.parser.MusicXMLParser;
import com.jcodder.pr4ctice.writer.SmuflWriter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //final TextView textView = root.findViewById(R.id.text_home);
        //textView.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.bravura));

        try {
            MusicXMLParser mXMLParser = new MusicXMLParser();
            final Part part = mXMLParser.parse(getActivity().getResources().openRawResource(R.raw.fpagana));

            RelativeLayout relativeLayout = (RelativeLayout) root.findViewById(R.id.part);
            relativeLayout.addView(new PartView(getActivity(), part));
            /*homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    SmuflWriter writer = new SmuflWriter();
                    textView.setTextSize(40f);
                    textView.setTextColor(Color.BLACK);
                    textView.setLineHeight(300);
                    textView.setAllCaps(true);
                    //textView.setText("\uE050 \uE09E\uE081\uE09E\uE082\uE09F\uE084");        //U+EB90 U+E0A4 U+E210

                    textView.setText(writer.write(part));
                }
            });*/
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return root;
    }

    private class PartView extends View {
        Paint paint = new Paint();
        Part part;

        public PartView(Context context, Part part) {
            super(context);
            this.part = part;
        }

        @Override
        public void onDraw(Canvas canvas) {
            SmuflWriter writer = new SmuflWriter();
            paint.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.bravura));
            paint.setColor(Color.BLACK);
            paint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 30, getResources().getDisplayMetrics()));
            paint.setTextAlign(Paint.Align.LEFT);

            List<String> lines = writer.write(part);
            int y = 120;
            for (String line : lines) {
                canvas.drawText(line,30,y,paint);
                y = y + 140;
            }

            Path wallpath = new Path();
            wallpath.reset();
            wallpath.moveTo(282, 90);
            wallpath.lineTo(282, 100);
            wallpath.lineTo(394, 120);
            wallpath.lineTo(394, 110);
            wallpath.lineTo(282, 90);
            canvas.drawPath(wallpath, paint);
            //canvas.drawLine(282, 90, 395, 110, paint);
        }
    }
}
