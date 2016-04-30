package main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.DefaultEditorKit;

@SuppressWarnings({ "serial", "unused" })
public class View {

	private Controller controller;
	private String fileName = "Untitled";
	
	private JFrame frame;
	private JTextArea area;
	private JScrollPane scrollPane;
	private JFileChooser dialog;
	private Action saveAction, saveAsAction;
	
	private boolean modified;
	
	public View(final Controller controller){
		this.controller = controller;
		
		dialog = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
		dialog.addChoosableFileFilter(new FileNameExtensionFilter(".txt", "txt"));
		dialog.setAcceptAllFileFilterUsed(false);
		
		frame = new JFrame(TextEditor.APPNAME + " - " + fileName);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setLayout(new BorderLayout());
		
		// Text Area
		area = new JTextArea(40, 135);
		area.setFont(new Font("Monospaced", Font.PLAIN, 12));
		area.setWrapStyleWord(true);
		area.setLineWrap(true);
		scrollPane = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		ActionMap am = area.getActionMap();
		
		// Actions
		Action newAction = new AbstractAction("New", new ImageIcon("img/new-icon.png")){
			@Override
			public void actionPerformed(ActionEvent e) {
				newTextArea();
				controller.newEvent();
			}
		};
		
		Action openAction = new AbstractAction("Open", new ImageIcon("img/open-icon.png")){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(checkSaveHandled()){
					if(dialog.showOpenDialog(panel)==JFileChooser.APPROVE_OPTION) {
						controller.openEvent(dialog.getSelectedFile().getPath(), dialog.getSelectedFile().getName());
						setModified(false);
					}
				}
			}
		};
		
		saveAction = new AbstractAction("Save", new ImageIcon("img/save-icon.png")){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(controller.fileExists()){
					controller.saveEvent(area.getText());
					setModified(false);
				}
				else{
					//TODO- If you load a file and open a new one, the filename still appears in the dialog when you save
					if(dialog.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){
						controller.saveAsEvent(area.getText(), dialog.getSelectedFile().getAbsolutePath());
						setModified(false);
					}
				}
			}
		};
		
		saveAsAction = new AbstractAction("SaveAs", new ImageIcon("img/save-icon.png")){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(dialog.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){
					controller.saveAsEvent(area.getText(), dialog.getSelectedFile().getAbsolutePath());
					setModified(false);
				}
			}
		};
		
		Action quitAction = new AbstractAction("Quit"){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(checkSaveHandled()){
					System.exit(0);
				}
			}
		};
		
		Action cutAction = am.get(DefaultEditorKit.cutAction);
		cutAction.putValue(Action.SMALL_ICON, new ImageIcon("img/cut-icon.png"));
		
		Action copyAction =  am.get(DefaultEditorKit.copyAction);
		copyAction.putValue(Action.SMALL_ICON, new ImageIcon("img/copy-icon.png"));
		
		Action pasteAction =  am.get(DefaultEditorKit.pasteAction);
		pasteAction.putValue(Action.SMALL_ICON, new ImageIcon("img/paste-icon.png"));
		
		Action aboutAction = new AbstractAction("About TextEditor", new ImageIcon("img/coffee-icon.png")){
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO - Implement this
				JOptionPane.showMessageDialog(frame,
					    "TextEditor -- a simple text editor implemented in Java\n" +
					    		"Author \t Matt Golden\nReleased under The MIT License (MIT)",
					    "About TextEditor",
					    JOptionPane.INFORMATION_MESSAGE);
			}
		};
		
		setModified(false);
		
		// KeyListener to update modified boolean
		KeyListener keyListener = new KeyAdapter(){

			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				setModified(true);
			}
		};
		
		area.addKeyListener(keyListener);
		
		// MenuBar
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(newAction);
		fileMenu.add(openAction);
		fileMenu.add(saveAction);
		fileMenu.add(saveAsAction);
		fileMenu.addSeparator();
		fileMenu.add(quitAction);
		menuBar.add(fileMenu);
		
		JMenu editMenu = new JMenu("Edit");
		editMenu.add(cutAction);
		editMenu.add(copyAction);
		editMenu.add(pasteAction);
		menuBar.add(editMenu);
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(aboutAction);
		menuBar.add(helpMenu);
		
		//ToolBar
		JToolBar toolBar = new JToolBar();
		JButton newBtn = new JButton();
		toolBar.add(newAction);
		
		JButton openBtn = new JButton();
		toolBar.add(openAction);
		
		JButton saveBtn = new JButton();
		toolBar.add(saveAction);
		
		toolBar.addSeparator();
		
		JButton cutBtn = new JButton();
		toolBar.add(cutAction);
		
		JButton copyBtn = new JButton();
		toolBar.add(copyAction);
		
		JButton pasteBtn = new JButton();
		toolBar.add(pasteAction);
		
		toolBar.addSeparator();
		
		JButton coffeeBtn = new JButton();
		toolBar.add(aboutAction);
		
		panel.add(toolBar, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);
		frame.setJMenuBar(menuBar);
		frame.pack();
		frame.setVisible(true);
		area.requestFocus();
	}
	
	public boolean checkSaveHandled(){
		if(modified){
			switch(JOptionPane.showConfirmDialog(frame, "Would you like to save " + fileName + "?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION)) {
			case JOptionPane.YES_OPTION:	if(controller.fileExists()){
												controller.saveEvent(area.getText());
												setModified(false);
												return true;
											}
											else{
												if(dialog.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){
													controller.saveAsEvent(area.getText(), dialog.getSelectedFile().getAbsolutePath());
													setModified(false);
													return true;
												}
											}
			case JOptionPane.NO_OPTION:		return true;
			case JOptionPane.CANCEL_OPTION:	return false;
			}
		}
		return true;
	}
	
	private void newTextArea(){
		area.setText(null);
		fileName = "Untitled";
		frame.setTitle(TextEditor.APPNAME + " - " + fileName);
		modified = false;
	}
	
	public void updateView(String content, String fileName){
		area.setText(content);
		frame.setTitle(TextEditor.APPNAME + " - " + fileName);
		modified = false;
	}
	
	// No getter for modified.  It's fine being an inaccessible member of
	// this class because no other class needs to use it yet.
	public void setModified(boolean modified){
		saveAction.setEnabled(modified);
		saveAsAction.setEnabled(modified);
		this.modified = modified;
	}
}
