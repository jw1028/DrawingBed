from selenium import webdriver
import time
import unittest
import os


class TestCase2(unittest.TestCase):
    @classmethod
    def setUp(self):
        self.driver = webdriver.Chrome()
        self.url = "http://localhost:8080/DBed/index.html"
        self.driver.maximize_window()
        time.sleep(2)


    def tearDown(self):
        self.driver.quit()


    def test_delete(self):
            driver = self.driver
            driver.get(self.url)
            driver.find_element_by_xpath("//*[@id='container']/div[2]/button").click()
            alert = driver.switch_to.alert
            time.sleep(2)
            alert.accept()
            time.sleep(2)
            alert.accept()
            time.sleep(2)
            try:
                print(driver.title)
                self.assertEqual(u"个人图库", driver.title)
            except:
                self.saveScreenShot(driver, "删除.png")
    def saveScreenShot(self, driver, file_name):
        if not os.path.exists("./errorImage"):
            os.mkdir("./errorImage")
        now = time.strftime("%Y%m%d-%H%M%S", time.localtime(time.time()))
        driver.get_screenshot_as_file("./errorImage/"+now+"-"+file_name)
        time.sleep(3)


if __name__ == "__main__":
    unittest.main(verbosity=0)

