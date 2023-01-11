package pl.ug.edu.mwitt.jpa.domain;

public enum MatchType {
    Elimination("Elimination"),
    Semifinal("Semi-final"),
    Final("Final");

    String translation;

    private MatchType(String translation) {
        this.translation = translation;
    }

    @Override
    public String toString() {
        return translation;
    }
}