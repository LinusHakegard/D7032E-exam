package boomerang.server.australia;

import boomerang.server.iCountryActivities;

import java.util.ArrayList;
import java.util.Arrays;

public class AustraliaActivities implements iCountryActivities {
    private final ArrayList<String> australiaActivities = new ArrayList<>(Arrays.asList("Indigenous Culture", "Sightseeing", "Bushwalking", "Swimming", "No activity"));

    @Override
    public ArrayList<String> getActivities() {
        return this.australiaActivities;
    }
}
