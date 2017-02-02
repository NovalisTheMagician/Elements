package system.input;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputProcessor
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
		
		public Input()
		{
			this(InputType.KEYBOARD, 0);
		}
		
		public Input(InputType type, int key)
		{
			this(type, key, InputState.PRESSED);
		}
		
		public Input(InputType type, int key, InputState state)
		{
			this(type, key, state, InputAction.CONTINUOUS);
		}
		
		public Input(InputType type, int key, InputState state, InputAction action)
		{
			this(type, key, state, action, 0);
		}
		
		public Input(InputType type, int key, InputState state, InputAction action, int duration)
		{
			this.type = type;
			this.key = key;
			this.state = state;
			this.action = action;
			this.duration = duration;
		}
	};
	
	private boolean[] kbCurrState;
	private boolean[] kbPrevState;
	private boolean[] msCurrState;
	private boolean[] msPrevState;
	
	public InputProcessor() 
	{
		kbCurrState = new boolean[Keyboard.KEYBOARD_SIZE];
		kbPrevState = new boolean[Keyboard.KEYBOARD_SIZE];
		
		msCurrState = new boolean[5];
		msPrevState = new boolean[5];
	}

	public void update() 
	{	
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

	public void setMouseRelativeMode(boolean mode)
	{
		Mouse.setGrabbed(mode);
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
			boolean inputCurrBuffer[] = getInputBuffer(input.type, true);
			boolean inputPrevBuffer[] = getInputBuffer(input.type, false);
			
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
	
	private boolean[] getInputBuffer(InputType byType, boolean curr)
	{
		switch(byType)
		{
		case KEYBOARD: return curr ? kbCurrState : kbPrevState;
		case MOUSE: return curr ? msCurrState : msPrevState;
		}
		//impossible to get here, but java complains anyway
		return new boolean[0];
	}
	
	public int getMouseX()
	{
		return Mouse.getX();
	}
	
	public int getMouseY()
	{
		return Mouse.getY();
	}
	
	public int getMouseDX()
	{
		return Mouse.getDX();
	}
	
	public int getMouseDY()
	{
		return Mouse.getDY();
	}
}
