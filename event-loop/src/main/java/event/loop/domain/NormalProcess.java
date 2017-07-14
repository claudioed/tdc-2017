package event.loop.domain;

import java.time.LocalDateTime;

/**
 * Normal process
 * Created by claudio on 13/07/17.
 */
public class NormalProcess {

    public static String process(){
        final String date = LocalDateTime.now().toString();
        return String.format("TDC 2017 - Event Loop Non-Blocking at %s", date);
    }

}
