package com.jobbot.jobapplicationbot.service;

import com.jobbot.jobapplicationbot.model.jobSeeker;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class LinkedInJobApplicationService {

    private static final String LINKEDIN_URL = "https://www.linkedin.com/";

    public void applyForJobsOnLinkedIn(jobSeeker jobSeeker) {
        WebDriverManager.chromedriver().driverVersion("125.0.6422.60").setup();

        ChromeOptions options = new ChromeOptions();
       // options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);

        try {
            loginToLinkedIn(driver, jobSeeker.getLinkedinUsername(), jobSeeker.getLinkedinPassword());
            searchJobs(driver, jobSeeker.getPositionTitle());
            List<WebElement> jobPostings = getJobPostings(driver);
            for (WebElement jobPosting : jobPostings) {
                if (matchJobSkills(driver, jobPosting, jobSeeker.getSkills())) {
                    applyToJob(driver, jobSeeker, jobPosting);
                }
            }
        } finally {
            driver.quit();
        }
    }

    private void loginToLinkedIn(WebDriver driver, String username, String password) {
        driver.get(LINKEDIN_URL);

        WebElement usernameField = driver.findElement(By.id("session_key"));
        WebElement passwordField = driver.findElement(By.id("session_password"));
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
    }

    private void searchJobs(WebDriver driver, String positionTitle) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement jobsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("jobs-tab-icon")));
        jobsTab.click();

        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[contains(@class, 'jobs-search-box__text-input')]")));
        searchBox.sendKeys(positionTitle);
        searchBox.submit();
    }

    private List<WebElement> getJobPostings(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//ul[@class='jobs-search-results__list']/li")));
    }

    private boolean matchJobSkills(WebDriver driver, WebElement jobPosting, List<String> skills) {
        jobPosting.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement jobDescription = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='jobs-description-content__text']")));

        String jobDescriptionText = jobDescription.getText().toLowerCase();
        for (String skill : skills) {
            if (jobDescriptionText.contains(skill.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void applyToJob(WebDriver driver, jobSeeker jobSeeker, WebElement jobPosting) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement applyButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'jobs-apply-button')]")));
        applyButton.click();

        // Handle applying through LinkedIn or redirecting to the company's website
        while (true) {
            try {
                WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Next')]")));
                nextButton.click();
            } catch (Exception e) {
                break;
            }
        }

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Submit')]")));
        submitButton.click();
    }
}
