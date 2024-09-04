package test;

import java.io.File;

public class findExecutableOnPath {

	public static void main(String[] args) {
		System.out.println(findExecutableOnPath("chromedriver.exe"));
	}
	private static String  findExecutableOnPath(String name) {
		// TODO Auto-generated method stub
		 for (String dirname : System.getenv("PATH").split(File.pathSeparator)) {
		        File file = new File(dirname, name);
		        if (file.isFile() && file.canExecute()) {
		            return file.getAbsolutePath();
		        }
		    }
		    throw new AssertionError("should have found the executable");

	}
	

}
