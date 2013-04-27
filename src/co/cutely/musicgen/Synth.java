
package co.cutely.musicgen;

import java.util.ArrayList;

public class Synth {
    public static class Tone {
        private static final int DEFAULT_LENGTH = 120;

        private final Note _note;
        private final int _oct;
        private int _length = 1;

        public Tone(final Note note, final int oct) {
            if (oct < 0 || oct > 8) {
                throw new IllegalArgumentException("Octave must be between 0 and 8");
            }
            this._note = note;
            this._oct = oct;
        }

        public int length() {
            return Tone.DEFAULT_LENGTH * this._length;
        }

        public void setLength(final int len) {
            if (len > 0) {
                this._length = len;
            }
        }

        public double freq() {
            return this._note.freq() * Math.pow(2, _oct);
        }

        public Tone next() {
            final int newOctave = (_note == Note.A) ? _oct + 1 : _oct;
            if (newOctave > 8) {
                throw new IllegalArgumentException("Cannot perform next from " + this);
            }
            return new Tone(_note.next(), newOctave);
        }

        public Tone prev() {
            final int newOctave = (_note == Note.C) ? _oct - 1 : _oct;
            if (newOctave < 0) {
                throw new IllegalArgumentException("Cannot perform prev from " + this);
            }
            return new Tone(_note.prev(), newOctave);
        }

        public String toString() {
            return _note.name() + _oct;
        }
    }

    public Synth() {
        this._baseTone = this._softBase = new Tone(Note.C, 4);
    }

    public Synth(final Tone base) {
        this._baseTone = this._softBase = base;
    }

    public ArrayList<Tone> getSequence() {
        return _seq;
    }

    public static abstract class Rule {
        protected abstract void apply(final Synth synth);
    }

    public static class SetRule extends Rule {
        final Tone _tone;

        public SetRule(final Tone newTone) {
            this._tone = newTone;
        }

        @Override
        protected void apply(Synth synth) {
            synth.setSoftBase(this._tone);
            synth.applyTone(this._tone);
        }
    }

    public static class HardSetRule extends SetRule {

        public HardSetRule(Tone newTone) {
            super(newTone);
        }

        @Override
        protected void apply(Synth synth) {
            super.apply(synth);
            synth.setBaseTone(synth.getSoftBase());
        }
    }

    public static class SoftDisplaceRule extends Rule {
        final int _displacement;

        public SoftDisplaceRule(final int displacement) {
            this._displacement = displacement;
        }

        @Override
        protected void apply(Synth synth) {
            Tone t = synth.getSoftBase();
            if (_displacement > 0) {
                for (int i = 0; i < _displacement; ++i) {
                    t = t.next();
                }
            } else {
                final int disp = -1 * _displacement;
                for (int i = 0; i < disp; ++i) {
                    t = t.prev();
                }
            }
            synth.setSoftBase(t);
            synth.applyTone(t);
        }
    }

    public static class HardDisplaceRule extends SoftDisplaceRule {

        public HardDisplaceRule(final int displacement) {
            super(displacement);
        }

        @Override
        protected void apply(Synth synth) {
            super.apply(synth);
            synth.setBaseTone(synth.getSoftBase());
        }
    }

    public static class ResetToBaseRule extends Rule {

        @Override
        protected void apply(Synth synth) {
            synth.setSoftBase(synth.getBaseTone());
        }
    }

    public static class ApplyRule extends Rule {
        public static final int APPLY_SOFT = 0;
        public static final int APPLY_HARD = 1;

        private final int _apply;

        public ApplyRule(final int apply) {
            this._apply = apply;
        }

        @Override
        protected void apply(Synth synth) {
            if (this._apply == APPLY_SOFT) {
                synth.applyTone(synth.getSoftBase());
            } else {
                synth.applyTone(synth.getBaseTone());
            }
        }
    }

    public static class RuleSet extends Rule {
        private final Rule[] _rules;

        public RuleSet(final Rule... rules) {
            this._rules = rules;
        }

        @Override
        protected void apply(Synth synth) {
            for (final Rule rule : this._rules) {
                rule.apply(synth);
            }
        }
    }

    protected Tone getBaseTone() {
        return this._baseTone;
    }

    protected void setBaseTone(final Tone newBaseTone) {
        this._baseTone = newBaseTone;
    }

    protected Tone getSoftBase() {
        return this._softBase;
    }

    protected void setSoftBase(final Tone newBaseTone) {
        this._softBase = newBaseTone;
    }

    protected void applyTone(final Tone tone) {
        _seq.add(tone);
    }

    private Tone _baseTone;
    private Tone _softBase;

    private final ArrayList<Tone> _seq = new ArrayList<Tone>();
}
