package pl.ug.edu.mwitt.jpa.domain;

public enum PersonType {
    Player("Player"),
    Guest("Guest"),
    Admin("Administrator");

    String translation;

    private PersonType(String translation) {
        this.translation = translation;
    }

    @Override
    public String toString() {
        return translation;
    }
}
