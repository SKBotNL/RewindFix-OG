package nl.skbotnl.rewindfixog

import java.util.*

object Helper {
    private var boatTimeout: MutableMap<UUID, Long> = HashMap()

    fun getBoatTimeout(uuid: UUID): Long {
        val cooldown: Long
        if (!boatTimeout.containsKey(uuid)) {
            boatTimeout[uuid] = System.currentTimeMillis()
        }
        if (System.currentTimeMillis() - boatTimeout[uuid]!! > 150) {
            boatTimeout[uuid] = System.currentTimeMillis()
            cooldown = 0
        } else {
            cooldown = System.currentTimeMillis() - boatTimeout[uuid]!!
        }
        return cooldown
    }
}
