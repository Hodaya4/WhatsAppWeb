import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;
import java.util.List;
import javax.swing.*;


public class MainContent extends JPanel {
    private JButton openWW;
    private JTextField phoneNum;
    private JTextField textData;
    private JLabel errorMsg;
    JLabel displayCurrentMessage;

    public MainContent() {
        this.setLayout(null);
        this.displayCurrentMessage = new JLabel("");
        displayCurrentMessage.setBounds(Constants.DISPLAY_MSG_X, Constants.DISPLAY_MSG_Y,
                Constants.DISPLAY_MSG_WIDTH, Constants.DISPLAY_MSG_HEIGHT);
        this.add(displayCurrentMessage);
        displayCurrentMessage.setVisible(false);
        this.setBounds(Constants.ZERO, Constants.ZERO, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setDoubleBuffered(true);

        this.openWW = new JButton("Open WhatsApp Web");
        this.openWW.setBounds(Constants.OPENWW_X, Constants.OPENWW_Y, Constants.OPENWW_WIDTH, Constants.OPENWW_HEIGHT);
        this.add(this.openWW);

        JLabel phoneNumLabel = new JLabel("Phone Number: ");
        phoneNumLabel.setBounds(Constants.PHONE_NUM_X, Constants.LABLES_Y, Constants.PHONE_NUM_WIDTH,
                Constants.PHONE_NUM_HEIGHT);
        this.add(phoneNumLabel);

        JLabel textDataLabel = new JLabel("Text body: ");
        textDataLabel.setBounds(Constants.TEXT_DATA_X, Constants.LABLES_Y, Constants.TEXT_DATA_WIDTH,
                Constants.TEXT_DATA_HEIGHT);
        this.add(textDataLabel);

        this.phoneNum = new JTextField();
        this.phoneNum.setBounds(Constants.PHONE_NUM_X, Constants.PHONE_NUM_Y, Constants.PHONE_NUM_WIDTH,
                Constants.PHONE_NUM_HEIGHT);
        this.add(this.phoneNum);

        this.textData = new JTextField();
        this.textData.setBounds(Constants.TEXT_DATA_X, Constants.PHONE_NUM_Y, Constants.TEXT_DATA_WIDTH,
                Constants.TEXT_DATA_HEIGHT);
        this.add(this.textData);

        this.errorMsg = new JLabel("");
        this.errorMsg.setBounds(Constants.DISPLAY_MSG_X, Constants.ERROR_Y, Constants.DISPLAY_MSG_WIDTH, Constants.DISPLAY_MSG_HEIGHT);

        repaint();
        this.openWW.addActionListener((e) -> {
            this.remove(this.errorMsg);
            String phoneNumber = phoneNum.getText();
            String textBody = textData.getText();
            if (phoneNumber.equals("")) {
                this.errorMsg.setText("No phone number provided");
            } else if ((phoneNumber.charAt(Constants.ZERO) != '9' || phoneNumber.charAt(Constants.ONE)
                    != '7' || phoneNumber.charAt(Constants.TWO) != '2') ||
                    phoneNumber.length() != Constants.PHONE_NUM_LENGTH) {
                this.errorMsg.setText("Phone number invalid");
            } else if (textBody.equals("")) {
                this.errorMsg.setText("No text body provided");
            } else {
                System.setProperty("webdriver.chrome.driver", "D:\\IntelliJ IDEA Community Edition 2021.2.3\\chromedriver.exe");
                ChromeDriver driver = new ChromeDriver();
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constants.SLEEP_1));
                driver.get("https://web.whatsapp.com/");
                loginProcess(driver);
                String link = "https://web.whatsapp.com/send?phone=".concat(phoneNumber);
                driver.get(link);
                WebElement typingBox = driver.findElement(By.xpath("//*[@id=\"main\"]/footer/div[1]/div/span[2]/div/div[2]/div[1]/div/div[2]"));
                typingBox.sendKeys(textBody);
                typingBox.sendKeys(Keys.RETURN);
                this.displayCurrentMessage.setText("message sent successfully");

                List<WebElement> messages = driver.findElements(By.className("_1beEj"));
                WebElement lastMsg = messages.get(messages.size() - Constants.ONE);
                messageStatus(lastMsg);

                stageSix(driver, textBody);

            }
            this.add(this.errorMsg);
            repaint();
        });
    }


    public void stageSix(ChromeDriver driver, String sentMsg) {
        new Thread(() -> {
            try {
                Thread.sleep(Constants.SLEEP_2);
                String msgData = "";
                do {
                    List<WebElement> receivedMsg = driver.findElements(By.className("i0jNr"));
                    WebElement lastMsg = receivedMsg.get(receivedMsg.size() - Constants.ONE);
                    WebElement spanTag = lastMsg.findElement(By.tagName("span"));
                    msgData = spanTag.getText();
                } while (msgData.equals(sentMsg));
                this.displayCurrentMessage.setText(msgData);
                if (this.displayCurrentMessage.getText().equals(msgData)) {
                    driver.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }


    private void messageStatus(WebElement lastMsg) {
        new Thread(() -> {
            String status = null;
            try {
                do {
                    WebElement msgStatus = lastMsg.findElement(By.cssSelector("span[data-testid='msg-dblcheck']"));
                    status = msgStatus.getAttribute("aria-label");
                    if (!status.equals(" Delivered ") && !status.equals(" Read ")) {
                        this.displayCurrentMessage.setText(" Pending ");
                    } else {
                        this.displayCurrentMessage.setText(status);
                    }
                } while (!status.equals(" Read "));
                System.out.println("read");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }


    public void loginProcess(ChromeDriver driver) {
        this.displayCurrentMessage.setText("Logged in successfully");

        while (!this.displayCurrentMessage.isVisible()) {
            try {
                WebElement element = driver.findElement(By.linkText("Get it here"));
                if (element.isDisplayed()) {
                    this.displayCurrentMessage.setVisible(true);
                    this.openWW.setVisible(false);
                }
            } catch (NoSuchElementException exception) {
            }
        }
    }

}