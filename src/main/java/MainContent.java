import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.*;

public class MainContent extends JPanel {
    private JButton openWW;
    private JTextField phoneNum;
    private JTextField textData;
    private JLabel errorMsg;


    public MainContent() {
        this.setBounds(0,0,Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
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
        this.errorMsg.setBounds(Constants.LOGIN_X, Constants.ERROR_Y, Constants.LOGIN_WIDTH, Constants.LOGIN_HEIGHT);

        repaint();
        this.openWW.addActionListener((e) -> {
            this.remove(this.errorMsg);
            String phoneNumber = phoneNum.getText();
            String textBody = textData.getText();
            if (phoneNumber.equals("")) {
                this.errorMsg.setText("No phone number provided");
            } else if ((phoneNumber.charAt(0) != '9' || phoneNumber.charAt(1) != '7' || phoneNumber.charAt(2) != '2') ||
                    phoneNumber.length() != 12) {
                this.errorMsg.setText("Phone number invalid");
            } else if (textBody.equals("")) {
                this.errorMsg.setText("No text body provided");
            } else {
                System.setProperty("webdriver.chrome.driver", "D:\\IntelliJ IDEA Community Edition 2021.2.3\\chromedriver.exe");
                ChromeDriver driver = new ChromeDriver();
                driver.get("https://web.whatsapp.com/");
                loginProcess(driver);
                String link = "https://api.whatsapp.com/send?phone=".concat(phoneNumber);
                driver.get(link);
            }
            this.add(this.errorMsg);
            repaint();
        });
    }


    public void loginProcess(ChromeDriver driver) {
        JLabel login = new JLabel("Logged in successfully");
        login.setBounds(Constants.LOGIN_X, Constants.LOGIN_Y, Constants.LOGIN_WIDTH, Constants.LOGIN_HEIGHT);
        login.setVisible(false);
        this.add(login);

        while (!login.isVisible()) {
            try {
                WebElement element = driver.findElement(By.linkText("Get it here"));
                if (element.isDisplayed()) {
                    login.setVisible(true);
                    this.openWW.setVisible(false);
                }
            } catch (NoSuchElementException exception) {
            }
        }
    }


}
