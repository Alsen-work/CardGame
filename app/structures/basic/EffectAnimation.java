package structures.basic;

import java.util.List;

public class EffectAnimation {

	List<String> animationTextures;
	ImageCorrection correction;
	int fps;
	
	public EffectAnimation() {}
	
	public EffectAnimation(List<String> animationTextures, ImageCorrection correction, int fps) {
		super();
		this.animationTextures = animationTextures;
		this.correction = correction;
		this.fps = fps;
	}
	public List<String> getAnimationTextures() {
		return animationTextures;
	}
	public void setAnimationTextures(List<String> animationTextures) {
		this.animationTextures = animationTextures;
	}
	public ImageCorrection getCorrection() {
		return correction;
	}
	public void setCorrection(ImageCorrection correction) {
		this.correction = correction;
	}
	public int getFps() {
		return fps;
	}
	public void setFps(int fps) {
		this.fps = fps;
	}
	
	
}
