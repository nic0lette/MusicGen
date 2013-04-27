
package co.cutely.musicgen;

import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import co.cutely.musicgen.Synth.Tone;

public class StringMusic {
    private static final int HOLD_WEIGHT = 6;

    public StringMusic(final String string) {
        // First, let's filter the string to only the values we want
        final StringBuilder sb = new StringBuilder();
        for (final char c : string.toLowerCase().toCharArray()) {
            if (Character.isLetter(c)) {
                sb.append(c);
            }
        }
        _clean = sb.toString();

        // Create our synth
        _synth = new Synth();
        for (final char c : _clean.toCharArray()) {
            if (RULES.containsKey(c)) {
                RULES.get(c).apply(_synth);
            }
        }

        int i = 0;
        int sum = 0;
        char c;
        final int strLen = _clean.length();
        for (final Tone t : _synth.getSequence()) {
            c = this._clean.charAt(i % strLen);
            while (!WEIGHTS.containsKey(c)) {
                c = this._clean.charAt((++i) % strLen);
            }

            sum += WEIGHTS.get(c);
            if (sum > HOLD_WEIGHT) {
                t.setLength(2);
                sum = sum % HOLD_WEIGHT;
            }
            ++i;
        }

        for (final Tone t : _synth.getSequence()) {
            playTone(t);
        }
    }

    private void playTone(final Tone t) {
        byte[] buf;
        AudioFormat af = new AudioFormat((float) 44100, 16, 1, true, false);
        SourceDataLine sdl;

        try {
            final int samples = (int) (t.length() * (float) 44100 / 1000);
            final int in = (int) (10 * (float) 44100 / 1000);
            double vol = 0;

            buf = new byte[samples * 2];

            sdl = AudioSystem.getSourceDataLine(af);
            sdl.open(af);
            sdl.start();
            for (int i = 0; i < samples; i += 2) {
                if (i < in) {
                    vol = ((double) i) / in;
                } else if (i + in > samples) {
                    vol = ((double) samples - i) / in;
                } else {
                    vol = 1;
                }

                double angle = i / ((float) 44100 / t.freq()) * Math.PI;
                short value = (short) (Math.sin(angle) * vol * Short.MAX_VALUE);
                buf[i] = (byte) (value & 0x00ff);
                buf[i + 1] = (byte) ((value >> 8) & 0x00ff);
            }
            sdl.write(buf, 0, buf.length);
            sdl.drain();
            sdl.stop();
            sdl.close();
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Synth _synth;
    private String _clean;

    private static final HashMap<Character, Synth.Rule> RULES = new HashMap<Character, Synth.Rule>();
    static {
        RULES.put('e', new Synth.SoftDisplaceRule(1));
        RULES.put('t', new Synth.SoftDisplaceRule(-1));
        RULES.put('a', new Synth.RuleSet(new Synth.SoftDisplaceRule(2), new Synth.SoftDisplaceRule(-1), new Synth.ResetToBaseRule()));
        RULES.put('o', new Synth.RuleSet(new Synth.SoftDisplaceRule(-1), new Synth.SoftDisplaceRule(2), new Synth.ResetToBaseRule()));
        RULES.put('i', new Synth.RuleSet(new Synth.SoftDisplaceRule(2), new Synth.SoftDisplaceRule(-1), new Synth.SoftDisplaceRule(2),
                new Synth.ResetToBaseRule()));
        RULES.put('n', new Synth.RuleSet(new Synth.SoftDisplaceRule(1), new Synth.SoftDisplaceRule(2), new Synth.SoftDisplaceRule(-1),
                new Synth.ResetToBaseRule()));
        RULES.put('s', new Synth.RuleSet(new Synth.SoftDisplaceRule(1), new Synth.SoftDisplaceRule(1)));
        RULES.put('h', new Synth.RuleSet(new Synth.SoftDisplaceRule(1), new Synth.SoftDisplaceRule(-2)));
        RULES.put('r', new Synth.RuleSet(new Synth.SoftDisplaceRule(2), new Synth.SoftDisplaceRule(-1)));
        RULES.put('d', new Synth.RuleSet(new Synth.SoftDisplaceRule(-1), new Synth.SoftDisplaceRule(-1)));
        RULES.put('l', new Synth.RuleSet(new Synth.SoftDisplaceRule(-2), new Synth.SoftDisplaceRule(1)));
        RULES.put('c', new Synth.RuleSet(new Synth.SoftDisplaceRule(0), new Synth.SoftDisplaceRule(0), new Synth.SoftDisplaceRule(1),
                new Synth.ResetToBaseRule()));
        RULES.put('u', new Synth.RuleSet(new Synth.SoftDisplaceRule(0), new Synth.SoftDisplaceRule(0), new Synth.SoftDisplaceRule(-1),
                new Synth.ResetToBaseRule()));
        RULES.put('m', new Synth.RuleSet(new Synth.SoftDisplaceRule(0), new Synth.SoftDisplaceRule(0), new Synth.SoftDisplaceRule(2),
                new Synth.ResetToBaseRule()));
        RULES.put('w', new Synth.RuleSet(new Synth.SoftDisplaceRule(0), new Synth.SoftDisplaceRule(0), new Synth.SoftDisplaceRule(-2),
                new Synth.ResetToBaseRule()));
        RULES.put('f', new Synth.RuleSet(new Synth.SoftDisplaceRule(1), new Synth.SoftDisplaceRule(1), new Synth.SoftDisplaceRule(1),
                new Synth.ResetToBaseRule()));
        RULES.put('g', new Synth.SetRule(new Tone(Note.C, 4)));
        RULES.put('y', new Synth.SetRule(new Tone(Note.D, 4)));
        RULES.put('p', new Synth.SetRule(new Tone(Note.F, 4)));
        RULES.put('b', new Synth.SetRule(new Tone(Note.G, 4)));
        RULES.put('v', new Synth.SetRule(new Tone(Note.A, 4)));
        RULES.put('k', new Synth.HardSetRule(new Tone(Note.C, 5)));
        RULES.put('j', new Synth.HardSetRule(new Tone(Note.D, 5)));
        RULES.put('x', new Synth.HardSetRule(new Tone(Note.F, 5)));
        RULES.put('q', new Synth.HardSetRule(new Tone(Note.G, 5)));
        RULES.put('z', new Synth.HardSetRule(new Tone(Note.A, 5)));
    }

    private static final HashMap<Character, Integer> WEIGHTS = new HashMap<Character, Integer>();
    static {
        WEIGHTS.put('e', 1);
        WEIGHTS.put('t', 1);
        WEIGHTS.put('a', 1);
        WEIGHTS.put('o', 1);
        WEIGHTS.put('i', 1);
        WEIGHTS.put('n', 1);
        WEIGHTS.put('s', 1);
        WEIGHTS.put('h', 4);
        WEIGHTS.put('r', 1);
        WEIGHTS.put('d', 2);
        WEIGHTS.put('l', 1);
        WEIGHTS.put('c', 3);
        WEIGHTS.put('u', 1);
        WEIGHTS.put('m', 3);
        WEIGHTS.put('w', 4);
        WEIGHTS.put('f', 4);
        WEIGHTS.put('g', 2);
        WEIGHTS.put('y', 4);
        WEIGHTS.put('p', 3);
        WEIGHTS.put('b', 3);
        WEIGHTS.put('v', 4);
        WEIGHTS.put('k', 5);
        WEIGHTS.put('j', 8);
        WEIGHTS.put('x', 8);
        WEIGHTS.put('q', 10);
        WEIGHTS.put('z', 10);
    }
}
