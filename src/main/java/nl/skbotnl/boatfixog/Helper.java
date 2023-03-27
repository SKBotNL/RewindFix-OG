package nl.skbotnl.boatfixog;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Helper {
    public static Map<UUID, Long> boatTimeout = new HashMap<>();

    public static long getBoatTimeout(UUID uuid) {
        long cooldown;
        if (!boatTimeout.containsKey(uuid)) {
            boatTimeout.put(uuid, System.currentTimeMillis());
        }

        if (System.currentTimeMillis() - boatTimeout.get(uuid) > 250) {
            boatTimeout.put(uuid, System.currentTimeMillis());
            cooldown = 0;
        }
        else {
            cooldown = System.currentTimeMillis() - boatTimeout.get(uuid);
        }
        return cooldown;
    }
}
