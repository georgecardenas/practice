package com.jcodder.pr4ctice.smufl;

import com.jcodder.pr4ctice.model.Attribute;
import com.jcodder.pr4ctice.model.Clef;
import com.jcodder.pr4ctice.model.Key;
import com.jcodder.pr4ctice.model.Measure;
import com.jcodder.pr4ctice.model.Note;
import com.jcodder.pr4ctice.model.Part;
import com.jcodder.pr4ctice.model.Time;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SmuflTranslator {
    private HashMap<String, String> mapper;

    public SmuflTranslator() {
        mapper = new HashMap<>();

        mapper.put("clefG2","\uE050");

        mapper.put("timenumerator","\uE09E");
        mapper.put("timedenominator","\uE09F");
        mapper.put("time0","\uE080");
        mapper.put("time1","\uE081");
        mapper.put("time2","\uE082");
        mapper.put("time3","\uE083");
        mapper.put("time4","\uE084");
        mapper.put("time5","\uE085");
        mapper.put("time6","\uE086");
        mapper.put("time7","\uE087");
        mapper.put("time8","\uE088");
        mapper.put("time9","\uE089");

        mapper.put("C5","\uEB90");
        mapper.put("D5","\uEB91");
        mapper.put("E5","\uEB92");
        mapper.put("F5","\uEB93");
        mapper.put("G5","\uEB94");
        mapper.put("A5","\uEB95");
        mapper.put("B5","\uEB96");
        mapper.put("C6","\uEB97");
        mapper.put("B4","");
        mapper.put("A4","\uEB98");
        mapper.put("G4","\uEB99");
        mapper.put("F4","\uEB9A");
        mapper.put("E4","\uEB9B");
        mapper.put("D4","\uEB9C");
        mapper.put("C4","\uEB9D");
        mapper.put("B3","\uEB9E");
        mapper.put("A3","\uEB9F");

        mapper.put("n1024thup","\uE1E5");
        mapper.put("n512thup","\uE1E3");
        mapper.put("n256thup","\uE1E1");
        mapper.put("n128thup","\uE1DF");
        mapper.put("n64thup","\uE1DD");
        mapper.put("n32ndup","\uE1DB");
        mapper.put("n16thup","\uE1D9");
        mapper.put("neighthup","\uE1D7");
        mapper.put("nquarterup","\uE1D5");
        mapper.put("nhalfup","\uE1D3");
        mapper.put("nwholeup","\uE1D2");
        mapper.put("nbreveup","\uE1D0");

        mapper.put("n1024thdown","\uE1E6");
        mapper.put("n512thdown","\uE1E4");
        mapper.put("n256thdown","\uE1E2");
        mapper.put("n128thdown","\uE1D0");
        mapper.put("n64thdown","\uE1DE");
        mapper.put("n32nddown","\uE1DC");
        mapper.put("n16thdown","\uE1DA");
        mapper.put("neighthdown","\uE1D8");
        mapper.put("nquarterdown","\uE1D6");
        mapper.put("nhalfdown","\uE1D4");
        mapper.put("nwholedown","\uE1D2");
        mapper.put("nbrevedown","\uE1D0");

        mapper.put("r1024th","\uE4ED");
        mapper.put("r512th","\uE4EC");
        mapper.put("r256th","\uE4EB");
        mapper.put("r128th","\uE4EA");
        mapper.put("r64th","\uE4E9");
        mapper.put("r32nd","\uE4E8");
        mapper.put("r16th","\uE4E7");
        mapper.put("reighth","\uE4E6");
        mapper.put("rquarter","\uE4E5");
        mapper.put("rhalf","\uE4E4");
        mapper.put("rwhole","\uE4E3");
        mapper.put("rbreve","\uE4E2");

        mapper.put("dot","\uE1E7");

        mapper.put("staff", "\uE01A");
        mapper.put("barlinesingle", "\uE030");
        mapper.put("accidentalsharp", "\uE262");
        mapper.put("accidentalflat", "\uE261");
    }

    public List<String> write(Part part) {
        List<String> lines = new ArrayList<>();

        StringBuilder sb = new StringBuilder("");
        int offset = 0;
        int measureCount = 0;
        for (Measure measure : part.getMeasures()) {
            for (Attribute attribute : measure.getAttributes()) {
                if (attribute.getClef() != null) {
                    sb.append(mapper.get("staff"));
                    sb.append(mapper.get("clef" + attribute.getClef().getSign() + attribute.getClef().getLine()));
                    sb.append(mapper.get("staff"));
                    sb.append("=");
                    offset++;
                }
                if (attribute.getTime() != null) {
                    sb.append(mapper.get("staff"));
                    sb.append(writeTime(attribute.getTime().getBeats(), "timenumerator"));
                    sb.append(writeTime(attribute.getTime().getBeatType(), "timedenominator"));
                    sb.append(mapper.get("staff"));
                    sb.append("=");
                    offset++;
                }
            }
            for (Note note : measure.getNotes()) {
                if (note.isRest()) {
                    sb.append(mapper.get("staff"));
                    sb.append(mapper.get("r" + note.getType()));
                    if (note.isDotted()) {
                        if ("quarter".equals(note.getType())){
                            sb.append(mapper.get("C5"));
                        }
                        sb.append(mapper.get("dot"));
                    }
                } else {
                    sb.append(mapper.get("staff"));
                    sb.append(mapper.get("" + note.getPitch().getStep() + note.getPitch().getOctave()));

                    if (note.getBeam() == null){
                        sb.append(mapper.get("n" + note.getType() + getNoteVerticalDirection(note.getPitch().getOctave(), note.getPitch().getStep())));
                    } else {
                        sb.append(mapper.get("n" + "quarter" + getNoteVerticalDirection(note.getPitch().getOctave(), note.getPitch().getStep())));
                    }

                    if (!"B4".equals("" + note.getPitch().getStep() + note.getPitch().getOctave())) {
                        sb.append("- ");
                    } else {
                        sb.append(" ");
                    }

                    if (note.isDotted()) {
                        sb.append(mapper.get("" + note.getPitch().getStep() + note.getPitch().getOctave()));
                        sb.append(mapper.get("dot"));
                    }
                }
                sb.append(mapper.get("staff"));
                sb.append("=");
                offset++;
            }
            sb.append(mapper.get("barlinesingle"));
            sb.append(mapper.get("staff"));
            sb.append("=");
            measureCount++;

            if (measureCount == 2) {
                lines.add(sb.toString());
                measureCount = 0;
                sb = new StringBuilder();
            }
        }

        return lines;
    }

    public String getNoteGlyphs(Note note) {
        StringBuilder sb = new StringBuilder("");

        if (note.isRest()) {
            sb.append(mapper.get("r" + note.getType()));
            if (note.isDotted()) {
                if ("quarter".equals(note.getType())){
                    sb.append(mapper.get("C5"));
                }
                sb.append(mapper.get("dot"));
            }
        } else {
            sb.append(mapper.get("" + note.getPitch().getStep() + note.getPitch().getOctave()));

            if (note.getBeam() == null){
                sb.append(mapper.get("n" + note.getType() + getNoteVerticalDirection(note.getPitch().getOctave(), note.getPitch().getStep())));
            } else {
                sb.append(mapper.get("n" + "quarter" + getNoteVerticalDirection(note.getPitch().getOctave(), note.getPitch().getStep())));
            }

            if (!"B4".equals("" + note.getPitch().getStep() + note.getPitch().getOctave())) {
                sb.append("- ");
            } else {
                sb.append(" ");
            }

            if (note.isDotted()) {
                sb.append(mapper.get("" + note.getPitch().getStep() + note.getPitch().getOctave()));
                sb.append(mapper.get("dot"));
            }
        }

        return sb.toString();
    }

    public String getClefGlyphs(Clef clef) {
        StringBuilder sb = new StringBuilder("");

        sb.append(mapper.get("clef" + clef.getSign() + clef.getLine()));

        return sb.toString();
    }

    public String getTimeGlyphs(Time time) {
        StringBuilder sb = new StringBuilder("");

        sb.append(writeTime(time.getBeats(), "timenumerator"));
        sb.append(writeTime(time.getBeatType(), "timedenominator"));

        return sb.toString();
    }

    public String getKeyGlyphs(Key key) {
        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i < key.getFifths(); i++) {
            if ("major".equals(key.getMode())) {
                switch(i) {
                    case 0:
                        sb.append(mapper.get("F5"));
                        break;
                    case 1:
                        sb.append(mapper.get("C5"));
                        break;
                    case 2:
                        sb.append(mapper.get("G5"));
                        break;
                    case 3:
                        sb.append(mapper.get("D5"));
                        break;
                    case 4:
                        sb.append(mapper.get("A4"));
                        break;
                    case 5:
                        sb.append(mapper.get("E5"));
                        break;
                    case 6:
                        sb.append(mapper.get("B5"));
                        break;
                }
                sb.append(mapper.get("accidentalsharp"));
                sb.append("-");
            } else {
                sb.append(mapper.get("accidentalflat"));
            }
        }

        return sb.toString();
    }

    private String writeTime(int time, String modifier) {
        String timeString = String.valueOf(time);
        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i < timeString.length(); i++) {
            sb.append(mapper.get(modifier));
            sb.append(mapper.get("time" + timeString.charAt(i)));
        }

        return sb.toString();
    }

    private String getNoteVerticalDirection(int octave, String step) {
        if (octave >= 5 || (octave == 4 && "B".equals(step))) {
            return "down";
        }
        return "up";
    }
}
