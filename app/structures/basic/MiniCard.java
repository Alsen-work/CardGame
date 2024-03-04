package structures.basic;

public class MiniCard {

	String[] cardTextures;
	String[] animationFrames;
	int fps;
	int index;
	
	public MiniCard() {}

	public MiniCard(String[] cardTextures, String[] animationFrames, int fps, int index) {
		super();
		this.cardTextures = cardTextures;
		this.animationFrames = animationFrames;
		this.fps = fps;
		this.index = index;
	}

	public String[] getCardTextures() {
		return cardTextures;
	}

	public void setCardTextures(String[] cardTextures) {
		this.cardTextures = cardTextures;
	}

	public String[] getAnimationFrames() {
		return animationFrames;
	}

	public void setAnimationFrames(String[] animationFrames) {
		this.animationFrames = animationFrames;
	}

	public int getFps() {
		return fps;
	}

	public void setFps(int fps) {
		this.fps = fps;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	
	
	
	
}
