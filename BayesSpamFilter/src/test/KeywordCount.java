package test;

public class KeywordCount {

	public KeywordCount(String keyword, int spam, int spamAll, int legit, int legitAll)
	{
		super();
		this.keyword = keyword;
		this.spam = spam;
		this.spamAll = spamAll;
		this.legit = legit;
		this.legitAll = legitAll;
	}
	public String keyword;
	public int spam;
	public int spamAll;
	public int legit;
	public int legitAll;
	public double combiningProbabilities;

	@Override
	public String toString()
	{
		return "[keyword=" + keyword + ", spam=" + spam + ", spamAll=" + spamAll + ", legit=" + legit + ", legitAll=" + legitAll + ", combiningProbabilities="
				+ combiningProbabilities + "]";
	}
}
