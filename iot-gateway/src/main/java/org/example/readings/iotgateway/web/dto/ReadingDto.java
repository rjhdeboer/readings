package org.example.readings.iotgateway.web.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ReadingDto {

    @NotBlank(message = "Sensor id is mandatory")
    private String sensorId;
    @NotBlank(message = "Type is mandatory")
    private String type;
    @NotBlank(message = "Category is mandatory")
    private String category;
    @NotNull(message = "Value is mandatory")
    private BigDecimal value;
    @Min(value = 0, message = "Taken at value should be zero or higher")
    private long takenAt;

    public ReadingDto() {
    }

    public ReadingDto(String sensorId, String type, String category, BigDecimal value, long takenAt) {
        this.sensorId = sensorId;
        this.type = type;
        this.category = category;
        this.value = value;
        this.takenAt = takenAt;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public long getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(long takenAt) {
        this.takenAt = takenAt;
    }

    @Override
    public String toString() {
        return "ReadingDto{" +
                "type='" + type + '\'' +
                ", category='" + category + '\'' +
                ", value=" + value +
                ", takenAt=" + takenAt +
                '}';
    }
}
