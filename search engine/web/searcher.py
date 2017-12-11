import sys
class Searcher:  
    def __init__(self, base_dir):  
        self.docs, self.index  = {}, {}  
        with open(base_dir + '\\PageRank.txt') as url_file:  
            for line in url_file.readlines():  
                try:  
                    url, docId, refCount = line.split(' ')  
                    self.docs[int(docId)] = {'url': url, 'refCount': int(refCount), 'id':int(docId)}  
                except Exception as e: pass #print 'invalid doc entry:', line  
  
        with open(base_dir + '\\Index.txt',encoding='utf-8') as index_file:  
            for word, chain in map(lambda line: line.split(' '), index_file.readlines()):  
                self.index[word] = map(int, chain.split(','))  
                try:  
                    self.index[word].sort(lambda doc1, doc2: self.docs[doc1]['refCount'] - self.docs[doc2]['refCount'])  
                except Exception as e: pass
  
    def search(self, keyword): 
        if keyword not in self.index:
            return []  
        l=list(map(lambda docId: self.docs[docId]['id'], self.index[keyword]))
        return l
  