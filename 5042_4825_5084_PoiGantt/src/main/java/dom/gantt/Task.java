package dom.gantt;

import java.util.Collections;
import java.util.List;

public class Task extends TaskAbstract {
	private int taskStart;
	private int taskEnd;
	private double cost;
	private double effort;
	private boolean simple;
	private List<TaskAbstract> subtasks;
	
	public Task(int taskId, String taskText, int containerTaskId, int taskStart, int taskEnd, double cost, double effort, boolean simple, List<TaskAbstract> subtasks) {
		super(taskId, taskText, containerTaskId);
		this.taskStart = taskStart;
		this.taskEnd = taskEnd;
		this.cost = cost;
		this.effort = effort;
		this.simple = simple;
		Collections.sort(subtasks);
		this.subtasks = subtasks;
	}
	
	public Task(int taskId, String taskText, int containerTaskId, int taskStart, int taskEnd, double cost, double effort, boolean simple) {
		super(taskId, taskText, containerTaskId);
		this.taskStart = taskStart;
		this.taskEnd = taskEnd;
		this.cost = cost;
		this.effort = effort;
		this.simple = simple;
	}

	@Override
	public int getTaskStart() {
		return taskStart;
	}

	@Override
	public int getTaskEnd() {
		return taskEnd;
	}

	@Override
	public double getCost() {
		return cost;
	}

	@Override
	public double getEffort() {
		return effort;
	}

	@Override
	public boolean isSimple() {
		return simple;
	}

	@Override
	public List<TaskAbstract> getSubtasks() {
		return subtasks;
	}

}
