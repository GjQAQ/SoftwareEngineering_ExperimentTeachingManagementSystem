package team.segroup.etms.scoresys.service.impl;

import lombok.AllArgsConstructor;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public abstract class CheckCronTask implements Delayed {
    public final long duration;
    public final long birth;
    public final int targetId;

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(
            duration + birth - System.currentTimeMillis(),
            TimeUnit.MILLISECONDS
        );
    }

    @Override
    public int compareTo(Delayed o) {
        long dif = getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
        return (int) dif;
    }

    public abstract void check();
}
