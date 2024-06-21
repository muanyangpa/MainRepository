package test;

import java.awt.Point;

public abstract class Piece {
	final int DOWN = 0; //이동방향지정
	final int LEFT = 1;
	final int RIGHT = 2;
	protected int r[]; //Y축 좌표 배열
	protected int c[]; //X축 좌표 배열
	protected TetrisData data; //테트리스 내부 데이터 
	protected Point center; //조각의 중심 좌표
	
	public Piece(TetrisData data) {
		r = new int[4];
		c = new int[4];
		this.data = data;
		center = new Point(5,0);
	}
	
	public abstract int getType();
	public abstract int roteType();
	
	public int getX() {return center.x;}
	public int getY() {return center.y;}
	
	public boolean copy() { //값 복사 
		boolean value = false;
		int x = getX();
		int y = getY();
		
		if(getMinY() + y <=0) {  //개입 종료 상황   
			value = true;
		}
		
		for (int i=0; i<4; i++) {
			data.setAt(y + r[i], x + c[i], getType());
		}
		return value;
	}
	
	public boolean isOverlap(int dir) { //다른 조각과 겹치는지 파악
		int x = getX();
		int y = getY();
		switch(dir) {
		case 0 : // 아래 
			for(int i=0;i < r.length;i++) {
				if(data.getAt(y+r[i]+1, x+c[i])!=0) {
					return true;
				}
			}
			break;
		case 1 : // 왼쪽 
			for(int i=0; i < r.length ; i++) {
				if(data.getAt(y+r[i], x+c[i]-1) !=0) {
					return true;
				}
			}
			break;
		case 2 : // 오른쪽 
			for(int i=0; i < r.length ; i++) {
				if(data.getAt(y+r[i], x+c[i]+1) !=0) {
					return true;
				}
			}
			break;
		}
		return false;
	}
	
	public int getMinX() {
		int min = c[0];
		for(int i=1; i< c.length; i++) {
			if(c[i] < min) {
				min = c[i];
			}
		}
		return min;
	}
	
	public int getMaxX() {
		int max = c[0];
		for(int i=1; i< c.length; i++) {
			if(c[i] > max) { //c는 x축 좌표 배열 
				max = c[i];
			}
		}
		return max;
	}
	
	public int getMinY() {
		int min = r[0];
		for(int i=1; i< r.length; i++) {
			if(r[i] < min) {
				min = r[i];
			}
		}
		return min;
	}
	
	public int getMaxY() {
		int max = r[0];
		for(int i=1; i < r.length; i++) {
			if(r[i] > max) {
				max = r[i];
			}
		}
		return max;
	}
	
	public boolean moveDown() { // 아래로 이동
		if(center.y + getMaxY() +1 < TetrisData.ROW) {
			if(isOverlap(DOWN)!=true) {
				center.y++;
			} else {
				return true;
			}
		}else {
			return true;
		}
		
		return false;
	}
	
	public void moveLeft() { //왼쪽으로 이동
		if(center.x + getMinX() -1 >= 0) 
			if(isOverlap(LEFT) !=true) {center.x--;} 
			else return;
	}
		
	public void moveRight() { //오른쪽으로 이동
		if(center.x + getMaxX() + 1 < TetrisData.COL) 
			if(isOverlap(RIGHT) != true) {center.x++;} 
			else return ;
	}
	public void rotate() {
	    int rc = roteType();
	    if (rc <= 1) return;

	    if (rc == 2) {
	        rotate4();
	        rotate4();
	        rotate4();
	    } else {
	        rotate4();
	    }

	    // 벽 충돌 체크 및 위치 조정
	    int x = getX();
	    int y = getY();
	    boolean adjusted = false;

	    for (int i = 0; i < r.length; i++) {
	        while (x + c[i] < 0) { // 왼쪽 벽에 붙은 경우
	            center.x++;
	            x = getX();
	            adjusted = true;
	        }
	        while (x + c[i] >= TetrisData.COL) { // 오른쪽 벽에 붙은 경우
	            center.x--;
	            x = getX();
	            adjusted = true;
	        }
	        while (y + r[i] >= TetrisData.ROW ) { // 바닥에 붙은 경우
	            center.y--;
	            y = getY();
	            adjusted = true;
	        }
	        while (y + r[i] <= 0) { // 천장에 붙은 경우
	            center.y++;
	            y = getY();
	            adjusted = true;
	        }
	        
	    }

	    // 벽 밖으로 나가는 경우 원래 상태로 되돌리기
	    if (adjusted && isOverlap(0)) {
	        if (rc == 2) {
	            rotate4();
	        } else {
	            rotate4();
	            rotate4();
	            rotate4();
	        }
	    }
	}

	/*public void rotate() { //조각 회전
		int rc = roteType();
		if(rc <= 1) return;
		
		if(rc==2) {
			rotate4();
			rotate4();
			rotate4();
		} else {
			rotate4();
		}
	}*/
	
	public void rotate4() { //조각 회전 90도 회전 
		for(int i =0; i<4; i++) {
			
			int temp = c[i];
			c[i] = -r[i];
			r[i] = temp;
		}
	}
}
