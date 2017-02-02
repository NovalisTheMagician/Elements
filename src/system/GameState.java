package system;

public interface GameState 
{
	public void enter();
	
	public void update(float delta);
	public void draw();
	
	public void leave();
}
