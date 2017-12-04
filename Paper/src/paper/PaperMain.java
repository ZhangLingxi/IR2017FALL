package paper;

import java.io.*;
/**
 * 主类
 * @author ZhangLingxi
 */
public class PaperMain {

	/**
	 * 主函数
	 * @param args 主函数参数
	 * @throws FileNotFoundException 文件未找到异常
	 * @throws IOException IO异常
	 */
	public static void main(String args[]) throws FileNotFoundException, IOException
    {
		UI ui = new UI();
        ui.go();
    }
}
