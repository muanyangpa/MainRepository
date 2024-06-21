package test;

public class TetrisData {
	public static final int ROW = 20;
	public static final int COL = 10;
	
	private int data[][]; //ROW x COL 의 배열
	private int line; //지운 줄 수
	
	public TetrisData() {
		data = new int [ROW][COL];
	}
	
	public int getAt(int x, int y) {
		if(x < 0 || x >= ROW || y< 0 || y>= COL) //유효한 데이터인지 아닌지.범위 값에서 벗어나면 0으로 체크함.
			return 0;
		return data[x][y];		
	}	
	
	public void setAt(int x, int y, int v) {
		data[x][y] = v;
	}
	
	public int getLine() {
		return line;
	}
	
	public synchronized void removeLines() {
		NEXT:
		for(int i= ROW-1; i>=0; i--) {
			boolean done = true;
			for(int k =0; k<COL; k++) {
				if(data[i][k]==0) {			
					done = false;
					continue NEXT;
				}
			}
			if(done) {
				line++;
				 for (int x = i; x > 0; x--) {
					 for (int y=0; y<COL; y++) {
						 data[x][y] = data[x-1][y];
					 }
		         } // 지워지는 행 위의 조각들을 한칸씩 아래로 옮김.채워진 행부터 차례로 바로 그 위의 행을 아래로 내리면 자동적으로 채워진칸이 지워지면서 한 행씩 내려오게됨.
				 

				 //r
				 if(i !=0) {
		            for(int y = 0; y < COL; y++){
		                data[0][y] = 0;
		            }
				 } 
				 i++;
			}
		}
	}
	
	public void clear() { // data 배열 초기화
		for(int i=0; i< ROW; i++) {
			for(int k =0; k < COL; k++) {
				data[i][k] = 0;
			} 
		}
	}
	
	public void dump() { //data 배열 내용 출력 
		for(int i=0; i<ROW; i++) {
			for(int k = 0; k<COL; k++) {
				System.out.print(data[i][k] + " ");
			}
			System.out.println();
		}
	}
}
