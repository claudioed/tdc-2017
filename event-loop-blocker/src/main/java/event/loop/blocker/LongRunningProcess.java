package event.loop.blocker;

/**
 * Long Running process
 * Created by claudio on 13/07/17.
 */
public class LongRunningProcess {

    public static void longProcess(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
