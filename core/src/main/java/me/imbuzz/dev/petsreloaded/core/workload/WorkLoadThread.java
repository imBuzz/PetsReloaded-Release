package me.imbuzz.dev.petsreloaded.core.workload;

import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ConcurrentLinkedDeque;

@RequiredArgsConstructor
public class WorkLoadThread extends BukkitRunnable {

    private final PetsReloaded petsReloaded;
    private static final int MAX_MS_PER_TICK = 10;
    private final ConcurrentLinkedDeque<Workload> workloads = new ConcurrentLinkedDeque<>();

    public void addLoad(Workload workload) {
        workloads.add(workload);
    }

    @Override
    public void run() {
        long stopTime = System.currentTimeMillis() + MAX_MS_PER_TICK;
        while (!workloads.isEmpty() && System.currentTimeMillis() <= stopTime && petsReloaded.getPetsManager().isLoadedPets()) {
            Workload workload = workloads.poll();
            if (workload == null) continue;
            workload.compute();
        }
    }


}
