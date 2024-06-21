package test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TetrisCanvas extends JPanel
	implements Runnable, KeyListener {
		  protected Thread worker;
		  protected Color colors[];
		  protected int w = 25;
		  protected TetrisData data;
		  protected int margin = 20;
		  protected boolean stop, makeNew;
		  protected Piece current;
		  protected int interval = 2000;
		  protected int level = 2;
		  protected Piece nextPiece;
		 
		  public TetrisCanvas() {
			  data = new TetrisData();
		 
			  addKeyListener(this);
			  colors = new Color[9]; // 테트리스 배경 및 조각 색
			  colors[0] = new Color(80, 80, 80); // 배경색(검은회색)
			  colors[1] = new Color(255, 0, 0); //빨간색
			  colors[2] = new Color(0, 255, 0); //녹색
			  colors[3] = new Color(0, 200, 255); //하늘색 
			  colors[4] = new Color(255, 255, 0); //노란색 
			  colors[5] = new Color(255, 150, 0); //황토색
			  colors[6] = new Color(210, 0, 240); //보라색
			  colors[7] = new Color(40, 0, 240); //파란색
			  colors[8] = new Color(000,051,051); //짙은녹색
		  }
		  
		  public void start() { // 게임 시작
			  data.clear();
			  worker = new Thread(this);
			  worker.start();
			  makeNew = true;
			  stop = false;
			  requestFocus();
			  repaint();
		  }
		  
		  public void stop() {
			  stop = true;
			  current = null;
		  }
		  
		  public void paint(Graphics g) {
			  super.paint(g);
			  
			  for(int i = 0; i < TetrisData.ROW; i++) { //쌓인 조각들 그리기
				  for(int k = 0; k < TetrisData.COL; k++) {
					  if(data.getAt(i, k) == 0) {
						  g.setColor(colors[data.getAt(i, k)]);
						  g.draw3DRect(margin/2 + w * k,
								  margin/2 + w * i, w, w, true);
					  } else {
						  g.setColor(colors[data.getAt(i, k)]);
						  g.fill3DRect(margin/2 + w * k,
								  margin/2 + w * i, w, w, true);
					  }
				  }
			  }
			 
			 if(current != null){ // 현재 내려오고 있는 테트리스 조각 그리기
				  for(int i = 0; i < 4; i++) {
					  g.setColor(colors[current.getType()]);
					  g.fill3DRect(margin/2 +
							  w * (current.getX()+current.c[i]),
							  margin/2 + w * (current.getY()+current.r[i]),
							  w, w, true);
				  }
			  }
			// 다음 테트로미노 그리기
			 if (nextPiece != null) {
				    int previewX = TetrisData.COL * w + margin; // nextPiece가 그려질 X 좌표 설정
			        int previewY = margin; // nextPiece가 그려질 Y 좌표 설정
			        int previewSize = w * 5; // nextPiece가 그려질 크기 설정
			        	g.setColor(Color.WHITE); // 배경 색상 설정
			        	g.fillRect(previewX, previewY, previewSize, previewSize); // nextPiece가 그려질 공간 클리어
			        	g.setColor(Color.BLACK); // 테두리 색상 설정
			        	g.drawRect(previewX, previewY, previewSize, previewSize); // nextPiece가 그려질 공간 테두리
			        	
			        for (int i = 0; i < 4; i++) {
			        g.setColor(colors[nextPiece.getType()]);
			        // 미리보기 테트로미노를 TetrisCanvas 내에서 그리는 위치 계산
			        int nextX = TetrisData.COL * w + margin + 40;
			        int nextY = margin + 40; // 미리보기 테트로미노를 그리는 Y 좌표 조정
			        g.fill3DRect(nextX + w * nextPiece.c[i], nextY + w * nextPiece.r[i], w, w, true);
			        }
			    }
		  }
		  
		  public Dimension getPreferredSize(){ // 테트리스 판의 크기 지정
			  int tw = w * TetrisData.COL + margin;
			  int th = w * TetrisData.ROW + margin;
			  return new Dimension(tw, th);
		  }
		  
		  public void run() {
			    while (!stop) {
			        try {
			            if (makeNew) { // 새로운 테트리스 조각 만들기
			                if (nextPiece == null) {
			                    nextPiece = createRandomPiece();
			                }
			                current = nextPiece; 
			                nextPiece = createRandomPiece();
			                makeNew = false;
			            } else { // 현재 만들어진 테트리스 조각 아래로 이동
			                if (current.moveDown()) { // 더 이상 아래로 내려갈 수 없는 경우를 의미
			                    makeNew = true;
			                    if (current.copy()) {
			                        stop();
			                        int score = data.getLine() * 175 * level;
			                        JOptionPane.showMessageDialog(this,
			                                "게임끝\n점수 : " + score);
			                    }
			                    current = null;
			                    data.removeLines();
			                }
			            }
			            repaint();
			            Thread.currentThread().sleep(interval / level);
			        } catch (Exception e) { }
			    }
			}
		  private Piece createRandomPiece() {
			    int random = (int) (Math.random() * Integer.MAX_VALUE) % 7;
			    switch (random) {
			        case 0: return new Bar(data);
			        case 1: return new Tee(data);
			        case 2: return new El(data);
			        case 3: return new O(data);
			        case 4: return new J(data);
			        case 5: return new S(data);
			        case 6: return new Z(data);
			        default: return new Tee(data);
			    }
		}
		  
		  
		// 키보드를 이용해서 테트리스 조각 제어
		 public void keyPressed(KeyEvent e) {
			 if(current == null) return;
		  
			 switch(e.getKeyCode()){
			 	case 37: // 왼쪽 화살표
			 		current.moveLeft();
			 		repaint();
			 		break;
			 	case 39: // 오른쪽 화살표
			 		current.moveRight();
			 		repaint();
			 		break;
			 	case 38: // 윗쪽 화살표
			 		current.rotate();
			 		repaint();
			 		break;
			 		
			 	case KeyEvent.VK_SPACE: // 스페이스 바가 눌렸을 때
		             movePieceToBottom(); // 테트로미노를 최대한 아래로 이동
		             break;
		             
			 	case 40: // 아랫쪽 화살표
			 		boolean temp = current.moveDown();
			 		if(temp){
			 			makeNew = true;
			 			if(current.copy()){
			 				stop();
			 				int score = data.getLine() * 175 * level;
			 				JOptionPane.showMessageDialog(this,
			 						"게임끝\n점수 : " + score);
			 			}
			 			current = null;
			 		}	
			 		data.removeLines();
			 		repaint();	
			 	
		     }
		 }
			
		// 테트로미노를 최대한 아래로 이동하는 메소드
		public void movePieceToBottom() {
		     if (current == null) return;
		     while (!current.moveDown()) {} // 테트로미노를 최대한 아래로 이동
		     makeNew = true; // 새로운 테트로미노 생성 플래그 설정
		     if (current.copy()) { // 테트로미노가 바닥에 닿았을 때
		         stop(); // 게임 중단
		         int score = data.getLine() * 175 * level;
		         JOptionPane.showMessageDialog(this,
		                 "게임 끝\n점수 : " + score);
		     }
		     current = null;
		     data.removeLines(); // 줄 삭제
		     repaint(); // 게임 화면 다시 그리기
		 }

		 public void keyReleased(KeyEvent e) { }
		 public void keyTyped(KeyEvent e) { }
}
