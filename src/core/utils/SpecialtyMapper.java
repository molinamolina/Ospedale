package core.utils;

import core.models.entities.Specialty;

public final class SpecialtyMapper {

    private SpecialtyMapper() {
    }

    public static Specialty fromJson(String value) {
        return switch (value) {
            case "ORTHOPEDICS" -> Specialty.TRAUMATOLOGY_ORTHOPEDICS;
            case "GYNECOLOGY" -> Specialty.GYNECOLOGY_OBSTETRICS;
            default -> Specialty.valueOf(value);
        };
    }

    public static Specialty fromDisplayName(String displayName) {
        String normalized = displayName.replace(" &", "").replace(" ", "_").toUpperCase();
        return fromJson(normalized);
    }

    public static String toDisplayName(Specialty specialty) {
        return specialty.toString().replace("_", " & ");
    }
}
