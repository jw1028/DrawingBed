from selenium import webdriver
import unittest
from ddt import ddt,file_data
import os
import time

@ddt
class TestCase1(unittest.TestCase):

    def setUp(self):
        self.driver = webdriver.Chrome()
        self.url = "http://localhost:8080/DBed/index.html"
        self.driver.maximize_window()
        time.sleep(2)


    def tearDown(self):
        self.driver.quit()

    @file_data('./data/test_upload_data.json')
    def test_upload(self, value):
            driver = self.driver
            driver.get(self.url)
            self.driver.find_element_by_xpath("//*[@id='blog-collapse']/form/div[1]/input").send_keys(value)
            time.sleep(2)
            self.driver.find_element_by_xpath("//*[@id='blog-collapse']/form/div[2]/input").click()
            time.sleep(2)
            try:
                print(driver.title)
                self.assertEqual(u"个人图库", driver.title)
            except:
                self.saveScreenShot(driver, "登录.png")
    def saveScreenShot(self, driver, file_name):
        if not os.path.exists("./errorImage"):
            os.mkdir("./errorImage")
        now = time.strftime("%Y%m%d-%H%M%S", time.localtime(time.time()))
        driver.get_screenshot_as_file("./errorImage/"+now+"-"+file_name)
        time.sleep(3)

if __name__ == "__main__":
    unittest.main(verbosity=0)
