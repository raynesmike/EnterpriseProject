package webservice;

import io.vertx.core.Launcher;

public class AbstractMain {
    public static void main(final String[] args){
        Launcher.executeCommand("run", MainVerticle.class.getName());
    }
}
