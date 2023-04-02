package org.jablonski.damian.data;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "pickers",
        "pickingStartTime",
        "pickingEndTime"
})

public class Store {

    @JsonIgnore
    private Map<String, LocalTime> ourPickers = new LinkedHashMap<>();

    @JsonProperty("pickers")
    private List<String> pickers;
    @JsonProperty("pickingStartTime")
    private LocalTime pickingStartTime;
    @JsonProperty("pickingEndTime")
    private LocalTime pickingEndTime;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new LinkedHashMap<>();

    @JsonProperty("pickers")
    public List<String> getPickers() {
        return pickers;
    }

    @JsonProperty("pickers")
    public void setPickers(List<String> pickers) {
        this.pickers = pickers;
    }

    @JsonProperty("pickingStartTime")
    public LocalTime getPickingStartTime() {
        return pickingStartTime;
    }

    @JsonProperty("pickingStartTime")
    public void setPickingStartTime(LocalTime pickingStartTime) {
        this.pickingStartTime = pickingStartTime;
    }

    @JsonProperty("pickingEndTime")
    public LocalTime getPickingEndTime() {
        return pickingEndTime;
    }

    @JsonProperty("pickingEndTime")
    public void setPickingEndTime(LocalTime pickingEndTime) {
        this.pickingEndTime = pickingEndTime;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
    public Map<String, LocalTime> getOurPickers() {
        return ourPickers;
    }
}
