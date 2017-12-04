package paper;

import java.util.List;
/**
 * 计算tfidf类
 * @author ZhangLingxi
 */
public class TfIdf {

	/**
	 * 计算tf值
	 * @param totalterms 所有词项
	 * @param termToCheck 被检查词项
	 * @return tf值
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
	 * 计算idf值
	 * @param allTerms 所有词项
	 * @param termToCheck 被检查词项
	 * @return idf值
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
