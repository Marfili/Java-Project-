package load;

import java.util.List;

import dom.gantt.TaskAbstract;

public interface ILoad {
	public List<TaskAbstract> importFile(String path);
}
