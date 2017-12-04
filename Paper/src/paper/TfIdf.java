package paper;

import java.util.List;
/**
 * ����tfidf��
 * @author ZhangLingxi
 */
public class TfIdf {

	/**
	 * ����tfֵ
	 * @param totalterms ���д���
	 * @param termToCheck ��������
	 * @return tfֵ
	 */
	public double tfCalculator(String[] totalterms, String termToCheck) {
		
        double count = 0; 
        for (String s : totalterms) {
            if (s.equalsIgnoreCase(termToCheck)) {
                count++;
            }
        }
        return count / totalterms.length;
    }
	/**
	 * ����idfֵ
	 * @param allTerms ���д���
	 * @param termToCheck ��������
	 * @return idfֵ
	 */
	public double idfCalculator(List<String[]> allTerms, String termToCheck) {
        double count = 0;
        for (String[] ss : allTerms) {
            for (String s : ss) {
                if (s.equalsIgnoreCase(termToCheck)) {
                    count++;
                    break;
                }
            }
        }
        return Math.log(allTerms.size() / count);
    }
}
