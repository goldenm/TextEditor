package main;

@SuppressWarnings("unused")
public class Controller {

	private View view;
	
	public Controller(){
		
	}
	
	public void setView(View view){
		this.view = view;
	}
	
	public void newEvent(){
		System.out.println("Controller logging newEvent.");
	}
	
	public void openEvent(String filename){
		System.out.println("Controller logging openEvent, received the String \"" + filename + "\" as argument.");
	}
	
	public void saveEvent(){
		System.out.println("Controller logging saveEvent.");
	}
	
}
