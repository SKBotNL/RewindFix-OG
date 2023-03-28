package nl.skbotnl.rewindfixog;

import net.minecraft.world.entity.vehicle.EntityBoat;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.entity.EnumMoveType;
import org.bukkit.util.Vector;

public class MoveBoatRunnable implements Runnable {
    private final EntityBoat boat;
    private final Vector vec;

    public MoveBoatRunnable(EntityBoat boat, Vector vec) {
        this.boat = boat;
        this.vec = vec;
    }

    @Override
    public void run() {
        // move(EnumMoveType arg0, Vec3D arg1)
        boat.a(EnumMoveType.a, new Vec3D(vec.getX(), vec.getY(), vec.getZ()));
    }
}