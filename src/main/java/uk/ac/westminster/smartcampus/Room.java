package uk.ac.westminster.smartcampus;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String id;
    private String name;
    private int capacity;
    private List<String> sensorIds = new ArrayList<>();

    public Room() {}

    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public String getId() { return id; }
    public void setID(String id) { this.id = id;}

    public String getName() { return name;}
    public void setName(String name) { this.name = name;}
}
