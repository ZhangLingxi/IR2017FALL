package paper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 文本分析类
 * @author ZhangLingxi
 */
public class GetDocument {

	private List<String[]> termsDocsArray = new ArrayList<String[]>();
    private List<String> allTerms = new ArrayList<String>();
    private List<double[]> tfidfDocsVector = new ArrayList<double[]>();
    private List<String[]> inputDocsArray = new ArrayList<String[]>();
    private List<double[]> inputVector = new ArrayList<double[]>();
    private String[] file_name = {
    		"1984 DNC Address.txt",
    		"I Have a Dream.txt",
    		"I Have Been to the Mountain Top.txt",
    		"The Declaration of Independence.txt",
    		"We Shall Overcome.txt"
    };
    private int num=file_name.length;
    private Map<String,Double> answer=new HashMap<String,Double>();
    /**
     * 分析input文件夹中内容并存入词典
     * @param filePath 文档路径
     * @throws IOException IO异常
     */
    public void parseFiles(String filePath) throws IOException {
    	for(int i=0;i<num;i++){
    		InputStream stream = GetDocument.class.getResourceAsStream("/input/"+file_name[i]);
    		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
    		StringBuilder sb = new StringBuilder();
            String s = null;
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");
            for (String term : tokenizedTerms) {
                if (!allTerms.contains(term)) {
                    allTerms.add(term);
                }
            }
            termsDocsArray.add(tokenizedTerms);
    	}
    }
    /**
     * 分析输入内容并存入词典
     */
    public void parseInput(){
    	String s=UI.sentenceArea.getText();
    	String[] tokenizedTerms=s.split(" ");
    	for (String term : tokenizedTerms) {
            if (!allTerms.contains(term)) {
                allTerms.add(term);
            }
        }
    	inputDocsArray.add(tokenizedTerms);
    }
    /**
     * 计算文档tfidf值，存储文档向量，使用tf
     */
    public void tfIdfCalculator() {
        double tf; 
        double idf; 
        double tfidf;         
        for (String[] docTermsArray : termsDocsArray) {
            double[] tfidfvectors = new double[allTerms.size()];
            int count = 0;
            for (String terms : allTerms) {
                tf = new TfIdf().tfCalculator(docTermsArray, terms);
                idf = 1;
                tfidf = tf * idf;
                tfidfvectors[count] = tfidf;
                count++;
            }
            tfidfDocsVector.add(tfidfvectors);             
        }
    }
    /**
     * 将输入内容视为文档，并计算文档向量,使用tfidf
     */
    public void inputCalculator() {
        double tf; 
        double idf; 
        double tfidf;           
        for (String[] docTermsArray : inputDocsArray) {
            double[] tfidfvectors = new double[allTerms.size()];
            int count = 0;
            for (String terms : allTerms) {
                tf = new TfIdf().tfCalculator(docTermsArray, terms);
                idf = new TfIdf().idfCalculator(termsDocsArray, terms);
                tfidf = tf * idf;
                tfidfvectors[count] = tfidf;
                count++;
            }
            inputVector.add(tfidfvectors);             
        }
    } 
    /**
     * 根据查询向量和文档向量的余弦相似度，对文档进行从大到小排序，并输出
     * @param map 需要排序的map
     */
    public static void mapSort(Map<String, Double> map){
    	List<Map.Entry<String, Double>> listData = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
    	Collections.sort(listData, new Comparator<Map.Entry<String, Double>>(){
    		public int compare(Map.Entry<String,Double > o1, Map.Entry<String, Double> o2){
    			return (int)((o2.getValue() - o1.getValue()) * 1000);
    		}
    	});
    	for(int i=0;i<3;i++){
    		if(listData.get(i).getValue()>0){
    			UI.resultArea.append(listData.get(i).getKey()+"\n");
    		}
    	}
    }
    /**
     * 根据余弦相似度存储文档的得分并排序
     */
    public void getCosineSimilarity() {
    	double[] score = new double[tfidfDocsVector.size()];
    	for(int i=0;i<tfidfDocsVector.size();i++){
    		score[i] = new Cosine().cosineSimilarity(tfidfDocsVector.get(i),inputVector.get(0));
    		answer.put(file_name[i], score[i]);
    	}
    	mapSort(answer);
    }
}
