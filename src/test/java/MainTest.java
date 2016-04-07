import edu.kit.trufflehog.Main;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.junit.Test;

/**
 * Testing basic JFX-application functionality.
 */
public class MainTest {

    /**
     * This test checks if the jfx-application starts at all.
     *
     * @throws InterruptedException
     */
    @Test
    public void basicStartTest() throws InterruptedException {
        Thread thread = new Thread(() -> {
            new JFXPanel();
            Platform.runLater(() -> new Main().start(new Stage()));
        });
        thread.start();
        Thread.sleep(10000);
    }

}
