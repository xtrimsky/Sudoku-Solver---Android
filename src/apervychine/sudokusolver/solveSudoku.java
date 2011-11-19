package apervychine.sudokusolver;

import java.lang.reflect.Field;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class solveSudoku extends Activity {
	private EditText[][] text;
	private String[][] allowedPosition;
	private String[][] finalNumber;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initSolver();
    }
    
    public void initSolver(){
    	text = new EditText[10][10];
    	allowedPosition = new String[10][10];
    	finalNumber = new String[10][10];
    	
    	for(int i = 1; i<10; i++){
    		for(int j = 1; j<10; j++){
    			String property = "EditText" + Integer.toString(i) + Integer.toString(j);
    			Field f = null;
				try {
					f = R.id.class.getField(property);
				} catch (SecurityException e) {
					Toast.makeText(this, "Unknown error #1!", Toast.LENGTH_LONG).show();
				} catch (NoSuchFieldException e) {
				}
				int rid = 0;
				try {
					rid = f.getInt(R.id.class);
				} catch (IllegalArgumentException e) {
					Toast.makeText(this, "Unknown error #2!", Toast.LENGTH_LONG).show();
				} catch (IllegalAccessException e) {
					Toast.makeText(this, "Unknown error #3!", Toast.LENGTH_LONG).show();
				}
				
    			text[i][j] = (EditText) findViewById ( rid );
    			text[i][j].setBackgroundResource(R.color.white);
    			//text[i][j].setPadding(10, 1, 1, 1);
    			text[i][j].setText("");
    		}
    	}
    }
    
    public void onClickSolve(View view){
    	phase1();
    	phase2();
    	phase3();
    }
   
    /* update finalnumber and allowedpositions with user entered values */
    public void phase1(){
    	for(int i = 1; i<10; i++){
    		for(int j = 1; j<10; j++){
    			
    			allowedPosition[i][j] = "123456789";
    			finalNumber[i][j] = "";
    			
    			String temp = text[i][j].getText().toString();
    			
    			if(temp.length() > 0){
    				text[i][j].setBackgroundResource(R.color.gray);
    				allowedPosition[i][j] = "";
    				
    			}
    			
    			finalNumber[i][j] = temp;
    		}
    	}
    }
    
    //scans and updates all the data
    public void phase2(){
    	int i = 0;
    	//do 50 passes
    	while(i < 50){
    		scanLines();
    		scanColumns();
    		scanSquares();
    		
    		if(applyChanges()){
    			i = 0;
    		}
    		
    		i++;
    	}
    }
    
    //sets final data back
    public void phase3(){
    	for(int i = 1; i<10; i++){
    		for(int j = 1; j<10; j++){
    			if(finalNumber[i][j].length() == 1){
    				text[i][j].setText(String.valueOf( finalNumber[i][j] ));
    			}else{
    				text[i][j].setText(String.valueOf( allowedPosition[i][j] ));
    				text[i][j].setBackgroundResource(R.color.red);
    			}
    			
    		}
    	}
    }
    
    public void scanLines(){
    	for(int i = 1; i<10; i++){
    		String numbersOnLine = "";
    		
    		//first line read to get all numbers
    		for(int j = 1; j<10; j++){
    			if(finalNumber[i][j].length() > 0){
    				numbersOnLine = numbersOnLine.concat(finalNumber[i][j]);
    			}
    		}
    		
    		Integer[] possibleCharsCount = {0,0,0,0,0,0,0,0,0,0};
    		while(numbersOnLine.length() > 0){
    			String temp = numbersOnLine.substring(0, 1);
    			
    			//second line read to remove possible numbers
    			for(int j = 1; j<10; j++){
    				if(allowedPosition[i][j].length() > 0){
    					allowedPosition[i][j] = allowedPosition[i][j].replace(temp, "");
    				}
    				
    				//counts every characteres that could be somewhere
    				String temp2 = allowedPosition[i][j];
    				while(temp2.length() > 0){
    					int c = Integer.parseInt(temp2.substring(0,1));
    					possibleCharsCount[c]++;
    					
    					temp2 = temp2.substring(1);
    				}
        		}
    			
    			numbersOnLine = numbersOnLine.substring(1);
    		}
    		
    		for(int k = 1; k<10; k++){
    			if(possibleCharsCount[k] == 1){
    				for(int j = 1; j<10; j++){
    					String c = Integer.toString(k);
    					if(allowedPosition[i][j].indexOf(c) > 0){
    						allowedPosition[i][j] = c;
    					}
    				}
    			}
    		}
    	}
    }
    
    public void scanColumns(){
    	for(int j = 1; j<10; j++){
    		String numbersOnColumn = "";
    		
    		//first column read to get all numbers
    		for(int i = 1; i<10; i++){
    			if(finalNumber[i][j].length() > 0){
    				numbersOnColumn = numbersOnColumn.concat(finalNumber[i][j]);
    			}
    		}
    		
    		Integer[] possibleCharsCount = {0,0,0,0,0,0,0,0,0,0};
    		while(numbersOnColumn.length() > 0){
    			String temp = numbersOnColumn.substring(0, 1);
    			
    			//second column read to remove possible numbers
    			for(int i = 1; i<10; i++){
    				if(allowedPosition[i][j].length() > 0){
    					allowedPosition[i][j] = allowedPosition[i][j].replace(temp, "");
    				}
    				
    				//counts every characteres that could be somewhere
    				String temp2 = allowedPosition[i][j];
    				while(temp2.length() > 0){
    					int c = Integer.parseInt(temp2.substring(0,1));
    					possibleCharsCount[c]++;
    					
    					temp2 = temp2.substring(1);
    				}
        		}
    			
    			
    			numbersOnColumn = numbersOnColumn.substring(1);
    		}
    		
    		for(int k = 1; k<10; k++){
    			if(possibleCharsCount[k] == 1){
    				for(int i = 1; i<10; i++){
    					String c = Integer.toString(k);
    					if(allowedPosition[i][j].indexOf(c) > 0){
    						allowedPosition[i][j] = c;
    					}
    				}
    			}
    		}
    	}
    }
    
    public void scanSquares(){
    	for(int s = 1; s < 4; s++){
    		for(int r = 1; r < 4; r++){
    			int endi = s*3 + 1;
    			int starti = endi - 3;
    			int endj = r*3 + 1;
    			int startj = endj - 3;
    			
    			String numbersInSquare = "";
    			
    			//first square read to get all numbers
    			for(int i = starti; i<endi; i++){
    	    		for(int j = startj; j<endj; j++){
    	    			if(finalNumber[i][j].length() > 0){
    	    				numbersInSquare = numbersInSquare.concat(finalNumber[i][j]);
    	    			}
    	    		}
    	    	}
    			
    			Integer[] possibleCharsCount = {0,0,0,0,0,0,0,0,0,0};
    			while(numbersInSquare.length() > 0){
	    			String temp = numbersInSquare.substring(0, 1);
	    			
	    			//second square read to clean allowed positions
	    			for(int i = starti; i<endi; i++){
	    	    		for(int j = startj; j<endj; j++){
	    	    			if(allowedPosition[i][j].length() > 0){
	    	    				allowedPosition[i][j] = allowedPosition[i][j].replace(temp, "");
	    	    			}
	    	    			
	    	    			//counts every characteres that could be somewhere
	        				String temp2 = allowedPosition[i][j];
	        				while(temp2.length() > 0){
	        					int c = Integer.parseInt(temp2.substring(0,1));
	        					possibleCharsCount[c]++;
	        					
	        					temp2 = temp2.substring(1);
	        				}
	    	    		}
	    	    	}
	    			
	    			numbersInSquare = numbersInSquare.substring(1);
	    		}
    			
    			for(int k = 1; k<10; k++){
        			if(possibleCharsCount[k] == 1){
        				for(int i = starti; i<endi; i++){
    	    	    		for(int j = startj; j<endj; j++){
    	    	    			String c = Integer.toString(k);
            					if(allowedPosition[i][j].indexOf(c) > 0){
            						allowedPosition[i][j] = c;
            					}
    	    	    		}
    	    	    	}
        			}
        		}
        	}
    	}
    }
    
    /* if there was any changes apply them */
    public boolean applyChanges(){
    	boolean changes = false;
    	
    	for(int i = 1; i<10; i++){
    		for(int j = 1; j<10; j++){
    			if(allowedPosition[i][j].length() == 1){
    				changes = true;
    				
    				finalNumber[i][j] = allowedPosition[i][j];
    				allowedPosition[i][j] = "";
    			}
    		}
    	}
    	
    	return changes;
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	// This method is called once the menu is selected
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// We have only one menu option
		case R.id.pReset:
			initSolver();
			break;
		case R.id.pQuit:
			this.finish();
			break;
		}
		return true;
	}
}