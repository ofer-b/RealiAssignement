package result;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Feature {
    @JsonProperty()
    public String type;
    @JsonProperty()
    public Geometry geometry;
    @JsonProperty()
    public Properties properties;
}
