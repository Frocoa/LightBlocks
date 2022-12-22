package com.frocoa.lights.sqlite;

import com.frocoa.lights.Lights;
import java.util.logging.Level;

public class Error {
    public static void execute(Lights plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(Lights plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}

