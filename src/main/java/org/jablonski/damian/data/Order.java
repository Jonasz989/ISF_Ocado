package org.jablonski.damian.data;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "orderId",
        "orderValue",
        "pickingTime",
        "completeBy"
})

public class Order {

    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("orderValue")
    private BigDecimal orderValue;
    @JsonProperty("pickingTime")
    private Duration pickingTime;
    @JsonProperty("completeBy")
    private LocalTime completeBy;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new LinkedHashMap<>();

    @JsonProperty("orderId")
    public String getOrderId() {
        return orderId;
    }

    @JsonProperty("orderId")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @JsonProperty("orderValue")
    public BigDecimal getOrderValue() {
        return orderValue;
    }

    @JsonProperty("orderValue")
    public void setOrderValue(BigDecimal orderValue) {
        this.orderValue = orderValue;
    }

    @JsonProperty("pickingTime")
    public Duration getPickingTime() {
        return pickingTime;
    }

    @JsonProperty("pickingTime")
    public void setPickingTime(Duration pickingTime) {
        this.pickingTime = pickingTime;
    }

    @JsonProperty("completeBy")
    public LocalTime getCompleteBy() {
        return completeBy;
    }

    @JsonProperty("completeBy")
    public void setCompleteBy(LocalTime completeBy) {
        this.completeBy = completeBy;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
