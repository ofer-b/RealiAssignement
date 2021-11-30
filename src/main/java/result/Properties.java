package result;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Properties {
    @JsonProperty()
    public String id;
    @JsonProperty()
    public int price;
    @JsonProperty()
    public String street;
    @JsonProperty()
    public int bedrooms;
    @JsonProperty()
    public int bathrooms;
    @JsonProperty()
    public int sq_ft;
}
