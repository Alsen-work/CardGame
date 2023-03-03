package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;

/**
 * A generic event processor interface, implemented by all classes that process events
 * sent from the user interface. 
 * @author Dr. Richard McCreadie
 *  * 一个通用的事件处理器接口，由所有处理事件的类来实现
 *  * 从用户界面发送的事件。
 *  * @作者 Richard McCreadie 博士
 *
 */
public interface EventProcessor {

	/**
	 * The processEvent method takes as input the contents of the event in the form of a
	 * Jackson JsonNode object, which contains a set of key-value pairs (the information
     * about the event). It also takes in a copy of an ActorRef object, which can be used
     * to send commands back to the front-end, and a reference to the GameState class,
     * which as the name suggests can be used to hold game state information.
	 * @param message
	 * @return
	 * processEvent方法将事件的内容作为输入，其形式是一个
	 * 	 * Jackson JsonNode对象，它包含一组键值对（关于事件的信息
	 *      * 关于该事件的信息）。它还接收一个ActorRef对象的副本，它可以用来
	 *      * 用于向前端发送命令，以及一个对GameState类的引用。
	 *      * 顾名思义，它可以用来保存游戏状态信息。
	 * 	 * @param message
	 * 	 * @return
	 *          */

	public void processEvent(ActorRef out, GameState gameState, JsonNode message);
	
}
