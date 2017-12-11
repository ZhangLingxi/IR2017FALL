from urllib.request import urlopen, Request  
from html.parser import HTMLParser  
from urllib.parse import urlparse  
from multiprocessing import Queue
from importlib import reload 
import sys  
reload(sys)
#sys.setdefaultencoding("utf-8")  
  
base_url=urlparse('http://nankai.en.school.cucas.cn/') 
index_dir = '../data/'  


min_word_len, max_chain_len, excludeList = 4, 100, ['under', 'because', 'these', 'those']  
index, docs, queue = {},{base_url.geturl(): [0, 0]}, Queue(0)  
seq, url = 0, ''  
queue.put(base_url.geturl())  
  
def get_words(data):  
    for i in range(0, len(data)):  
            if data[i].isalpha() and (i == 0 or not data[i - 1].isalpha()): word_start = i  
            if data[i].isalpha() and (i == len(data) - 1 or not data[i + 1].isalpha()):  
                word = data[word_start: i + 1]  
                if len(word) >= min_word_len and word not in excludeList: yield  word  
  
class MyParser(HTMLParser):  
    def __init__(self):  
        HTMLParser.__init__(self)  
        self.__stack = []  
  
    def handle_starttag(self, tag, attrList):  
        self.__stack.append(tag)  
        attrs = {}  
        for kv in attrList: attrs[kv[0]] = kv[1]  
        if tag == 'a':  
            try:  
                link = attrs['href']  
                pr = urlparse(link)  
                i = pr.path.find('.')  
                if (i > 0 and pr.path[i + 1:] in ['pdf', 'png', 'JPG', 'jpg', 'jpeg', 'gif', 'bmp', 'flv', 'mp4', 'wmv','swf','css']): return  
                if pr.scheme == '':  
                    if pr.path[0] == '/': link = 'http://' + base_url.netloc + link  
                    else: link = url[0: url.rindex('/') + 1] + link  
                elif pr.scheme != 'http' or pr.netloc != base_url.netloc: return  
                if link[-1] == '/': link = link[0: -1]  
                link = link.strip()  
                if link not in docs:  
                    docs[link] = len(docs), 1  
                    queue.put(link)  
                else: docs[link][1] += 1  
            except Exception: return  
  
    def handle_endtag(self, tag):  
        if len(self.__stack) > 0: self.__stack.pop()  
  
    def handle_data(self, data):  
        if len(self.__stack) > 0 and self.__stack[-1] in ['script', 'style']: return  
        for word in get_words(data):  
            if word not in index: index[word] = []  
            if len(index[word]) > 0 and index[word][-1] == seq: continue  
            if len(index[word]) < max_chain_len: index[word].append(seq)  
  
parser, depth = MyParser(), 5  
while not queue.empty() and depth > 0:  
    for i in range(queue.qsize()):  
        url = queue.get()
        try:  
            parser.feed(urlopen(Request(url, headers={'User-agent': 'Mozilla 5.10'})).read().decode())  
            print (url, "indexed.") 
            seq += 1  
        except Exception as e: print ('Failed to index', url, '. the error is', e) 
    depth -= 1  
  
with open(index_dir + 'PageRank.txt','w',encoding='utf-8') as url_file:  
    url_file.writelines(map(lambda e: e[0] + ' ' + str(e[1][0]) + ' ' + str(e[1][1]) + '\n', docs.items()))  
with open(index_dir + 'Index.txt','w',encoding='utf-8') as index_file:  
   index_file.writelines( map(lambda e: e[0] + ' ' + ','.join(map(str, e[1])) + '\n', index.items()))  
  
print (seq,'pages indexed. Keyword number:', len(index)) 