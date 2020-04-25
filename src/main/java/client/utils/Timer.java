package client.utils;

public class Timer {
    private long prevMS;
    private static long lastMS;

    public Timer()
    {
        this.prevMS = 0L;
    }

    public boolean delay(double speed)
    {
        return getTime() - this.prevMS >= speed;
    }

    public boolean hasPassed(float milliSec)
    {
        return (float)(getTime() - this.prevMS) >= milliSec;
    }

    public static long getCurrentMS()
    {
        return System.nanoTime() / 1000000L;
    }

    public void reset()
    {
        this.prevMS = getTime();
    }

    public static boolean hasReached(long milliseconds)
    {
        return getCurrentMS() - lastMS >= milliseconds;
    }

    public boolean hasReachedd(double milliseconds)
    {
        return getCurrentMS() - lastMS >= milliseconds;
    }

    public long getTime()
    {
        return System.nanoTime() / 1000000L;
    }

    public long getDifference()
    {
        return getTime() - this.prevMS;
    }

    public void setDifference(long difference)
    {
        this.prevMS = (getTime() - difference);
    }
}
