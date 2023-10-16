package boomerang.server;

public class Card {
    private String name;
    private int number;
    private String site;
    private String region;
    private String collection;
    private String animal;
    private String activity;

    private Card(Builder builder) {
        this.name = builder.name;
        this.number = builder.number;
        this.site = builder.site;
        this.region = builder.region;
        this.collection = builder.collection;
        this.animal = builder.animal;
        this.activity = builder.activity;
    }

    public static class Builder {
        private String name;
        private int number;
        private String site;
        private String region;
        private String collection;
        private String animal;
        private String activity;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder number(int number) {
            this.number = number;
            return this;
        }

        public Builder site(String site) {
            this.site = site;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public Builder collection(String collection) {
            this.collection = collection;
            return this;
        }

        public Builder animal(String animal) {
            this.animal = animal;
            return this;
        }

        public Builder activity(String activity) {
            this.activity = activity;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getSite() {
        return site;
    }

    public String getRegion() {
        return region;
    }

    public String getCollection() {
        return collection;
    }

    public String getAnimal() {
        return animal;
    }

    public String getActivity() {
        return activity;
    }
}
