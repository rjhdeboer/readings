package org.example.readings.statistics.domain;

public class FetchReadingStatisticsQuery {

    private String sensorId;
    private String type;
    private String category;
    private long from;
    private long until;

    public FetchReadingStatisticsQuery() {
    }

    public FetchReadingStatisticsQuery(String sensorId, String type, String category, long from, long until) {
        this.sensorId = sensorId;
        this.type = type;
        this.category = category;
        this.from = from;
        this.until = until;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public long getFrom() {
        return from;
    }

    public long getUntil() {
        return until;
    }
}
