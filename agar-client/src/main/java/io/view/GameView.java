package io.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import io.model.Cell;
import io.model.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameView {

	@FXML
	private Button select;

	private JTextArea text;
	
	@FXML
    private ComboBox<?> songList;

    @FXML
    private Label songTxt;
	
//    private TextArea escribir;

	private Label scoreTxt;

	private Canvas myCanvas;

	private Game game;

	private Parent parent;

	private Stage stage;

	public void init(Parent parent, Game game) {
		this.parent = parent;

		
		
		
		for (int i = 0; i < parent.getChildrenUnmodifiable().size(); i++) {

			Node node = parent.getChildrenUnmodifiable().get(i);

			if (node != null && node.getId() != null) {
				if (node.getId().equals("myCanvas")) {
					myCanvas = (Canvas) node;
				}
			}

		}

		AnchorPane content = (AnchorPane) parent.getChildrenUnmodifiable().get(1);

		for (int i = 0; i < content.getChildren().size(); i++) {
			Node node = content.getChildrenUnmodifiable().get(i);

			if (node != null && node.getId() != null) {
				if (node.getId().equals("scoreTxt")) {
					scoreTxt = (Label) node;
				}
			}
		}

		this.game = game;
	}

	private void drawShapes(GraphicsContext gc) {

		gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());

		List<Cell> cells = game.getCells();
		ArrayList<Cell> userCells = new ArrayList<Cell>();
		for (int i = 0; i < cells.size(); i++) {

			int pos1 = cells.get(i).getX();
			int pos2 = cells.get(i).getY();
			int color1 = cells.get(i).getR();
			int color2 = cells.get(i).getG();
			int color3 = cells.get(i).getB();
			double radius = cells.get(i).getRadius();
			Color color = Color.rgb(color1, color2, color3);

			if (cells.get(i).getUser() != null) {
				userCells.add(cells.get(i));

			} else {
				gc.setFill(color);
				gc.fillOval(pos1, pos2, radius, radius);
			}

		}

		for (Cell userCell : userCells) {

			if (userCell != null) {
				int pos1 = userCell.getX();
				int pos2 = userCell.getY();
				int color1 = userCell.getR();
				int color2 = userCell.getG();
				int color3 = userCell.getB();
				double radius = userCell.getRadius();
				Color color = Color.rgb(color1, color2, color3);

				gc.setFill(color);

				gc.fillOval(pos1, pos2, radius, radius);

				gc.setFill(Color.BLACK);

				gc.fillText(userCell.getUser(), pos1, pos2 + (radius), radius);

			}
		}

		scoreTxt.setText("Score: " + game.getScore());

	}
	
	//Chat
//	  public void mostrarMensaje(String mensaje) {
//	        escribir.appendText(mensaje + "\n");
//	    }

	public void dibujarUsuarios() {

	}

	public void dibujar() {

		GraphicsContext canvas2 = myCanvas.getGraphicsContext2D();
		drawShapes(canvas2);
	}

	public void setStage(Stage stage) {
		// TODO Auto-generated method stub
		this.stage = stage;
	}

	public Parent getParent() {
		return parent;
	}

	public Stage getStage() {
		return stage;
	}

	public void setKeyHandler(EventHandler<KeyEvent> handler) {
		this.stage.getScene().addEventFilter(KeyEvent.KEY_TYPED, handler);

	}
	
	



	public JTextArea getText() {
		return text;
	}

	public void setText(JTextArea text) {
		this.text = text;
	}

	//Chat
//	public TextArea getEscribir() {
//		return escribir;
//	}
//
//	public void setEscribir(TextArea escribir) {
//		this.escribir = escribir;
//	}

	public void hide() {
		this.stage.close();

	}
	
	void selectBut(ActionEvent event) {

    }

}
