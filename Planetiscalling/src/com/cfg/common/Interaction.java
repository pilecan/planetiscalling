package com.cfg.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.model.Result;

public class Interaction {
	private static Interaction instance = new Interaction();

	public static Interaction getInstance(){
		return instance;
	}
	
	public void listenerButtons(final JButton askMeBt, final Result result, final JPanel outputPanel, final JEditorPane jEditorPane,  final JScrollPane askmeScrollPan){
		askMeBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result.showAskMeAnswer(outputPanel, jEditorPane, askMeBt, askmeScrollPan);
			}
		});	

		
		
	}
	

}
