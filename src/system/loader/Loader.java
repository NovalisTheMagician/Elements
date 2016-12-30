package system.loader;

import java.io.InputStream;

public interface Loader<AssetType> 
{
	AssetType Load(InputStream in, String workingDir);
}
