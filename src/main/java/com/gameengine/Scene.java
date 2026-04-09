package com.gameengine;

import com.gameengine.vehicles.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private final List<GameItem> items = new ArrayList<>();
    private final List<Vehicle> vehicles = new ArrayList<>();

    public void addItem(GameItem item) {
        items.add(item);
        if (item instanceof Vehicle v) {
            vehicles.add(v);
        }
    }

    public void removeAllVehicles() {
        items.removeAll(vehicles);
        vehicles.clear();
    }

    public void update(float deltaTime) {
        for (Vehicle vehicle : vehicles) {
            vehicle.update(deltaTime);
        }
        List<Vehicle> copy = new ArrayList<>(vehicles);
        for (int i = 0; i < copy.size(); i++) {
            for (int j = i + 1; j < copy.size(); j++) {
                copy.get(i).collide(copy.get(j));
            }
        }
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<GameItem> getItems() {
        return items;
    }
}
