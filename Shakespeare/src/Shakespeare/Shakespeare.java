package Shakespeare;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
/**
 * ����
 * @author ZhangLingxi
 */
public class Shakespeare extends JFrame{
	static int flag=1;
	static int num=43;
	static String[] file_name = {
    		"1kinghenryiv.txt",
    		"1kinghenryvi.txt",
    		"2kinghenryiv.txt",
    		"2kinghenryvi.txt",
    		"3kinghenryvi.txt",
    		"allswellthatendswell.txt",
    		"antonyandcleopatra.txt",
    		"asyoulikeit.txt",
    		"comedyoferrors.txt",
    		"coriolanus.txt",
    		"cymbeline.txt",
    		"glossary.txt",
    		"hamlet.txt",
    		"juliuscaesar.txt",
    		"kinghenryv.txt",
    		"kinghenryviii.txt",
    		"kingjohn.txt",
    		"kinglear.txt",
    		"kingrichardii.txt",
    		"kingrichardiii.txt",
    		"loverscomplaint.txt",
    		"loveslabourslost.txt",
    		"macbeth.txt.txt",
    		"measureforemeasure.txt",
    		"merchantofvenice.txt",
    		"merrywivesofwindsor.txt",
    		"midsummersnightsdream.txt",
    		"muchadoaboutnothing.txt",
    		"othello.txt",
    		"periclesprinceoftyre.txt",
    		"rapeoflucrece.txt",
    		"romeoandjuliet.txt",
    		"sonnets.txt",
    		"tamingoftheshrew.txt",
    		"tempest.txt",
    		"timonofathens.txt",
    		"titusandronicus.txt",
    		"troilusandcressida.txt",
    		"twelfthnight.txt",
    		"twogentlemenofverona.txt",
    		"various.txt",
    		"venusandadonis.txt",
    		"winterstale.txt",
    };
	static String[] andArray,orArray,notArray;
	static String doc[]=readDocs();
	static Map<String, Set<Integer>> reverted_indexs;
    static JTextArea sentenceArea,resultArea;
    static JButton searchButton,cancelButton,ANDButton,ORButton,NOTButton;
    static JPanel logicPanel,sentencePanel,resultPanel,doPanel,panel;
    static ArrayList<String> andList,orList,notList;
    
	/**
	 * ��ȡinput��txt�ļ����ݲ������ַ�������
	 * @return ����ȫ���ļ����ݵ��ַ�������
	 */
	public static String[] readDocs(){
		String[] docs=new String[num];
        try {
        	for(int i=0;i<num;i++){
        		InputStream stream = Shakespeare.class.getResourceAsStream("/input/"+file_name[i]);
        		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        		String line = null;
        		docs[i]="";
        		while((line = reader.readLine()) != null){
            		docs[i]+=line;
            	}
        		reader.close();
        	}
        }catch(Exception ex) {
        	ex.printStackTrace();
        }
        return docs;
	}
	
	/**
	 * ������������
	 */
	public static void revertedIndex() {
        reverted_indexs = new HashMap<String, Set<Integer>>();
        String[] upd_docs = new String[num];
        String[] terms;
        int i = 0,  j;
        for (i = 0; i < num; i++) {
            upd_docs[i] = doc[i].replaceAll(";|:|\\.", "");
            terms = upd_docs[i].split(" ");
            for (j = 0; j < terms.length; j++) {
                if (!reverted_indexs.containsKey(terms[j])) {
                    Set<Integer> sub = new HashSet<Integer>();
                    sub.add(i + 1);
                    reverted_indexs.put(terms[j], sub);
                } else {
                    Set<Integer> sub = reverted_indexs.get(terms[j]);
                    sub.add(i + 1);
                }
            }
        }
    }
	
	/**
	 * ����������ݷֳ�����list�����array
	 */
	public static void divide(){
		andList = new ArrayList<String>();
		orList = new ArrayList<String>();
		notList = new ArrayList<String>();		
		String content = sentenceArea.getText();
		String[] str = content.split(" ");
		andList.add(str[0]);
		for(int i = 1; i < str.length-1; i++){
			if(str[i].equals("AND")){
				andList.add(str[i+1]);
			}
			if(str[i].equals("OR")){
				orList.add(str[i+1]);
			}
			if(str[i].equals("NOT")){
				notList.add(str[i+1]);
			}
			else
				continue;
		}
		andArray = (String[])andList.toArray(new String[andList.size()]);
		orArray = (String[])orList.toArray(new String[orList.size()]);
		notArray = (String[])notList.toArray(new String[notList.size()]);
	}
	
	/**
	 * ���������ϵĲ���
	 * @param s1 ����1
	 * @param s2 ����2
	 * @return s1��s2�Ĳ���
	 */
	public static Set<Integer> union(Set<Integer> s1 , Set<Integer> s2){
       Set<Integer> unionSet = new HashSet<Integer>();
       for(int s : s1){
           unionSet.add(s);
       }
       if(s2==null){
    	   return unionSet;
       }
       for(int s : s2){
            unionSet.add(s);
       }
       return unionSet;   
	}
	
	/**
	 * ���������ϵĽ���
	 * @param s1 ����1
	 * @param s2 ����2
	 * @return s1��s2�Ľ���
	 */
	public static Set<Integer> same(Set<Integer> s1 , Set<Integer> s2){
        Set<Integer> sameSet = new HashSet<Integer>();
        for(int s : s1){
            if(s2.contains(s))
                sameSet.add(s);
        }
        return sameSet;
    }
	
	/**
	 * ���������ϲ
	 * @param s1 ����1
	 * @param s2 Ҫ��ȥ���ļ���2
	 * @return s1ȥ��s2��Ĳ
	 */
	public static Set<Integer> different(Set<Integer> s1 , Set<Integer> s2){
		Set<Integer> differentSet = new HashSet<Integer>();
		if(s2==null){
			 return s1;
		}
		else{
			for(int s : s1){
	            if(!s2.contains(s))
	                differentSet.add(s);
	        }
			return differentSet;
		}
    }
		
	/**
	 * ��andListȡ����
	 * @return andList��Ԫ��ȡ����������Ӧ�ĵ��ı��
	 */
	public static Set<Integer> intersect_and(){
		Set<Integer> tempSet1 = reverted_indexs.get(andArray[0]);
		for(int i=1;i<andList.size();i++){
			Set<Integer> tempSet2 = reverted_indexs.get(andArray[i]);
			tempSet1 = same(tempSet1,tempSet2);
			same(tempSet1,tempSet2).clear();
		}
		return tempSet1;
	}
	
	/**
	 * ��orListȡ����
	 * @return orList��Ԫ��ȡ����������Ӧ�ĵ��ı��
	 */
	public static Set<Integer> union_or(){
		if(orList.size()==0){
			return null;
		}
		else{
			Set<Integer> tempSet1 = reverted_indexs.get(orArray[0]);
			for(int i=1;i<orList.size();i++){
				Set<Integer> tempSet2 = reverted_indexs.get(orArray[i]);
				tempSet1 = union(tempSet1,tempSet2);
				union(tempSet1,tempSet2).clear();
			}
			return tempSet1;
		}
	}

	/**
	 * ��notListȡ����
	 * @return notList��Ԫ��ȡ����������Ӧ�ĵ��ı��
	 */
	public static Set<Integer> intersect_not(){
		if(notList.size()==0){
			return null;
		}
		else{
			Set<Integer> tempSet1 = reverted_indexs.get(notArray[0]);
			for(int i=1;i<notList.size();i++){
				Set<Integer> tempSet2 = reverted_indexs.get(notArray[i]);
				tempSet1 = same(tempSet1,tempSet2);
				same(tempSet1,tempSet2).clear();
			}
			return tempSet1;
		}
	}
	
	/**
	 * �ж�andList,orList,notList�Ƿ��в����ڵĴ�,and/or���У�flag=0�����أ�no���У������Ƴ�
	 */
	public static void judge(){
		for(String s:andList){
			if(!reverted_indexs.containsKey(s)){
				flag=0;
				return;
			}
		}
		for(String s:orList){
			if(!reverted_indexs.containsKey(s)){
				flag=0;
				return;
			}
		}
		for(String s:notList){
			if(!reverted_indexs.containsKey(s)){
				notList.remove(s);
			}
		}
		return;
	}
	
	/**
	 * ��ʾ��ѯ���
	 */
	public static void answer() {
		judge();
		if(flag==0){
			resultArea.setText("�޽��");
        	return;
		}
		Set<Integer> i_a = intersect_and();
		Set<Integer> u_o = union_or();
		Set<Integer> i_n = intersect_not();
		Set<Integer> ia_uo = union(i_a,u_o);
		Set<Integer> indexResults = different(ia_uo,i_n);
		if(indexResults.size()==0){
        	resultArea.setText("�޽��");
        	return;
		}
		else {
			String[] results = new String[indexResults.size()];
			int[] index_results = new int[indexResults.size()]; 
			int temp=0;
	        for(int s : indexResults){  
	        	index_results[temp] = s; 
	        	temp++;
	        }
	        for(int k = 0;k<indexResults.size();k++){  
	        	results[k] = file_name[index_results[k]-1];
	        }
	        int length = results.length;
	        resultArea.setText("");
	        for(int i=0;i<length;i++){
	        	resultArea.append(results[i]+"\n");
	        }
		}
    }
	
	/**
	 * ��������
	 */
    public void go(){
    	sentenceArea = new JTextArea(6,35);
    	resultArea = new JTextArea(8,35);
    	sentenceArea.setLineWrap(true);
    	resultArea.setLineWrap(true);
    	JScrollPane scrollPane1 = new JScrollPane(sentenceArea);
    	JScrollPane scrollPane2 = new JScrollPane(resultArea);
    	scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    	scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    	scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    	scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    	sentencePanel = new JPanel();
    	resultPanel = new JPanel();
    	logicPanel = new JPanel();
    	doPanel = new JPanel();
    	panel = new JPanel();
    	searchButton = new JButton("search");
    	searchButton.addActionListener(new SearchListener());
    	cancelButton = new JButton("cancel");
    	cancelButton.addActionListener(new CancelListener());
    	ANDButton = new JButton("AND");
    	ANDButton.addActionListener(new ANDListener());
    	ORButton = new JButton("OR");
    	ORButton.addActionListener(new ORListener());
    	NOTButton = new JButton("NOT");
    	NOTButton.addActionListener(new NOTListener());
    	sentencePanel.add(scrollPane1);
    	doPanel.add(searchButton);
    	doPanel.add(cancelButton);
    	logicPanel.add(ANDButton);
    	logicPanel.add(ORButton);
    	logicPanel.add(NOTButton);
    	resultPanel.add(scrollPane2);
    	panel.add(sentencePanel);   
    	panel.add(logicPanel);
    	panel.add(resultPanel);  
    	panel.add(doPanel);
    	panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
    	this.add(panel);
    	this.setSize(500,450);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.setVisible(true);
    	this.setTitle("Shakespeare");
    }
	
    /**
     * search��ť����
     * @author ZhangLingxi
     */
    class SearchListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		// TODO Auto-generated method stub 
    		if(sentenceArea.getText()==null || sentenceArea.getText().trim().equals("")){
    			resultArea.setText("�������ѯ���ݣ�");
    		}
    		else{
    			divide();
        		answer();
    		}
    	}
    }
    
    /**
     * cancel��ť����
     * @author ZhangLingxi
     */
    class CancelListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		// TODO Auto-generated method stub
    		sentenceArea.setText("");
    		resultArea.setText("");
    	}
    }
    
    /**
     * AND��ť����
     * @author ZhangLingxi
     */
    class ANDListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		sentenceArea.append(" AND ");
    		sentenceArea.requestFocus();
    	}
    }

    /**
     * OR��ť����
     * @author ZhangLingxi
     */
    class ORListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		sentenceArea.append(" OR ");
    		sentenceArea.requestFocus();
    	}
    }
	
    /**
     * NOT��ť����
     * @author ZhangLingxi
     */
    class NOTListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		sentenceArea.append(" NOT ");
    		sentenceArea.requestFocus();
    	}
    }
    
    /**
     * ������
     * @param args ����������
     */
    public static void main(String[] args){
    	Shakespeare gui = new Shakespeare();
    	revertedIndex();
    	gui.go();
    }

}
