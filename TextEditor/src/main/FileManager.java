package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("unused")
public class FileManager {

	private Controller controller;
	private BufferedReader br;
	private FileWriter fw;
	private String filePath;
	
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
	
	public void saveFile(){
		
	}
	
	public void saveAsFile(String content, String filePath){
		
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
