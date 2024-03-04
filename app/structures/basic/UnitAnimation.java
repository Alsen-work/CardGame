package structures.basic;


public class UnitAnimation {

	int[] frameStartEndIndices;
	int fps;
	boolean loop;
	
	public UnitAnimation() {}

	public UnitAnimation(int[] frameStartEndIndices, int fps, boolean loop) {
		super();
		this.frameStartEndIndices = frameStartEndIndices;
		this.fps = fps;
		this.loop = loop;
	}

	public int[] getFrameStartEndIndices() {
		return frameStartEndIndices;
	}

	public void setFrameStartEndIndices(int[] frameStartEndIndices) {
		this.frameStartEndIndices = frameStartEndIndices;
	}

	public int getFps() {
		return fps;
	}

	public void setFps(int fps) {
		this.fps = fps;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	};
	
	
	
	
	
	
}
