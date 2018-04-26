package control;

import java.util.ArrayList;

import pieces.*;

public class Player {
	public ArrayList<Structure> structures;
	public ArrayList<Unit> units;
	//Corpse and ruins stores dead pieces
	public ArrayList<Structure> ruins;
	public ArrayList<Unit> corpses;
	
	protected int gold;	//Gold will be the primary resource in the game
	protected int gem;		//Gem will be the secondary resource in the game
	
	//Getter and setter methods for the resources
	public int getGold(){
		return gold;
	}
	public int getGem(){
		return gem;
	}
	public void gainGold(int change){
		gold += change;
	}
	public void gainGem(int change){
		gem += change;
	}
	public void spendGold(int change){
		gold -= change;
	}
	public void spendGem(int change){
		gem -= change;
	}
	
}
