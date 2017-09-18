package com.funtap.minigame.puzzledemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * 
 */

/**
 * @author kamil
 *
 */
public class GameBoard implements OnClickListener{

	public final static int ORIENTATION_PORTRAIT = 0;
	public final static int ORIENTATION_HORIZONTAL = 1;
	
	private RelativeLayout screen;
	private Context context;
	private Dimension bitmapResolution; // e. g. 480 x 854 like defy
	private Dimension gameSize = null; // e. g. 3 x 5
	private int xTileSize,yTileSize; // length of the side of tile in px, e. g. 160
	//private int widthTileSize;
	private volatile int counter = 0;
	private int orientation;
	private TextView tv;
	private GameTile[][] tiles;
	private GameTile currentTile;
	private MyTimer timer;

	public GameBoard(Dimension gameSize, RelativeLayout scr, int orientation, Context con, Dimension bitmapResolution, MyTimer timer){
		this.gameSize = gameSize;
		this.screen = scr;
		this.context = con;
		this.orientation = orientation;
		this.bitmapResolution = bitmapResolution;
		//If orientatnion is horizontal, we need to flip gameSize.
		if(orientation==ORIENTATION_HORIZONTAL){
			this.gameSize = new Dimension(gameSize.y, gameSize.x);
		}
		
		tiles = new GameTile[this.gameSize.x][this.gameSize.y];
		calculateTileSize();
		this.timer = timer;

	}
	
	public void loadTiles(PuzzleTile[][] res){
		for(int y=0; y<gameSize.y; y++){
			for(int x=0; x<gameSize.x; x++){
				if(res[x][y]==null)
					tiles[x][y] = null; 
				else{
				tiles[x][y] = new GameTile(this.context, res[x][y].getDrawable(), res[x][y].getNumber());
				tiles[x][y].setClickable(true);
				tiles[x][y].setOnClickListener(this);
				tiles[x][y].pos = new Dimension(x,y);
				}
			}
		}
	}	
	
	public void calculateTileSize(){
		
		//Getting screen resolution.
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		//screenResolution = new Dimension(display.getWidth(), display.getHeight());
		Log.d("KAMIL", "Screen resolution: "+ bitmapResolution); // just for testing
		
		//And having the resolution, we can now calculate the size of the tile.
		int xSize = (int) (bitmapResolution.x/gameSize.x);
		int ySize = (int) (bitmapResolution.y/gameSize.y);
		/*if(tSize*gameSize.y>bitmapResolution.y){
			//That means height of the screen is too small to fit all the tiles.
			//In this case tile size needs to be recalculated, taking
			//height as important size, instead of width.
			tSize = (int) (bitmapResolution.y/gameSize.y);
		}*/

		
		//Log.d("KAMIL", "Calculated tile size: "+ tSize);
		xTileSize = xSize;
		yTileSize = ySize;
	}
	
	public void drawBoard(){
		for(int x=0; x<gameSize.x; x++){
			for(int y=0; y<gameSize.y; y++){
				if(tiles[x][y]==null) continue;
				tiles[x][y].params = new LayoutParams(xTileSize, yTileSize);
				Dimension onScreen = getOnScreenCord(new Dimension(x,y));
				tiles[x][y].params.setMargins(onScreen.x, onScreen.y, 0, 0);
				screen.addView(tiles[x][y], tiles[x][y].params);
				
			}
		}
	}
	
	public void reDrawBoard(){
		screen.removeAllViews();
		for(int x=0; x<gameSize.x; x++){
			for(int y=0; y<gameSize.y; y++){
				if(tiles[x][y]==null) continue;
				screen.addView(tiles[x][y], tiles[x][y].params);
				
			}
		}
	}
	
	public GameTile getTile(Dimension at){
		return tiles[at.x][at.y];
	}
	
	public Dimension getTilePos(GameTile t){
		for(int x=0; x<gameSize.x; x++){
			for(int y=0; y<gameSize.y; y++){
				if(tiles[x][y]==null) continue;
				if(t.equals(tiles[x][y])) return new Dimension(x,y);
			}
		}
		return null;
	}
	
	/**
	 * Calculates tile position on screen, given its position on the game board.
	 * @param input
	 * @return
	 */
	public Dimension getOnScreenCord(Dimension input){
		return new Dimension(xTileSize*input.x, yTileSize*input.y);
	}
	
	private boolean canBeMoved(GameTile tile){
		Dimension empty = getEmptyPosition();
		if(tile.pos.x==empty.x){
			if(empty.y==tile.pos.y-1 || empty.y==tile.pos.y+1) return true;
		}
		if(tile.pos.y==empty.y){
			if(empty.x==tile.pos.x-1 || empty.x==tile.pos.x+1) return true;
		}
		return false;
	}
	
	public Dimension getEmptyPosition(){
		
		for(int x=0; x<gameSize.x; x++){
			for(int y=0; y<gameSize.y; y++){
				if(tiles[x][y]==null) {
					//Log.d("KAMIL", "null at: "+new Dimension(x,y));
					return new Dimension(x,y);
				}
			}
		}
		
		throw new RuntimeException("No empty space! Not a sinle cell is null! WTF?");
	
	}
	
	synchronized private void animateTile(GameTile clickedTile, int number ){
		Dimension emptyOnScreen = getOnScreenCord(getEmptyPosition());
		Dimension empty = getEmptyPosition();
		//calculating the translation for animation
		int changeX = 0;
		int changeY = 0;
		if(clickedTile.pos.x==empty.x && empty.y==clickedTile.pos.y-1){
			//empty to the north
			changeY = -yTileSize;
		}else if(clickedTile.pos.x==empty.x && empty.y==clickedTile.pos.y+1){
			//empty to the south
			changeY = yTileSize;
		}else if(clickedTile.pos.y==empty.y && empty.x==clickedTile.pos.x-1){
			//empty to the left
			changeX = -xTileSize;
		}else changeX = xTileSize; // empty to the right
		
		//setting the animation
		TranslateAnimation anim = new TranslateAnimation(0, changeX, 0, changeY);
        anim.setDuration(1000);
        anim.setFillAfter(true);        
        //after animation need to really change a position of tile
        //and change position of the empty space
        
        
        //and the animation starts!
        currentTile = clickedTile;
        Log.d("KAMIL", "Animation starts! ("+ counter +")");
        anim.setAnimationListener(new TileAnimationListener(clickedTile, this));
        //reDrawBoard();
        clickedTile.startAnimation(anim);
	}
	
	
	//when a tile is clicked
	synchronized public void onClick(View v) {
		counter++;
		PuzzleActivity.totalStep = counter;
//		PuzzleActivity.totalStep -=1;
//		tv.setText(PuzzleActivity.totalStep+"");
//		if (PuzzleActivity.totalStep == 0) {
//			MyDialog dialog = new MyDialog(context,tv,1);
//			dialog.setCancelable(false);
//			dialog.show(((FragmentActivity)context).getSupportFragmentManager(),"");
//		}
		Log.d("KAMIL", "onClick method number " + counter + " was called.");
		
		GameTile clickedTile = (GameTile) v;
		if(!canBeMoved(clickedTile)){
			//Log.d("KAMIL", "cannot move the tile ("+ counter +")");

			return;
		}
		//Log.d("KAMIL", "Tile " + clickedTile.pos + " clicked, it will move. (" + counter +")");
		//animateTile(clickedTile, counter);
		moveTileToEmpty(clickedTile);
		
		if(isSolved()){
            timer.cancel();
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("You solved the puzzle! Congratulations!\n" + "Your step is: " + PuzzleActivity.totalStep )
			       .setCancelable(false)
			       .setPositiveButton("Thanks.", new DialogInterface.OnClickListener() {
					   @Override
					   public void onClick(DialogInterface dialogInterface, int i) {
						   ((Activity)context).finish();
					   }
				   });
			AlertDialog alert = builder.create();
			alert.show();
		}
        
	}
	
	synchronized public void moveTileToEmpty(GameTile toMove){
		Dimension empty = getEmptyPosition(); // getting pos of the empty spot before change
		Dimension destOnScreen = getOnScreenCord(getEmptyPosition());
		tiles[toMove.pos.x][toMove.pos.y]=null; // old place
		tiles[empty.x][empty.y] = toMove; //new place
		
		
		toMove.pos = empty;
		
		//screen.removeView(toMove);
		toMove.params.setMargins(destOnScreen.x, destOnScreen.y, 0, 0);
		toMove.setClickable(true);
		toMove.setOnClickListener(this);
		//screen.addView(toMove, toMove.params);
		reDrawBoard();

	}

	private boolean isSolved(){
		int n = 0;
		for(int y=0; y<gameSize.y; y++){
			for(int x=0; x<gameSize.x; x++){
				if(tiles[x][y]==null){
					if(x==gameSize.x-1 && y==gameSize.y-1)
						return true;
					else return false;
				}
				if(tiles[x][y].getCheckNumber()!=n) return false; 
				n++;
			}
		}
		return true;
	}
	

	public Dimension getGameSize() {
		return gameSize;
	}

	public int getxTileSize() {
		return xTileSize;
	}

	public int getyTileSize() {
		return yTileSize;
	}

	public RelativeLayout getScreen() {
		return screen;
	}

	public void setScreen(RelativeLayout screen) {
		this.screen = screen;
	}

	public Dimension getScreenResolution() {
		return bitmapResolution;
	}

	public void setScreenResolution(Dimension screenResolution) {
		this.bitmapResolution = screenResolution;
	}

	public void setGameSize(Dimension gameSize) {
		this.gameSize = gameSize;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	
	
}
