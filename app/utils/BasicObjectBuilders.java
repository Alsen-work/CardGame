package utils;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import structures.basic.Card;
import structures.basic.EffectAnimation;
import structures.basic.Tile;
import structures.basic.Unit;

/**
 * This class contains methods for producing basic objects from configuration files
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class BasicObjectBuilders {

	@JsonIgnore
	protected static ObjectMapper mapper = new ObjectMapper(); // Jackson Java Object Serializer, is used to read java objects from a file
	
	/**
	 * This class produces a Card object (or anything that extends Card) given a configuration
	 * file. Configuration files can be found in the conf/gameconfs directory. The card should
	 * be given a unique id number. The classtype field specifies the type of Card to be
	 * constructed, e.g. Card.class will create a default card object, but if you had a class
	 * extending card, e.g. MyAwesomeCard that extends Card, you could also specify
	 * MyAwesomeCard.class here. If using an extending class you will need to manually set any
	 * new data fields.
	 * 这个类产生一个Card对象（或任何扩展Card的东西），给定一个配置文件。
	 * 	 *文件。配置文件可以在conf/gameconfs目录下找到。该卡应该
	 * 	 * 被赋予一个唯一的ID号码。classtype字段指定了要构建的Card的类型。
	 * 	 * 例如，Card.class将创建一个默认的卡片对象，但如果你有一个类
	 * 	 * 扩展Card，例如MyAwesomeCard扩展了Card，你也可以指定
	 * 	 * MyAwesomeCard.class在这里。如果使用一个扩展类，你将需要手动设置任何
	 * 	 * 新的数据字段。
	 * @param configurationFile
	 * @param id
	 * @param classtype
	 * @return
	 */
	public static Card loadCard(String configurationFile, int id, Class<? extends Card> classtype) {
		try {
			Card card = mapper.readValue(new File(configurationFile), classtype);
			card.setId(id);
			return card;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}
	
	/**
	 * This class produces a EffectAnimation object given a configuration
	 * file. Configuration files can be found in the conf/gameconfs directory.
	 * 该类产生一个EffectAnimation对象，给定一个配置文件。
	 * 	 *文件。配置文件可以在conf/gameconfs目录下找到。
	 * 	 *@param configurationFile
	 * @param configurationFile
	 * @return
	 */
	public static EffectAnimation loadEffect(String configurationFile) {
		try {
			EffectAnimation effect = mapper.readValue(new File(configurationFile), EffectAnimation.class);
			return effect;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}
	
	/**
	 * Loads a unit from a configuration file. Configuration files can be found 
	 * in the conf/gameconfs directory. The unit needs to be given a unique identifier
	 * (id). This method requires a classtype argument that specifies what type of
	 * unit to create.
	 * 从一个配置文件中加载一个单元。配置文件可以找到
	 * 	 * 在conf/gameconfs目录下。该单元需要被赋予一个唯一的标识符
	 * 	 * (id)。这个方法需要一个classtype参数，指定要创建什么类型的
	 * 	 * 要创建的单元。
	 * @param configFile
	 * @return
	 */
	public static Unit loadUnit(String configFile, int id,  Class<? extends Unit> classType) {
		
		try {
			Unit unit = mapper.readValue(new File(configFile), classType);
			unit.setId(id);
			return unit;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
		
	}
	
	/**
	 * Generates a tile object with x and y indices
	 * 生成一个具有X和Y指数的瓷砖对象
	 * @param x
	 * @param y
	 * @return
	 */
	public static Tile loadTile(int x, int y) {
		int gridmargin = 5;
		int gridTopLeftx = 410;
		int gridTopLefty = 280;
		
		Tile tile = Tile.constructTile(StaticConfFiles.tileConf);
		tile.setXpos((tile.getWidth()*x)+(gridmargin*x)+gridTopLeftx);
		tile.setYpos((tile.getHeight()*y)+(gridmargin*y)+gridTopLefty);
		tile.setTilex(x);
		tile.setTiley(y);
		
		return tile;
		
	}
	
}
