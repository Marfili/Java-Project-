package load;

import util.FileTypes;

public class LoadFactory {
	public static ILoad createLoad(FileTypes type) {
		if(type == FileTypes.CSV || type == FileTypes.CSV_EU || type == FileTypes.TSV) {
			return new LoadText(type);
		} else {
			return new LoadExcel(type);
		}
	}
}
