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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class MusicXMLParser {
    private static final String namespace = null;

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
        parser.require(XmlPullParser.START_TAG, namespace, "score-partwise");
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            switch(parser.getEventType()) {
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("part")) {
                        part = readPart(parser);
                    }
                    break;
                default:
            }
        }
        return part;
    }

    private Part readPart(XmlPullParser parser) throws XmlPullParserException, IOException {
        Part part = new Part();
        parser.require(XmlPullParser.START_TAG, namespace, "part");
        while (!isEndTag(parser.getName(), parser.getEventType(), "part")) {
            parser.next();
            switch(parser.getEventType()) {
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("measure")) {
                        part.getMeasures().add(readMeasure(parser));
                    }
                    break;
                default:
            }
        }
        return part;
    }

    private Measure readMeasure(XmlPullParser parser) throws XmlPullParserException, IOException {
        Measure measure = new Measure();
        parser.require(XmlPullParser.START_TAG, namespace, "measure");
        while (!isEndTag(parser.getName(), parser.getEventType(), "measure")) {
            parser.next();
            switch(parser.getEventType()) {
                case XmlPullParser.START_TAG:
                    switch(parser.getName()) {
                        case "attributes":
                            measure.getAttributes().add(readAttribute(parser));
                            break;
                        case "note":
                            measure.getNotes().add(readNote(parser));
                            break;
                        case "direction":
                            measure.getDirections().add(readDirection(parser));
                            break;
                    }
                    break;
                default:
            }
        }
        return measure;
    }

    private Note readNote(XmlPullParser parser) throws XmlPullParserException, IOException {
        Note note = new Note();
        String text = null;
        parser.require(XmlPullParser.START_TAG, namespace, "note");
        while (!isEndTag(parser.getName(), parser.getEventType(), "note")) {
            parser.next();
            switch(parser.getEventType()) {
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.START_TAG:
                    switch(parser.getName()) {
                        case "pitch":
                            note.setPitch(readPitch(parser));
                            break;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    switch(parser.getName()) {
                        case "type":
                            note.setType(text);
                            break;
                        case "dot":
                            note.setDotted(true);
                            break;
                        case "beam":
                            note.setBeam(text);
                            break;
                        case "rest":
                            note.setRest(true);
                            break;
                    }
                    break;
                default:
            }
        }
        return note;
    }

    private Pitch readPitch(XmlPullParser parser) throws XmlPullParserException, IOException {
        Pitch pitch = new Pitch();
        String text = null;
        parser.require(XmlPullParser.START_TAG, namespace, "pitch");
        while (!isEndTag(parser.getName(), parser.getEventType(), "pitch")) {
            parser.next();
            switch(parser.getEventType()) {
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    switch(parser.getName()) {
                        case "step":
                            pitch.setStep(text);
                            break;
                        case "alter":
                            if (!StringUtils.isEmpty(text)) {
                                pitch.setAlter(Integer.parseInt(text));
                            }
                            break;
                        case "octave":
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
        parser.require(XmlPullParser.START_TAG, namespace, "attributes");
        while (!isEndTag(parser.getName(), parser.getEventType(), "attributes")) {
            parser.next();
            switch(parser.getEventType()) {
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.START_TAG:
                    switch(parser.getName()) {
                        case "key":
                            attribute.setKey(readKey(parser));
                            break;
                        case "time":
                            attribute.setTime(readTime(parser));
                            break;
                        case "clef":
                            attribute.setClef(readClef(parser));
                            break;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    switch(parser.getName()) {
                        case "divisions":
                            if (!StringUtils.isEmpty(text)) {
                                attribute.setDivisions(Integer.parseInt(text));
                            }
                            break;
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
        parser.require(XmlPullParser.START_TAG, namespace, "key");
        while (!isEndTag(parser.getName(), parser.getEventType(), "key")) {
            parser.next();
            switch(parser.getEventType()) {
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    switch(parser.getName()) {
                        case "fifths":
                            if (!StringUtils.isEmpty(text)) {
                                key.setFifths(Integer.parseInt(text));
                            }
                            break;
                        case "mode":
                            key.setMode(text);
                            break;
                    }
                    break;
                default:
            }
        }
        return key;
    }

    private Time readTime(XmlPullParser parser) throws XmlPullParserException, IOException {
        Time time = new Time();
        String text = null;
        parser.require(XmlPullParser.START_TAG, namespace, "time");
        while (!isEndTag(parser.getName(), parser.getEventType(), "time")) {
            parser.next();
            switch(parser.getEventType()) {
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    switch(parser.getName()) {
                        case "beats":
                            if (!StringUtils.isEmpty(text)) {
                                time.setBeats(Integer.parseInt(text));
                            }
                            break;
                        case "beat-type":
                            if (!StringUtils.isEmpty(text)) {
                                time.setBeatType(Integer.parseInt(text));
                            }
                            break;
                    }
                    break;
                default:
            }
        }
        return time;
    }

    private Clef readClef(XmlPullParser parser) throws XmlPullParserException, IOException {
        Clef clef = new Clef();
        String text = null;
        parser.require(XmlPullParser.START_TAG, namespace, "clef");
        while (!isEndTag(parser.getName(), parser.getEventType(), "clef")) {
            parser.next();
            switch(parser.getEventType()) {
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    switch(parser.getName()) {
                        case "sign":
                            clef.setSign(text);
                            break;
                        case "line":
                            if (!StringUtils.isEmpty(text)) {
                                clef.setLine(Integer.parseInt(text));
                            }
                            break;
                    }
                    break;
                default:
            }
        }
        return clef;
    }

    private Direction readDirection(XmlPullParser parser) throws XmlPullParserException, IOException {
        Direction direction = new Direction();
        parser.require(XmlPullParser.START_TAG, namespace, "direction");
        skip(parser, "direction");
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
