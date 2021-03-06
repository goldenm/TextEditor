package main;

public class Controller {

	private View view;
	private FileManager fileManager;
	
	public Controller(){
		fileManager = new FileManager(this);
	}
	
	public void setView(View view){
		this.view = view;
	}
	
	public void newEvent(){
		fileManager.newEvent();
	}
	
	public void openEvent(String filePath, String fileName){
		fileManager.openFile(filePath, fileName);
	}
	
	public void loadFile(String content, String fileName) {
		view.updateView(content, fileName);
	}
	
	public void saveEvent(String content){
		fileManager.saveFile(content);
	}
	
	public void saveAsEvent(String content, String filePath){
		fileManager.saveAsFile(content, filePath);
	}
	
	public boolean fileExists(){
		return fileManager.fileExists();
	}
}
