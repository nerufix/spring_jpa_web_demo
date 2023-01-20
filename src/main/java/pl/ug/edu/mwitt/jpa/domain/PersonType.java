package pl.ug.edu.mwitt.jpa.domain;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Arrays;

public enum PersonType {
    Player("Player"),
    Guest("Guest"),
    Admin("Administrator");

    final String translation;

    PersonType(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return this.translation;
    }

    private static final ImmutableMap<String, PersonType> reverseLookup =
            Maps.uniqueIndex(Arrays.asList(PersonType.values()), PersonType::getTranslation);

    public static PersonType fromString(final String id) {
        return reverseLookup.getOrDefault(id, Guest);
    }

//    @Override
//    public String toString() {
//        return translation;
//    }

}
