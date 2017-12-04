package paper;
/**
 * 计算余弦相似度类
 * @author ZhangLingxi
 *
 */
public class Cosine {

	/**
	 * 计算余弦相似度
	 * @param docVector1 参数向量1
	 * @param docVector2 参数向量2
	 * @return 余弦相似度
	 */
	public double cosineSimilarity(double[] docVector1, double[] docVector2) {
        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;
        double cosineSimilarity = 0.0;
        for (int i = 0; i < docVector1.length; i++) 
        {
            dotProduct += docVector1[i] * docVector2[i];  
            magnitude1 += Math.pow(docVector1[i], 2);  
            magnitude2 += Math.pow(docVector2[i], 2); 
        }
        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);
        if (magnitude1 != 0.0 | magnitude2 != 0.0) {
            cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
        } else {
            return 0.0;
        }
        return cosineSimilarity;
    }
}
