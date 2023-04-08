package pl.dolowy.movietopservice.application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import pl.dolowy.movietopservice.MovieTopServiceApplication;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        ApplicationContextInitializer<GenericApplicationContext> initializer =
                context -> {
                    context.registerBean(Application.class, () -> JavaFxApplication.this);
                    context.registerBean(Parameters.class, this::getParameters); // for demonstration, not really needed
                };

        this.applicationContext = new SpringApplicationBuilder()
                .sources(MovieTopServiceApplication.class)
                .initializers(initializer)
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
