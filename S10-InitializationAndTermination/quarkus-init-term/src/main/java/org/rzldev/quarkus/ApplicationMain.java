package org.rzldev.quarkus;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class ApplicationMain {

    public static void main(String ...args) {
        Quarkus.run(BookMain.class, args);
    }

    public static class BookMain implements QuarkusApplication {

        @Override
        public int run(String... args) throws Exception {
            System.out.println("BookMain class is running...");
            Quarkus.waitForExit();
            return 0;
        }
    }

}
