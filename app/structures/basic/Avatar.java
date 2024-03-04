package structures.basic;

public class Avatar extends Monster {
	

	public Avatar() {
		super();
		this.avatarSetUp();
	}

	public void avatarSetUp() {

		this.HP = 20;
		this.maxHP = 20;
		this.attackValue = 2;
		this.movesLeft = 2;			
		this.attacksLeft = 1;		
		this.attacksMax = 1;		
		this.attackRange = 1;		
		this.onCooldown = false;
	}
	
	public void setOwner(Player p) {
		this.owner = p;
	}

	@Override
	public boolean getDamage(int d) {
		if(this.HP - d <= 0) {
			this.HP = 0;
			this.getOwner().setHealth(this.HP);
			return false;
		} else {
			this.HP -= d;
			this.getOwner().setHealth(this.HP);
			return true;
		}
	}
	
	// Override monster heal
	@Override
	public boolean heal(int h) {
		if(this.HP == this.maxHP)	{	return false;	}	
		if(this.HP + h > this.maxHP) {
			this.HP = this.maxHP;
			this.getOwner().setHealth(h);
		}
		else {
			this.HP += h;
			this.getOwner().setHealth(h);
		}
		return true;
	}
	
}
