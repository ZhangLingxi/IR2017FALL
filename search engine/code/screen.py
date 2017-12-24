from selenium import webdriver
with open('../data/PageRank.txt') as url_file:  
        for line in url_file.readlines():  
            url= line.split(' ')[0]
            docID=line.split(' ')[1]  
            driver=webdriver.PhantomJS()
            driver.get(url)
            driver.save_screenshot('../web/static/screens/'+docID+'.png')
            print('saved! '+docID)
print('screen ok')