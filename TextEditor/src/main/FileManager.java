package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

@SuppressWarnings("unused")
public class FileManager {

	private Controller controller;
	private BufferedReader br;
	private PrintWriter pw;
	private String filePath;
	
	private BufferedWriter writer;
	
	public FileManager(Controller controller){
		this.controller = controller;
		filePath = null;
	}
	
	//TODO: User-friendlier exception handling 
	public void openFile(String filePath, String fileName){
		StringBuilder content = new StringBuilder();
		String tempString;
		
		try {
			br = new BufferedReader(new FileReader(filePath));
			
			while((tempString = br.readLine()) != null){
				content.append(tempString + "\n");
			}
			
			controller.loadFile(content.toString(), fileName);
			this.filePath = filePath;
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void saveFile(String content){
		
	}
	
	public void saveAsFile(String content, String filePath){
		try {
//			pw = new PrintWriter(filePath + ".txt");
//			pw.println(content);
			
			writer = new BufferedWriter(new FileWriter(filePath));
		    writer.write(content);
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
//			if(pw != null){
//				pw.close();
//			}
			
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public boolean fileExists(){
		if(filePath == null){
			return false;
		}
		else{
			return true;
		}
	}
}
