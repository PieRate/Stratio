package pieces;

import java.util.ArrayList;

import control.Player;

public class Barrack extends Structure{
	
	public Barrack (Player player){
		this.maxHealth = 1000;
		this.HP = 1000;
		this.armour = 3;
		this.damage = 0;
		this.attackSpeed = 0;
		this.radius = 4;
		this.name = "Barrack";
		this.player = player;
	}
	
	/**
	 * Creates a swordman unit
	 */
	public void produce1(){
		if(player.getGold() < Swordman.goldCost){
			//TODO: Replace this with in-game error message
			System.out.println("Need more gold");
		} else if (player.getGem() < Swordman.gemCost){
			//TODO: Replace this with in-game error message
			System.out.println("Need more gem");
		} else {
			player.spendGold(Swordman.gemCost);
			player.spendGem(Swordman.gemCost);
			player.units.add(new Swordman(player));
		}
	}
}