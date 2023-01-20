package pl.ug.edu.mwitt.jpa.domain;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Arrays;

public enum MatchType {
    Elimination("Elimination"),
    Semifinal("Semi-final"),
    Final("Final");

    final String translation;

    public String getTranslation() {
        return this.translation;
    }

    private static final ImmutableMap<String, MatchType> reverseLookup =
            Maps.uniqueIndex(Arrays.asList(MatchType.values()), MatchType::getTranslation);

    public static MatchType fromString(final String id) {
        return reverseLookup.getOrDefault(id, Elimination);
    }

    MatchType(String translation) {
        this.translation = translation;
    }

//    @Override
//    public String toString() {
//        return translation;
//    }
}