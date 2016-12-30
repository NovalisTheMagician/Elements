package system.loader;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import system.Config;

public class AssetManager 
{
	public static enum AssetType
	{
		TEXTURE,
		MESH,
		SHADER,
		MATERIAL
	}
	
	private Map<AssetType, Map<String, Object>> assets;
	private Config config;
	
	public AssetManager(Config config)
	{
		this.config = config;
		assets = new HashMap<>();
	}
	
	public Object get(AssetType type, String identifier)
	{
		
	}
}
