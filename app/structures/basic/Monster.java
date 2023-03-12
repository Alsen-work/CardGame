package structures.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import structures.basic.abilities.Ability;

import java.util.ArrayList;

public abstract class Monster extends Unit{


    @JsonIgnore
    protected static ObjectMapper mapper = new ObjectMapper(); // Jackson Java Object Serializer, is used to read java objects from a file

    // Monster attributes
    public String 					name;
    protected int 					HP;				// Monster's current health value, between 0 - maxHP
    protected int 					maxHP;			// Maximum value a Monster's HP can be
    protected int 					attackValue; 	// How much damage Monster does in one attack action
    protected ArrayList <Ability>	abilities;		// Any abilities the Monster has

    // Action values
    protected int 			movesLeft;				// number of move actions Monster has left, tracks directly to range
    protected int			movesMax;				// max number of move actions Monster can use per turn, reset by Cooldown
    protected int 			attacksLeft;			// number of attack actions Monster has 'leftover', != range
    protected int			attacksMax;				// max number of attack actions a Monster can use per turn, reset by Cooldown
    protected int 			attackRange;			// integer range of tiles (in all directions) for attacks

    // Gameplay info
    protected Player			owner;				// Player who owns the unit
    protected boolean			Cooldown;			// Tracks when the unit is capable of actions, where true == able
    protected boolean			provoked;			// Flag to tell if monster should use a altered act range
    protected EffectAnimation	abAnimation;		// EffectAnimation reference for abilities requiring effects that

    /* Constructor(s) */

    // Default constructor for JSON
    public Monster() {
        super();

        this.movesLeft = 0;
        this.movesMax = 2;
        this.attacksLeft = 0;
        this.attacksMax = 1;
        this.attackRange = 1;

        this.abilities = null;

        this.Cooldown = true;
        this.provoked = false;


    }

    public Monster(int id, UnitAnimationSet animations, ImageCorrection correction) {

        // Specify id, UnitAnimationSet, ImageCorrection and/or Tile
        super(id, animations, correction);

    }

    // Move
    // Returns the outcome of an attempt to move (successful or not) and updates move/location variables
    public boolean move(Tile t) {
        if(movesLeft > 0 && !(Cooldown)) {

            // Check change in Board dimension indices from current to t
            int xchange = Math.abs(this.getPosition().getTilex() - t.getTilex());
            int ychange = Math.abs(this.getPosition().getTiley() - t.getTiley());
            // Move fails if total change exceeds ability to move
            if(xchange + ychange > movesLeft) {	return false;	}

            movesLeft -= (xchange+ychange);
            this.setPositionByTile(t);

        } else {	return false;	}

        return true;
    }

    // Attack
    // Returns the outcome of an attempt to attack (successful or not) and updates attack variables
    public boolean attack() {
        // Check if Monster is able to attack
        if(this.Cooldown) {
            return false;
        }
        this.attacksLeft -= 1;
        if(this.attacksLeft == 0) {
            this.toggleCooldown();
        }
        return true;
    }

    // Counter-attack
    // Logic-related method name for retrieving attackValue of unit, called after surviving an attack.
    // Counter is not related to attack actions available.
    public int counter() {
        return attackValue;
    }

    // Receive damage from any source
    // Returns outcome of receiving damage (death if false) and updates health
    public boolean getDamage(int d) {
        if(this.HP - d <= 0) {
            this.HP = 0;
            return false;
        } else {
            this.HP -= d;
            return true;
        }
    }

    // Overide monster defend
    public boolean defend(int d) {
        if(this.HP - d <= 0) {
            this.HP = 0;
            return false;
        } else {
            this.HP -= d;
            return true;
        }
    }
    // Heal (gain health)
    // Returns the outcome of attempting to heal and updates health
    public boolean heal(int h) {
        if(this.HP == this.maxHP)	{	return false;	}
        if(this.HP + h > this.maxHP) {
            this.HP = this.maxHP;
        }
        else {
            this.HP += h;
        }
        return true;
    }

    // Buff (gain attack value)
    // Adjusts attackValue statistic from a buff action
    public void buffAttack(int b) {
        this.attackValue += b;
    }




    /**		Getters and setters		**/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int hP) {
        HP = hP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int h) {
        this.maxHP = h;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player p) {
        owner = p;
    }


    // Moves-related

    public int getMovesLeft() {
        return movesLeft;
    }

    public void setMovesLeft(int m) {
        this.movesLeft = m;
    }

    public int getMovesMax() {
        return movesMax;
    }

    public void setMovesMax(int mmx) {
        this.movesMax = mmx;
    }


    // Attack-related

    public int getAttackValue() {
        return attackValue;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }

    public int getAttacksLeft() {
        return attacksLeft;
    }

    public void setAttacksLeft(int a) {
        this.attacksLeft = a;
    }

    public int getAttacksMax() {
        return attacksMax;
    }

    public void setAttacksMax(int mx) {
        this.attacksMax = mx;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int a) {
        this.attackRange = a;
    }


    // Cooldown management

    // Indicates a Monster can no longer move & attack (if true)
    public boolean getOnCooldown() {
        return Cooldown;
    }

    // Mostly used only in testing
    public void setCooldown(boolean b) {
        this.Cooldown = b;
    }

    // Switches cooldown status and related action variables
    public void toggleCooldown() {
        this.Cooldown = !Cooldown;
        this.actionSet();
    }

    // Helper for cooldown management
    private void actionSet() {
        if(Cooldown) {
            this.movesLeft = 0;
            this.attacksLeft = 0;
        } else {
            this.movesLeft = this.movesMax;
            this.attacksLeft = this.attacksMax;
        }
    }

    public boolean hasAbility() {
        if(this.abilities != null) {
            return true;
        }
        return false;
    }

    public ArrayList <Ability> getMonsterAbility() {
        return abilities;
    }

    public void setAbility(ArrayList <Ability> abs) {
        abilities = abs;
    }

    public EffectAnimation getAbAnimation() {
        if(this.abAnimation != null) {
            return this.abAnimation;
        }
        return null;
    }

    public void setAbAnimation(EffectAnimation e) {
        this.abAnimation = e;
    }


    // Provoked status and range impairment

    public boolean isProvoked() {
        return this.provoked;
    }

    public void setProvoked(boolean value) {
        provoked = value;
    }

    public void toggleProvoked() {
        this.provoked = !provoked;
    }

    // Flag to indicate movement/attack range is impaired by an ability (if t
    public boolean hasActionRangeImpaired() {
        // Switch vairable
        boolean impariedActionRangeFlag = false;

        // List of potential debuffs (debuffs would likely be objects if there was more than 1)
        if (provoked) {
            impariedActionRangeFlag = true;
        }
        return impariedActionRangeFlag;
    }

}