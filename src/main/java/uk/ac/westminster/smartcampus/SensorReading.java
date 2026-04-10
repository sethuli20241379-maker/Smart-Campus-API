package uk.ac.westminster.smartcampus;

import java.time.LocalDateTime;

public class SensorReading {
    private double value;
    private String timestamp;

    public SensorReading() {
        this.timestamp = LocalDateTime.now().toString();
    }

    public SensorReading(double value) {
        this();
        this.value = value;
    }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
