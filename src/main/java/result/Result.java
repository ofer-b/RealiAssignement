package result;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Result {
    @JsonProperty()
    public String type;
    @JsonProperty()
    public List<Feature> features = new ArrayList<>();


}
