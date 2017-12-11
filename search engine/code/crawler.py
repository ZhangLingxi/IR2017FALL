from bs4 import BeautifulSoup
import urllib.request
import xml.etree.ElementTree as ET

def get_news_pool():
    news_pool = []
    data=[]
    with open('../data/PageRank.txt') as url_file:  
        for line in url_file.readlines():  
            url= line.split(' ')[0]
            docID=line.split(' ')[1]  
            data_info = [url, int(docID)]
            data.append(data_info)
    for i in range(1086):
        page_url=data[i][0]
        page_id=data[i][1]
        try:
            response = urllib.request.urlopen(page_url)
        except Exception as e:
            print("-----%s: %s-----"%(type(e), page_url))
            continue
        html = response.read()
        soup = BeautifulSoup(html,"lxml")
        title=soup.title.string
        date_time='unknown date or author'
        if title==None:
            title='Nankai University'
        e_d_left_fl = soup.find('div', class_ = "e_d_left fl")
        if e_d_left_fl != None:
            h2=e_d_left_fl.find('h2')
            if h2 != None:
                span=h2.find('span')
                if span!=None:
                    date_time=span.get_text()
                    title=h2.get_text().replace(date_time,"").strip()
                else:
                    title=h2.get_text().strip()
        news_info=[page_id,page_url,title,date_time]
        news_pool.append(news_info)
        print(i)
    return(news_pool)

def crawl_news(news_pool, doc_dir_path, doc_encoding):
    for news in news_pool:
        i=news[0]
        name=str(i)+'.xml'
        try:
            response = urllib.request.urlopen(news[1])
        except Exception as e:
            print("-----%s: %s-----"%(type(e), news[1]))
            continue
        html = response.read()
        soup = BeautifulSoup(html,"lxml")
        snippet='See more'
        MsoNormal = soup.find('p', class_ = "MsoNormal")
        if MsoNormal!=None:
            snippet=MsoNormal.get_text().strip()
        else:
            description = soup.find(attrs={"name":"description"})
            if description!=None:
                if description['content']!="":
                    snippet=description['content'].strip()
        if snippet=="":
            snippet='See more'
        doc = ET.Element("doc")
        ET.SubElement(doc, "id").text = str(i)
        ET.SubElement(doc, "url").text = news[1]
        ET.SubElement(doc, "title").text = news[2]
        ET.SubElement(doc, "datetime").text = news[3]
        ET.SubElement(doc, "snippet").text = snippet
        tree = ET.ElementTree(doc)
        tree.write(doc_dir_path + name, encoding = doc_encoding, xml_declaration = True)
        print(news[0],' ok')
news_pool = get_news_pool() 
crawl_news(news_pool, '../data/news/', 'utf-8')
print('done!')