import org.openqa.selenium.chrome.ChromeDriver;
import javax.swing.*;

public class Main extends JFrame { //972556685096
    public Main() {
        this.setVisible(true);
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);

        MainContent mainContent = new MainContent();
        this.add(mainContent);

        this.repaint();
    }

    public static void main(String[] args) {
        new Main();



    }
}