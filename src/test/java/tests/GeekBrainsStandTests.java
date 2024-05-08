package tests;



import org.example.pom.LoginPage;
import org.example.pom.MainPage;
import org.exemple.pom.MainPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.internal.Configuration;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Пример использования самых базовых методов библиотеки Selenium.
 */
public class GeekBrainsStandTests {

    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private MainPage mainPage;

    private static String USERNAME;
    private static String PASSWORD;

    @BeforeAll
    public static void setupClass() {
        Configuration.remote = "htpp://localhost:4444/wd./hub";
        Configuration.browser = "firefox";
        Configuration.browserVersion = "114";


        // Помещаем в переменные окружения путь до драйвера

        System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\geckodriver");
        // mvn clean test -Dgeekbrains_username=USER -Dgeekbrains_password=PASS
        USERNAME = "GB202310790789";
        PASSWORD ="0a5b2e39a5" ;
    }

    @BeforeEach
    public void setupTest() {
        // Создаём экземпляр драйвера
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        // Растягиваем окно браузера на весь экран
        driver.manage().window().maximize();
        // Навигация на https://test-stand.gb.ru/login
        driver.get("https://test-stand.gb.ru/login");
        // Объект созданного Page Object
        loginPage = new LoginPage(driver, wait);
    }
    @Test
    public void testChangeBirthdayOnMainPage() {
        // Логин в систему с помощью метода из класса Page Object
        loginPage.login(USERNAME, PASSWORD);
        // Инициализация объекта класса MainPage
        mainPage = Selenid.page(MainPage.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));

        step("Open profile page", () -> $("[href=\"/profile\"]").click());


        step("Click on Edit", () -> $(By.xpath("//button[contains(@data-testid, 'edit-button')]")).click());

        step("Enter new birthdate", () -> $("[placeholder=\"Date of Birth\"]").val("01/31/1980").pressEnter());

        step("Click on the save button", () -> $("[type=\"submit\"]").click());

        step("Assert birthdate has been changed", () -> assertThat($("[data-testid=\"additional-information\"] div:nth-of-type(2)")
                .find(By.xpath("//div[@data-testid='info-value']"))
                .getText(), equalTo("01/31/1980")));
    }
}


@AfterEach
public void teardown() {
    // Закрываем все окна брайзера и процесс драйвера
    driver.quit();
}

}


