package event.loop.bus.domain;

import java.time.LocalDateTime;

/**
 * Long Running process
 * Created by claudio on 13/07/17.
 */
public class LongRunningProcess {

    public static String longProcess(){
        try {
            Thread.sleep(200);
            return LocalDateTime.now().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
