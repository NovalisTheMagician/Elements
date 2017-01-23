package system;

public interface GameComponent 
{
	boolean initialize();
	void update(float delta);
	void destroy();
}
