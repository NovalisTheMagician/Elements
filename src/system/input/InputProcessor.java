package system.input;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import system.GameComponent;

public class InputProcessor implements GameComponent
{
	static public enum InputAction
	{
		CONTINUOUS,
		ONCE
	};
	
	static public enum InputState
	{
		PRESSED(true),
		RELEASED(false);
		
		private boolean stateVal;
		
		InputState(boolean state)
		{
			stateVal = state;
		}
		
		public boolean toBool()
		{
			return stateVal;
		}
	};
	
	static public enum InputType
	{
		KEYBOARD,
		MOUSE
	};
	
	static public enum InputKey
	{
		// TODO add all Keyboard and Mouse keys this game should be able to handle
	}
	
	static public class Input
	{
		public InputType type;
		public int key;				// TODO change to InputKey type
		public InputState state;
		public InputAction action;
		public int duration;		// TODO implement duration of action
	};
	
	private int lastMouseX, lastMouseY;
	private int currMouseX, currMouseY;
	private int mouseDX, mouseDY;
	
	private boolean[] kbCurrState;
	private boolean[] kbPrevState;
	private boolean[] msCurrState;
	private boolean[] msPrevState;
	
	@Override
	public boolean initialize() 
	{
		kbCurrState = new boolean[Keyboard.KEYBOARD_SIZE];
		kbPrevState = new boolean[Keyboard.KEYBOARD_SIZE];
		
		msCurrState = new boolean[5];
		msPrevState = new boolean[5];
		
		return true;
	}

	@Override
	public void update(float unused) 
	{
		currMouseX = Mouse.getX();
		currMouseY = Mouse.getY();
		
		mouseDX = currMouseX - lastMouseX;
		mouseDY = currMouseY - lastMouseY;
		
		lastMouseX = currMouseX;
		lastMouseY = currMouseY;
		
		System.arraycopy(kbCurrState, 0, kbPrevState, 0, kbCurrState.length);
		System.arraycopy(msCurrState, 0, msPrevState, 0, msCurrState.length);
		
		while(Keyboard.next())
		{
			int key = Keyboard.getEventKey();
			boolean state = Keyboard.getEventKeyState();
			kbCurrState[key] = state;
		}
		
		while(Mouse.next())
		{
			int key = Mouse.getEventButton();
			if(key == -1)
				continue;
			boolean state = Mouse.getEventButtonState();
			msCurrState[key] = state;
		}
	}

	public boolean isInput(Input input)
	{
		return isInput(new Input[] {input});
	}
	
	public boolean isInput(Input[] inputs)
	{
		boolean result = true;
		
		for(Input input : inputs)
		{	
			boolean inputCurrBuffer[] = input.type == InputType.KEYBOARD ? kbCurrState : msCurrState;
			boolean inputPrevBuffer[] = input.type == InputType.KEYBOARD ? kbPrevState : msPrevState;
			
			if(input.action == InputAction.CONTINUOUS)
			{
				result &= inputCurrBuffer[input.key] == input.state.toBool();
			}
			else if(input.action == InputAction.ONCE)
			{
				result &= inputCurrBuffer[input.key] == input.state.toBool() && inputPrevBuffer[input.key] == !input.state.toBool();
			}
		}
		
		return result;
	}
	
	public int getMouseX()
	{
		return currMouseX;
	}
	
	public int getMouseY()
	{
		return currMouseY;
	}
	
	public int getMouseDX()
	{
		return mouseDX;
	}
	
	public int getMouseDY()
	{
		return mouseDY;
	}
	
	@Override
	public void destroy() 
	{
		
	}
	
}
