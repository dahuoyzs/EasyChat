//package com.bigfire.easychat.util;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//
//import java.util.Set;
//
///**
// * @ IDE    ：IntelliJ IDEA.
// * @ Author ：dahuo.
// * @ Date   ：2019/8/31  22:10
// * @ Addr   ：China ShangHai
// * @ Email  ：835476090@qq.com
// * @ Desc   :
// */
//public class SElenium {
//    public static void dahuo(String search) throws InterruptedException
//    {
//        // 创建一个火狐浏览器WebDriver的新实例
//        // 注意代码的剩余部分都是依赖于这个接口，而不是这个接口的实现
//        System.setProperty("webdriver.chrome.driver","D:/java/jars/爬虫jar/chromedriver.exe");
//        WebDriver driver = new ChromeDriver();
//
//        // 然后使用刚刚创建的FirefoxWebDriver来访问百度首页
//        driver.get("http://www.baidu.com");
//        String search_handle = driver.getWindowHandle();
//        // 或者我们可以使用下面的代码完成上面相同的功能（后面我们会具体介绍navigate）
//        // driver.navigate().to("http://www.baidu.com");
//
//        // 通过name参数来找到文本输入框的元素位置
//        Thread.sleep(500);//打开页面延时
//        WebElement input = driver.findElement(By.id("kw"));
//        input.sendKeys(search);
//        int len = search.length();
//        int ban = len>>1;  //  len/2
//        String banStr = search.substring(ban, search.length());
////        System.out.println(banStr);
//        Thread.sleep(100);
//        for(int a = 0;a<3;a++){
//            for(int i = 0;i<len;i++){
//                input.sendKeys(Keys.BACK_SPACE);
//            }
//            Thread.sleep(200);
//            input.sendKeys(search);
//        }
//        String yzs = "大火yzs";
//        for(int i = 0;i<len;i++){
//            input.sendKeys(Keys.BACK_SPACE);
//        }
//        input.sendKeys(yzs);
//
//        Thread.sleep(300);//输入框输入延时
//        WebElement baidu = driver.findElement(By.id("su"));
//        baidu.click();
//        Thread.sleep(500);//页面点击延时
//        WebElement content_left = driver.findElement(By.id("content_left"));
//        WebElement firstAnswer = content_left.findElement(By.id("1")).findElement(By.tagName("h3")).findElement(By.tagName("a"));
//        firstAnswer.click();
//        Set<String> set = driver.getWindowHandles();
//        for (String string : set) {
//            if (!string.equals(search_handle)) {
//                driver.switchTo().window(string);
//            }
//        }
//
//        Thread.sleep(500);
//        // 检查页面的title
//        System.out.println("Page title is: " + driver.getTitle());
//
//
//        Thread.sleep(5000);
//        driver.quit();
//    }
//}
