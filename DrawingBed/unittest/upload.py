from selenium import webdriver
import time


driver = webdriver.Chrome()
url = "http://localhost:8080/DBed/index.html"
driver.get(url)
driver.maximize_window()
time.sleep(3)
driver.find_element_by_xpath("//*[@id='blog-collapse']/form/div[1]/input").send_keys("C:\\Users\\86131\\Desktop\\图片\\4.jpg")
time.sleep(3)
driver.find_element_by_xpath("//*[@id='blog-collapse']/form/div[2]/input").click()
time.sleep(3)

driver.quit()
