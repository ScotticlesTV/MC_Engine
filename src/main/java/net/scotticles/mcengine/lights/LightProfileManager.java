package net.scotticles.mcengine.lights;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LightProfileManager {

    // List of All Light Profile Names
    public static ArrayList<String> lpNames = new ArrayList<>();

    /**
     * Get all Light Profile file names from MC Engine's lights folder on the server.
     * @param server, the server to get the Light Profile file names from.
     */
    public static void getLPNames(MinecraftServer server) {
        Path path = server.getSavePath(WorldSavePath.ROOT).resolve("mcengine/lights/");

        // try-with-resources ensures the system file handles are closed properly
        try (Stream<Path> stream = Files.list(path)) {
            List<String> fileNames = stream
                    .filter(Files::isRegularFile) // Exclude folders
                    .map(p -> p.getFileName().toString().replace(".json", ""))
                    .toList(); // Use .collect(Collectors.toList()) if on Java 16 or older

            lpNames.clear();
            lpNames.addAll(fileNames);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
