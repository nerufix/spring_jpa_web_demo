package pl.ug.edu.mwitt.jpa.domain;

import lombok.Value;

import java.sql.Timestamp;
import java.util.Set;

@Value
public class MatchValueDTO {

    Match match;
    Double amount;

}
