package paper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 * 界面类
 * @author ZhangLingxi
 */
@SuppressWarnings("serial")
public class UI extends JFrame{

	public static JTextArea sentenceArea,resultArea;
    private JButton searchButton,cancelButton;
    private JPanel sentencePanel,resultPanel,doPanel,panel;
    /**
     * 界面生成
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
    	doPanel = new JPanel();
    	panel = new JPanel();
    	searchButton = new JButton("search");
    	searchButton.addActionListener(new SearchListener());
    	cancelButton = new JButton("cancel");
    	cancelButton.addActionListener(new CancelListener());
    	sentencePanel.add(scrollPane1);
    	doPanel.add(searchButton);
    	doPanel.add(cancelButton);
    	resultPanel.add(scrollPane2);
    	panel.add(sentencePanel); 
    	panel.add(doPanel);
    	panel.add(resultPanel); 
    	panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
    	this.add(panel);
    	this.setSize(500,450);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.setVisible(true);
    	this.setTitle("PaperPaper");
    }
	/**
	 * search按钮监听类
	 * @author ZhangLingxi
	 */
    class SearchListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		// TODO Auto-generated method stub 
    		if(sentenceArea.getText()==null || sentenceArea.getText().trim().equals("")){
    			resultArea.setText("请输入查询内容！");
    		}
    		else{
    			resultArea.setText(""); 
    			GetDocument gd = new GetDocument();
    			try {
    				gd.parseFiles("/input");
    			} catch (IOException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    	        gd.parseInput();
    			gd.tfIdfCalculator();
    			gd.inputCalculator();
    			gd.getCosineSimilarity(); 
    		}
    	}
    }
    /**
     * cancel按钮监听类
     * @author ZhangLingxi
     */
    class CancelListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		// TODO Auto-generated method stub
    		sentenceArea.setText("");
    		resultArea.setText("");
    	}
    }
}
