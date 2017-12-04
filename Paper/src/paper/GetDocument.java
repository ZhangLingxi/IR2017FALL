package paper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * �ı�������
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
     * ����input�ļ��������ݲ�����ʵ�
     * @param filePath �ĵ�·��
     * @throws IOException IO�쳣
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
     * �����������ݲ�����ʵ�
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
     * �����ĵ�tfidfֵ���洢�ĵ�������ʹ��tf
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
     * ������������Ϊ�ĵ����������ĵ�����,ʹ��tfidf
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
     * ���ݲ�ѯ�������ĵ��������������ƶȣ����ĵ����дӴ�С���򣬲����
     * @param map ��Ҫ�����map
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
     * �����������ƶȴ洢�ĵ��ĵ÷ֲ�����
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
