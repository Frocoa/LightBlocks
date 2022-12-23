package com.frocoa.lights.model;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

public class LightBlock {
    private final Location location;
    private final List<Long> schedule;
    private final List<BlockClosure> blocks;
    private final String templateName;

    public LightBlock(Location location, List<Long> schedule, List<BlockClosure> blocks, String templateName) {
        this.location = location;
        this.schedule = schedule;
        this.blocks = blocks;
        this.templateName = templateName;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getTemplateName() {
        return templateName;
    }

    public BlockClosure getCurrentClosure(Long current_time) {
        int index = 0;
        long min_delta = 25000L;
        BlockClosure current_closure = blocks.get(0);
        for (Long time: schedule) {
            long delta = current_time - time;
            if (delta >= 0 && delta <= min_delta) {
                current_closure = blocks.get(index);
                min_delta = delta;
            }
            index++;
        }
        return current_closure;
    }

    public Material getCurrentMaterial(Long current_time) {
        return getCurrentClosure(current_time).getMaterial();
    }

    public boolean getCurrentLit(Long current_time) {
        return getCurrentClosure(current_time).getLit();
    }
}
