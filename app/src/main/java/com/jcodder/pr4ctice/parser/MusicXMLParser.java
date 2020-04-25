package com.jcodder.pr4ctice.parser;

import android.util.Xml;

import com.jcodder.pr4ctice.model.Attribute;
import com.jcodder.pr4ctice.model.Clef;
import com.jcodder.pr4ctice.model.Direction;
import com.jcodder.pr4ctice.model.Key;
import com.jcodder.pr4ctice.model.Measure;
import com.jcodder.pr4ctice.model.Note;
import com.jcodder.pr4ctice.model.Part;
import com.jcodder.pr4ctice.model.Pitch;
import com.jcodder.pr4ctice.model.Time;
import com.jcodder.pr4ctice.parser.utils.StringUtils;
import com.jcodder.pr4ctice.parser.utils.Tags;
import com.jcodder.pr4ctice.smufl.SmuflTranslator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class MusicXMLParser {
    private static final String namespace = null;
    private SmuflTranslator translator = new SmuflTranslator();

    public Part parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(inputStream, null);
            parser.nextTag();

            return readScore(parser);
        } finally {
            inputStream.close();
        }

    }

    private Part readScore(XmlPullParser parser) throws XmlPullParserException, IOException {
        Part part = null;
        parser.require(XmlPullParser.START_TAG, namespace, Tags.SCORE_PARTWISE);
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if (parser.getName().equals(Tags.PART)) {
                    part = readPart(parser);
                }
            }
        }
        return part;
    }

    private Part readPart(XmlPullParser parser) throws XmlPullParserException, IOException {
        Part part = new Part();
        parser.require(XmlPullParser.START_TAG, namespace, Tags.PART);
        while (!isEndTag(parser.getName(), parser.getEventType(), Tags.PART)) {
            parser.next();
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if (parser.getName().equals(Tags.MEASURE)) {
                    part.getMeasures().add(readMeasure(parser));
                }
            }
        }
        return part;
    }

    private Measure readMeasure(XmlPullParser parser) throws XmlPullParserException, IOException {
        Measure measure = new Measure();
        parser.require(XmlPullParser.START_TAG, namespace, Tags.MEASURE);
        while (!isEndTag(parser.getName(), parser.getEventType(), Tags.MEASURE)) {
            parser.next();
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                switch (parser.getName()) {
                    case Tags.ATTRIBUTES:
                        measure.getAttributes().add(readAttribute(parser));
                        break;
                    case Tags.NOTE:
                        measure.getNotes().add(readNote(parser));
                        break;
                    case Tags.DIRECTION:
                        measure.getDirections().add(readDirection(parser));
                        break;
                }
            }
        }
        return measure;
    }

    private Note readNote(XmlPullParser parser) throws XmlPullParserException, IOException {
        Note note = new Note();
        String text = null;
        parser.require(XmlPullParser.START_TAG, namespace, Tags.NOTE);
        while (!isEndTag(parser.getName(), parser.getEventType(), Tags.NOTE)) {
            parser.next();
            switch(parser.getEventType()) {
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.START_TAG:
                    if (Tags.PITCH.equals(parser.getName())) {
                        note.setPitch(readPitch(parser));
                    }
                    break;
                case XmlPullParser.END_TAG:
                    switch(parser.getName()) {
                        case Tags.TYPE:
                            note.setType(text);
                            break;
                        case Tags.DOT:
                            note.setDotted(true);
                            break;
                        case Tags.BEAM:
                            note.setBeam(text);
                            break;
                        case Tags.REST:
                            note.setRest(true);
                            break;
                    }
                    break;
                default:
            }
        }
        note.setGlyphs(translator.getNoteGlyphs(note));
        return note;
    }

    private Pitch readPitch(XmlPullParser parser) throws XmlPullParserException, IOException {
        Pitch pitch = new Pitch();
        String text = null;
        parser.require(XmlPullParser.START_TAG, namespace, Tags.PITCH);
        while (!isEndTag(parser.getName(), parser.getEventType(), Tags.PITCH)) {
            parser.next();
            switch(parser.getEventType()) {
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    switch(parser.getName()) {
                        case Tags.STEP:
                            pitch.setStep(text);
                            break;
                        case Tags.ALTER:
                            if (!StringUtils.isEmpty(text)) {
                                pitch.setAlter(Integer.parseInt(text));
                            }
                            break;
                        case Tags.OCTAVE:
                            if (!StringUtils.isEmpty(text)) {
                                pitch.setOctave(Integer.parseInt(text));
                            }
                            break;
                    }
                    break;
                default:
            }
        }
        return pitch;
    }

    private Attribute readAttribute(XmlPullParser parser) throws XmlPullParserException, IOException {
        Attribute attribute = new Attribute();
        String text = null;
        parser.require(XmlPullParser.START_TAG, namespace, Tags.ATTRIBUTES);
        while (!isEndTag(parser.getName(), parser.getEventType(), Tags.ATTRIBUTES)) {
            parser.next();
            switch(parser.getEventType()) {
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.START_TAG:
                    switch(parser.getName()) {
                        case Tags.KEY:
                            attribute.setKey(readKey(parser));
                            break;
                        case Tags.TIME:
                            attribute.setTime(readTime(parser));
                            break;
                        case Tags.CLEF:
                            attribute.setClef(readClef(parser));
                            break;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (Tags.DIVISIONS.equals(parser.getName())) {
                        if (!StringUtils.isEmpty(text)) {
                            attribute.setDivisions(Integer.parseInt(text));
                        }
                    }
                    break;
                default:
            }
        }
        return attribute;
    }

    private Key readKey(XmlPullParser parser) throws XmlPullParserException, IOException {
        Key key = new Key();
        String text = null;
        parser.require(XmlPullParser.START_TAG, namespace, Tags.KEY);
        while (!isEndTag(parser.getName(), parser.getEventType(), Tags.KEY)) {
            parser.next();
            switch(parser.getEventType()) {
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    switch(parser.getName()) {
                        case Tags.FIFTHS:
                            if (!StringUtils.isEmpty(text)) {
                                key.setFifths(Integer.parseInt(text));
                            }
                            break;
                        case Tags.MODE:
                            key.setMode(text);
                            break;
                    }
                    break;
                default:
            }
        }
        key.setGlyphs(translator.getKeyGlyphs(key));
        return key;
    }

    private Time readTime(XmlPullParser parser) throws XmlPullParserException, IOException {
        Time time = new Time();
        String text = null;
        parser.require(XmlPullParser.START_TAG, namespace, Tags.TIME);
        while (!isEndTag(parser.getName(), parser.getEventType(), Tags.TIME)) {
            parser.next();
            switch(parser.getEventType()) {
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    switch(parser.getName()) {
                        case Tags.BEATS:
                            if (!StringUtils.isEmpty(text)) {
                                time.setBeats(Integer.parseInt(text));
                            }
                            break;
                        case Tags.BEAT_TYPE:
                            if (!StringUtils.isEmpty(text)) {
                                time.setBeatType(Integer.parseInt(text));
                            }
                            break;
                    }
                    break;
                default:
            }
        }
        time.setGlyphs(translator.getTimeGlyphs(time));
        return time;
    }

    private Clef readClef(XmlPullParser parser) throws XmlPullParserException, IOException {
        Clef clef = new Clef();
        String text = null;
        parser.require(XmlPullParser.START_TAG, namespace, Tags.CLEF);
        while (!isEndTag(parser.getName(), parser.getEventType(), Tags.CLEF)) {
            parser.next();
            switch(parser.getEventType()) {
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    switch(parser.getName()) {
                        case Tags.SIGN:
                            clef.setSign(text);
                            break;
                        case Tags.LINE:
                            if (!StringUtils.isEmpty(text)) {
                                clef.setLine(Integer.parseInt(text));
                            }
                            break;
                    }
                    break;
                default:
            }
        }
        clef.setGlyphs(translator.getClefGlyphs(clef));
        return clef;
    }

    private Direction readDirection(XmlPullParser parser) throws XmlPullParserException, IOException {
        Direction direction = new Direction();
        parser.require(XmlPullParser.START_TAG, namespace, Tags.DIRECTION);
        skip(parser, Tags.DIRECTION);
        return direction;
    }

    private void skip(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        while (!isEndTag(parser.getName(), parser.getEventType(), tag)) {
            parser.next();
        }
    }

    private boolean isEndTag(String name, int eventType, String endTag) {
        return endTag.equals(name) && eventType == XmlPullParser.END_TAG;
    }
}
