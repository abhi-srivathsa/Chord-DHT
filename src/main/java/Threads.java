import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

// Management of threads
public class Threads {

    private Threads(){}
    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(10000);

    public static void executeAfterDelay(Runnable runnable){
        pool.schedule(runnable, Helper.getTimer(), TimeUnit.MILLISECONDS);
    }

    public static ScheduledFuture<?> executeRarely(Runnable runnable){
        return pool.scheduleWithFixedDelay(runnable, Helper.getPeriod()*10, Helper.getPeriod()*10, TimeUnit.MILLISECONDS);
    }

    public static ScheduledFuture<?> executePeriodically(Runnable runnable){
        return pool.scheduleWithFixedDelay(runnable, Helper.getPeriod(), Helper.getPeriod(), TimeUnit.MILLISECONDS);
    }


    public static void executeImmediately(Runnable runnable){
        pool.submit(runnable);
    }



}
