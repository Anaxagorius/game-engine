package com.gameengine.collision;

import com.gameengine.vehicles.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollisionLog {

    private static final Logger logger = LoggerFactory.getLogger(CollisionLog.class);
    private static final List<String> logs = new ArrayList<>();

    private CollisionLog() {}

    public static void log(Vehicle a, Vehicle b) {
        String msg = String.format("Collision: %s@(%.2f,%.2f,%.2f) <-> %s@(%.2f,%.2f,%.2f)",
                a.getClass().getSimpleName(),
                a.getPosition().x, a.getPosition().y, a.getPosition().z,
                b.getClass().getSimpleName(),
                b.getPosition().x, b.getPosition().y, b.getPosition().z);
        logger.info(msg);
        logs.add(msg);
    }

    public static List<String> getLogs() {
        return Collections.unmodifiableList(logs);
    }

    public static void clear() {
        logs.clear();
    }
}
