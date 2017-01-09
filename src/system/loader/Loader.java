package system.loader;

import java.io.InputStream;

public interface Loader<AssetType> 
{
	AssetType load(InputStream in, String workingDir);
}
