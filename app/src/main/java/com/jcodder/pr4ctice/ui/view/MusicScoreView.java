package com.jcodder.pr4ctice.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.jcodder.pr4ctice.R;
import com.jcodder.pr4ctice.model.Attribute;
import com.jcodder.pr4ctice.model.Measure;
import com.jcodder.pr4ctice.model.Note;
import com.jcodder.pr4ctice.model.Part;

import androidx.core.content.res.ResourcesCompat;

public class MusicScoreView extends View implements OnTouchListener {
    Paint paint = new Paint();
    Part part;
    private static final int FONTSIZE = 40;

    public MusicScoreView(Context context) {
        super(context);
        this.setOnTouchListener(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setTypeface(ResourcesCompat.getFont(getContext(), R.font.bravura));
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, FONTSIZE, getResources().getDisplayMetrics()));
        paint.setTextAlign(Paint.Align.LEFT);

        int y = FONTSIZE * 4;
        String lastClef = "";
        String lastKey = "";
        for (Measure measure : part.getMeasures()) {
            int x = 30;
            canvas.drawText("\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE014=\uE030",x,y,paint);
            x = x + FONTSIZE/2;
            if (measure.getAttributes().get(0).getClef() == null) {
                canvas.drawText(lastClef,x,y,paint);
                x = x + FONTSIZE * 2;
            }
            if (measure.getAttributes().get(0).getKey() == null) {
                canvas.drawText(lastKey,x,y,paint);
                x = x + FONTSIZE * 2;
            }
            for (Attribute attribute : measure.getAttributes()) {
                if (attribute.getClef() != null) {
                    canvas.drawText(attribute.getClef().getGlyphs(),x,y,paint);
                    lastClef = attribute.getClef().getGlyphs();
                    x = x + FONTSIZE * 2;
                }

                if (attribute.getKey() != null) {
                    canvas.drawText(attribute.getKey().getGlyphs(),x,y,paint);
                    lastKey = attribute.getKey().getGlyphs();
                    x = x + FONTSIZE * (attribute.getKey().getFifths());
                }

                if (attribute.getTime() != null) {
                    canvas.drawText(attribute.getTime().getGlyphs(),x,y,paint);
                    x = x + FONTSIZE * 3;
                }
            }

            for (Note note : measure.getNotes()) {
                if (note.isSelected()) {
                    paint.setColor(Color.GREEN);
                }
                canvas.drawText(note.getGlyphs(),x,y,paint);
                if (note.getBeam() != null && !"end".equals(note.getBeam())) {
                    x = x + FONTSIZE + FONTSIZE/2;
                } else {
                    x = x + FONTSIZE * 2;
                }
                if (note.isSelected()) {
                    paint.setColor(Color.BLACK);
                }
            }
            y = y + FONTSIZE * 4 + Math.round(FONTSIZE * 1.5f);
        }

            /*Path wallpath = new Path();
            wallpath.reset();
            wallpath.moveTo(282, 90);
            wallpath.lineTo(282, 100);
            wallpath.lineTo(394, 120);
            wallpath.lineTo(394, 110);
            wallpath.lineTo(282, 90);
            canvas.drawPath(wallpath, paint);*/
        //canvas.drawLine(282, 90, 395, 110, paint);
    }

    @Override
    public void onMeasure(int width, int height) {
        int newHeight = height;
        if (part != null) {
            newHeight = FONTSIZE * 4;
            for (Measure ignored : part.getMeasures()) {
                newHeight = newHeight + FONTSIZE * 4 + Math.round(FONTSIZE * 1.5f);
            }
        }
        setMeasuredDimension(width, newHeight);
    }

    public void setPart(Part part) {
        this.part = part;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                part.getMeasures().get(0).getNotes().get(0).setSelected(true);
                this.invalidate();
                break;
        }
        return false;
    }
}
