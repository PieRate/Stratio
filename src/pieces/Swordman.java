package pieces;

import control.Player;

public class Swordman extends Unit implements Infantry {
	static protected int goldCost = 100;
	static protected int gemCost = 0;
	
	public Swordman (Player player){
		this.maxHealth = 100;
		this.HP = 100;
		this.armour = 0;
		this.damage = 10;
		this.attackSpeed = 1;
		this.radius = 1;
		this.name = "Swordman";
		this.player = player;
		this.consTime = 10;
	}
}
