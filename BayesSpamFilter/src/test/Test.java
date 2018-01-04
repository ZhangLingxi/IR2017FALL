package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test
{
	private static boolean filterKeyword(String strContent, String strKeyWord)
	{
		boolean retVal = false;

		if (strContent.toLowerCase().indexOf(strKeyWord.toLowerCase()) >= 0)
		{
			retVal = true;
		}

		return retVal;
	}

	public static List<String> getContent(String fileName)
	{
		List<String> totalList = new ArrayList<String>();
		try
		{
			File file = new File(fileName);

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String str;
			while ((str = br.readLine()) != null)
			{
				totalList.add(str.trim());
			}
			br.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return totalList;
	}

	public static String[] getAllWords(String fileName)
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			File file = new File(fileName);

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String str;
			while ((str = br.readLine()) != null)
			{
				sb.append(str.replaceAll("spam", "").replaceAll("ham", "").trim());
			}
			br.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		String[] a = sb.toString().split("[^a-zA-Z]+");
		return a;
	}

	public static void main(String[] args)
	{
		String[] banword = getAllWords("TrainingSet/SMSSpamCollection");
		Map<String, KeywordCount> keywordMap = new HashMap<String, KeywordCount>();
		for (String s : banword)
		{
			keywordMap.put(s, new KeywordCount(s, 0, 0, 0, 0));
		}
		List<String> mailList = getContent("TrainingSet/SMSSpamCollection");
		int spamNumber = 0;
		int legitNumber = 0;
		for (int i = 0; i < mailList.size(); i++)
		{
			String mailContent = mailList.get(i);
			if (mailContent.startsWith("spam"))
			{
				for (Map.Entry<String, KeywordCount> entry : keywordMap.entrySet())
				{
					boolean containFlag = filterKeyword(mailContent, entry.getKey());
					KeywordCount keywordCount = entry.getValue();

					if (containFlag == true)
					{
						keywordCount.spam += 1;
					}
					keywordCount.spamAll += 1;
				}
				spamNumber++;
			}
			else
			{
				for (Map.Entry<String, KeywordCount> entry : keywordMap.entrySet())
				{
					boolean containFlag = filterKeyword(mailContent, entry.getKey());
					KeywordCount keywordCount = entry.getValue();

					if (containFlag == true)
					{
						keywordCount.legit += 1;
					}
					keywordCount.legitAll += 1;

				}
				legitNumber++;
			}
		}

		List<String> needRemoveKey = new ArrayList<String>();
		
		for (Map.Entry<String, KeywordCount> entry : keywordMap.entrySet())
		{
			KeywordCount kcTemp = entry.getValue();
			if (kcTemp.spam + kcTemp.legit == 0)
			{
				needRemoveKey.add(entry.getKey());
				continue;
			}

			double Spam = 1.0 * kcTemp.spam / kcTemp.spamAll;
			double SpamAll = 1.0 * kcTemp.spamAll / (kcTemp.spamAll + kcTemp.legitAll);
			double Legit = 1.0 * kcTemp.legit / kcTemp.legitAll;
			double LegitAll = 1.0 * kcTemp.legitAll / (kcTemp.spamAll + kcTemp.legitAll);

			kcTemp.combiningProbabilities = (Spam * SpamAll) / (Spam * SpamAll + Legit * LegitAll); 

			if (kcTemp.combiningProbabilities < 0.93)
			{
				needRemoveKey.add(entry.getKey());
			}
		}
		
		for (String s : needRemoveKey)
		{
			keywordMap.remove(s);
		}
		List<String> testMailList = getContent("TestSet/TestFile.txt");
		int rightCount = 0;
		int wrongCount = 0;
		int spamCount = 0;
		for (String mail : testMailList)
		{
			String thisMail = mail;
			if (thisMail.startsWith("spam"))
			{
				spamCount++;
			}

			List<String> oneMailKeywordList = new ArrayList<String>();

			for (Map.Entry<String, KeywordCount> entry : keywordMap.entrySet())
			{
				boolean containFlag = filterKeyword(thisMail, entry.getKey());
				if (containFlag == true)
				{
					oneMailKeywordList.add(entry.getKey());
				}
			}

			if (oneMailKeywordList.size() <= 0)
			{
				continue;
			}
			
			double Pup = 1.0 * spamNumber / (spamNumber + legitNumber);
			double Pdown = 1.0f;
			for (String kw : oneMailKeywordList)
			{
				Pup = Pup * keywordMap.get(kw).spam / keywordMap.get(kw).spamAll;
				Pdown = Pdown * (keywordMap.get(kw).spam + keywordMap.get(kw).legit) / (spamNumber + legitNumber);
			}
			double Pmail = Pup / (Pup + Pdown);

			System.out.println("此邮件是垃圾邮件的概率为:" + Pmail + "，实际是否为垃圾邮件：" + thisMail.startsWith("spam"));

			if (Pmail > 0.999 && thisMail.startsWith("spam"))
			{
				rightCount++;
			}
			if (Pmail > 0.999 && thisMail.startsWith("ham"))
			{
				wrongCount++;
			}
		}
		System.out.println("垃圾邮件总数为" + spamCount + "，正确识别了" + rightCount + "封垃圾邮件，召回率为" + rightCount * 1.0 / spamCount + "，准确率为" + rightCount * 1.0
				/ (rightCount + wrongCount));
	}

	
}

