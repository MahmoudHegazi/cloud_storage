package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.controller.HomeController;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {
	private static String nextUrl = "";

	@LocalServerPort
	private int port;

	private WebDriver driver;


	private SignupPage signupPage;
	private LoginPage loginPage;
	private HomePage homePage;
	private ResultPage resultPage;


	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();


	}

	@BeforeEach
	public void beforeEach() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--test-type");
		// start in incognito
		options.addArguments("--incognito");
		options.addArguments("--disable-popup-bloacking");
		DesiredCapabilities chrome = DesiredCapabilities.chrome();
		chrome.setJavascriptEnabled(true);
		options.setCapability(ChromeOptions.CAPABILITY, options);
		//Create driver object for Chrome
		this.driver = new ChromeDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, 3);
		WebDriverManager.chromedriver().setup();

		if (!nextUrl.equals("")) {
			driver.get(nextUrl);
		}
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
		nextUrl = "http://localhost:" + this.port + "/home";
	}

	/** @// TODO: 10/28/2021 add 5 home Access/ Logins and signup Main Tests
	 * 1- Write a Selenium test that verifies that the home page is not accessible without logging in.
	 * 2- add New user and validated the success message (signup)
	 * 3- add invalid user and make sure no redirect and validate error message (signup)
	 * 4- login success add new user then validate success message then click on redirect link from success message and log with that user (login)
	 * 5- login failed try to log with invalid credentials and check error message
	 * **/

	// Write a Selenium test that verifies that the home page is not accessible without logging in.
	@Test
	public void getHomePageWithoutLogin() {
		if (!driver.getCurrentUrl().contains("home")){
			driver.get("http://localhost:" + this.port + "/home");
		}
		Assertions.assertEquals("Login", driver.getTitle());
		Assertions.assertNotEquals("Home", driver.getTitle());
		nextUrl = "http://localhost:" + this.port + "/signup";
	}


	/* Signup and Login Test Start **/

	// (Create New User)
	@Test
	public void addValidUser() throws InterruptedException {
		if (!driver.getCurrentUrl().contains("signup")){
			driver.get("http://localhost:" + port + "/signup");
		}

		int randomNum = (int)(Math.random() * 990000);
		String userName = "user" + randomNum + "_" + (int)(Math.random() * 990000);
		SignupPage signupPage = new SignupPage(driver);

		signupPage.createUser("Python", "king", userName, "myStrongPass");
		assertEquals("You successfully signed up! Please continue to the login page.",signupPage.getSuccessMessage());
		nextUrl = "http://localhost:" + this.port + "/signup";
	}

	// test add invalid user (duplicated username) add new user then try to add another user with same username
	@Test
	public void addInvalidUser() throws InterruptedException {
		if (!driver.getCurrentUrl().contains("signup")){
			driver.get("http://localhost:" + port + "/signup");
		}

		int randomNum = (int)(Math.random() * 990000);
		String userName = "user" + randomNum + "_" + (int)(Math.random() * 990000);

		SignupPage signupPage = new SignupPage(driver);
		// test add invalid user (first add valid user with username and then check if it accepts same username);

		signupPage.createUser("Python" + randomNum, "king" + randomNum, userName, "apassword");
		//validate the user created
		assertEquals("You successfully signed up! Please continue to the login page.",signupPage.getSuccessMessage());
		Thread.sleep(2000);
		driver.get("http://localhost:" + port + "/signup");
		// add new user with same username and validate the error message displayed
		signupPage.createUser("Python" + randomNum, "king" + randomNum, userName, "strongpasss");
		assertEquals("The username already exists.",signupPage.getErrorMessage());
		nextUrl = "http://localhost:" + this.port + "/login";
	}


	// (Test Login and redirect to home)
	@Test
	public void loginSuccess() throws InterruptedException {
		if (!driver.getCurrentUrl().contains("signup")){
			driver.get("http://localhost:" + port + "/signup");
		}
		//get random username and pass
		int randomNum = (int)(Math.random() * 990000);
		String userName = "user" + randomNum + "_" + (int)(Math.random() * 990000);
		String password = "password_" + randomNum + "_" + (int)(Math.random() * 990000);


		SignupPage signupPage = new SignupPage(driver);

		signupPage.createUser("Python" + randomNum, "king" + randomNum, userName, password);
		//validate the user created
		assertEquals("You successfully signed up! Please continue to the login page.",signupPage.getSuccessMessage());

		// make sure redirect after click login link
		signupPage.goToLoginAfterSuccess();

		LoginPage loginPage = new LoginPage(driver);

		String beforeLogUrl = driver.getCurrentUrl();
		// create new user
		loginPage.logIn(userName, password);
		String afterLogUrl = driver.getCurrentUrl();


		// check url before login and after login
		assertNotEquals(beforeLogUrl, afterLogUrl);

		// make sure after log it redirect to home
		Assertions.assertEquals("Home", driver.getTitle());

	}


	// (Test Login failed and error message and not redirected)
	@Test
	public void loginFailed() throws InterruptedException {
		if (!driver.getCurrentUrl().contains("login")){
			driver.get("http://localhost:" + port + "/login");
		}
		//get random username and pass
		int randomNum = (int)(Math.random() * 990000);
		String userName = "user" + randomNum + "_" + (int)(Math.random() * 990000);
		String password = "password_" + randomNum + "_" + (int)(Math.random() * 990000);
		Thread.sleep(1000);

		LoginPage loginPage = new LoginPage(driver);
		String beforeLogUrl = driver.getCurrentUrl();
		// create new user
		loginPage.logIn(userName, password);
		String afterLogUrl = driver.getCurrentUrl();


		// make sure no redirection happened and the error query parameter added
		assertEquals(beforeLogUrl+"?error", afterLogUrl);

		// check error message
		Assertions.assertEquals("Invalid username or password", loginPage.getErrorMsg());
		nextUrl = "http://localhost:" + this.port + "/signup";

	}

	/* Signup and Login Test End **/




}
