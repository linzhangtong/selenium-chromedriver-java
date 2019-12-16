package com.iwhalecloud.screenshotauto;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
public class ScreenshotAutoApplication {

    public static void main(String[] args) throws Exception {
        String url = args[0];
        Integer sleepTime = 5 * 1000;
        String targetFile = args[2];
        String targetPath = targetFile + "sim.png";
        System.setProperty("java.awt.headless", "true");
        URL resource = ScreenshotAutoApplication.class.getClassLoader().getResource("");
        String driverPath = args[1];
        ChromeOptions option = new ChromeOptions();
        option.addArguments("disable-infobars");
        option.addArguments("--headless");
        option.addArguments("--dns-prefetch-disable");
        option.addArguments("--no-referrers");
        option.addArguments("--disable-gpu");
        option.addArguments("--disable-audio");
        option.addArguments("--no-sandbox");
        option.addArguments("--ignore-certificate-errors");
        option.addArguments("--allow-insecure-localhost");
        option.addArguments("--window-size=1711,6021");
        System.setProperty("webdriver.chrome.driver", driverPath);
        WebDriver driver = new ChromeDriver(option);
        driver.get(url);
        Thread.sleep(sleepTime);
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, new File(targetPath));
        System.out.println("===>截取网页的图完成...正在同步数据" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        driver.quit();
        Thread.sleep(sleepTime);





        System.out.println("===>开始分割图片...正在同步数据" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday =calendar.getTime();

        String dateStr = df.format(yesterday);
        File file = new File(targetPath);
        BufferedImage buffer = ImageIO.read(file);
        int width = buffer.getWidth();
        int height = buffer.getHeight();
        int chunck = 10;
        int chunckHeight = height/chunck;
        //截图
        try {
            //sourceRegion()指定位置
            //图片中心400*400的区域
                for(int i = 0 ; i< chunck; i++){
                    Thumbnails.of(targetPath)
                            .sourceRegion(0,chunckHeight*i,width,chunckHeight)
                            .size(width,chunckHeight)
                            .keepAspectRatio(false)
                            .toFile(targetFile+dateStr+"sim"+i+".png");
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
