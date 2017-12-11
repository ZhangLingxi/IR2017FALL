from flask import Flask, render_template, request
from searcher import Searcher
import xml.etree.ElementTree as ET
import time

app = Flask(__name__)

global page
global keys

def init():
    global dir_path
    dir_path = '../data/news/'

@app.route('/')
def main():
    init()
    return render_template('index.html', error=True)

@app.route('/search/', methods=['POST'])
def search():
    try:
        global keys
        keys = request.form['key_word']
        if keys not in ['']:
            print(time.clock())
            page = searchidlist(keys) 
            docs = cut_page(page, 0)
            print(time.clock())
            return render_template('search.html', key=keys, docs=docs, page=page,
                                   error=True)
        else:
            return render_template('search.html', error=False)

    except:
        print('search error')

def searchidlist(key):
    global page
    global doc_id
    se = Searcher('../data/')
    doc_id=se.search(key)
    page = []
    for i in range(1, (len(doc_id) // 10 + 2)):
        page.append(i)
    return page

def cut_page(page, no):
    docs = find(doc_id[no*10:page[no]*10])
    return docs

def find(docid):
    docs = []
    global dir_path
    for id in docid:
        root = ET.parse(dir_path + '%s.xml' % id).getroot()
        url = root.find('url').text   
        title = root.find('title').text
        snippet = root.find('snippet').text+"..."
        datetime = root.find('datetime').text
        doc = {'url': url, 'title': title, 'snippet': snippet, 'datetime': datetime, 'time': time, 'id': id}
        docs.append(doc)
    return docs

@app.route('/search/page/<page_no>/', methods=['GET'])
def next_page(page_no):
    try:
        page_no = int(page_no)
        docs = cut_page(page, (page_no-1))
        return render_template('search.html', key=keys, docs=docs, page=page,
                               error=True)
    except:
        print('next error')

if __name__ == '__main__':
    app.run()
