import unittest
from drawingbed import delete
from drawingbed import upload

import os,sys
import HTMLTestRunner
import time


def TestSuite():
    suite = unittest.TestSuite()
    suite.addTest(unittest.makeSuite(upload.TestCase1))
    suite.addTest(unittest.makeSuite(delete.TestCase2))
    return suite

if __name__ == '__main__':
        curpath = sys.path[0]
        print(curpath)
        if not os.path.exists(curpath + "/resultReport"):
            os.makedirs(curpath + "/resultReport")
        now = time.strftime("%Y-%m-%d-%H %M %S", time.localtime(time.time()))
        filename = curpath + "/resultReport/" + now + "resultReport.html"
        with open(filename, 'wb') as fp:
            runner = HTMLTestRunner.HTMLTestRunner(stream=fp, title=u"测试报告", description=u"用例执行情况", verbosity=2)
            suite = TestSuite()
            runner.run(suite)

