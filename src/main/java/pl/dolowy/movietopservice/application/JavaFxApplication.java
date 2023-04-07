package pl.dolowy.movietopservice.application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import pl.dolowy.movietopservice.MovieTopServiceApplication;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        this.applicationContext = new SpringApplicationBuilder()
                .sources(MovieTopServiceApplication.class)
                .run(getParameters()
                        .getRaw()
                        .toArray(new String[0]));
    }

    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }

}
