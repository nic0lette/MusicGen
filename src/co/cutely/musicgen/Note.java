
package co.cutely.musicgen;

public enum Note {
    C(16.35), D(18.35), F(21.83), G(24.50), A(27.50);

    private double _freq;

    private Note(final double freq) {
        this._freq = freq;
    }

    public double freq() {
        return this._freq;
    }

    public Note next() {
        final Note[] notes = Note.values();
        return notes[(this.ordinal() + 1) % notes.length];
    }

    public Note prev() {
        final Note[] notes = Note.values();
        final int len = notes.length;
        return notes[(this.ordinal() + len - 1) % len];
    }
}
