package result;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Geometry {
    @JsonProperty()
    public String type;
    @JsonProperty()
    public List<Double> coordinates;
}
