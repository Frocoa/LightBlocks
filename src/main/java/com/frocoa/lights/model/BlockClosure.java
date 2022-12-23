package com.frocoa.lights.model;

import org.bukkit.Material;

public class BlockClosure {
    private final Material material;
    private final boolean lit;

    public  BlockClosure(Material material, boolean lit) {
        this.material = material;
        this.lit = lit;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean getLit() {
        return lit;
    }
}
