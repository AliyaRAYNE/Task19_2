import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DebitCardApplicationTest {
    WebDriver driver;
    ChromeOptions options;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }
    @AfterEach
    void teardown() {
        driver.quit();
        driver = null;
    }
    @Test
    void SuccessfulApplicationSubmission() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Петрова Юлия");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79663215584");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button_theme_alfa-on-white")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void WrongNameAndSurnameLatin () {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Petrova Yulia");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79663215584");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button_theme_alfa-on-white")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void EmptyFieldLastNameAndFirstName () {
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79663215584");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void  IncorrectPhoneNumberEntry () {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Петрова Юлия");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+796632155");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void EmptyPhoneField () {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Петрова Юлия");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void CheckboxNotChecked () {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys(("Петрова Юлия"));
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys(("+79663215584"));
        driver.findElement(By.cssSelector("button")).click();

        boolean actual = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text")).isDisplayed();
        assertTrue(actual);
    }
}
