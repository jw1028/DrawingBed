from selenium import webdriver
import time
from selenium.webdriver.common.keys import Keys

driver = webdriver.Chrome()
url = "http://localhost:8080/DBed/index.html"
driver.get(url)
driver.maximize_window()
time.sleep(3)
driver.find_element_by_xpath("//*[@id='container']/div[3]/button").click()
alert = driver.switch_to.alert
time.sleep(3)
alert.accept()
time.sleep(3)
alert.accept()
time.sleep(3)
