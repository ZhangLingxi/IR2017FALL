from os import listdir
from os.path import isfile, join
mypath='../data/screens'
screenid = [ f.replace(".png","") for f in listdir(mypath) if isfile(join(mypath,f)) ]
 
for i in screenid:
    with open('../web/static/snapshot/'+i+'.html','w',encoding='utf-8') as quickfile:
        quickfile.write("<!DOCTYPE html><html><head><title>Webpage snapshot</title></head><body><img src=\"../screens/"+i+".png\"></body></html>")
        print(i+" success")
print("ok")
