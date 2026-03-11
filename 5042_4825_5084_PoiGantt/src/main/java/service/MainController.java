package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dom.gantt.TaskAbstract;
import dom.gantt.Task;
import load.ILoad;
import load.LoadFactory;
import output.IntermediateStateExcel;
import output.NewStyledSheetExcel;
import util.FileTypes;
import util.ProjectInfo;

public class MainController implements IMainController {
	private int countTasks;
	private String sourcePath;
	private FileTypes fileType;
	private ProjectInfo fileInfo;
	private List<TaskAbstract> allTasks;
	private NewStyledSheetExcel newStyledSheet = new NewStyledSheetExcel();
	
	public List<String> load(String sourcePath, FileTypes filetype) {
		this.sourcePath = sourcePath;
		ILoad load = LoadFactory.createLoad(filetype);
		allTasks = load.importFile(sourcePath);
		Collections.sort(allTasks);
		
		List<String> print = new ArrayList<>();
		for(TaskAbstract topTask: allTasks) {
			print.add(topTask.toString());
						
			for(TaskAbstract subtask: topTask.getSubtasks()) {
				print.add(subtask.toString());
			}
		}
		
		countTasks = print.size();
		return print;
	}
	
	public ProjectInfo prepareTargetWorkbook(FileTypes fileType, String targetPath) {
		fileInfo = new ProjectInfo("Gantt Diagrams", sourcePath, targetPath, countTasks, getTopLevelTasksOnly().size());
		this.fileType = fileType;
		return fileInfo;
	}

	public List<TaskAbstract> getAllTasks() {
		List<TaskAbstract> tasks = new ArrayList<>();
		
		for(TaskAbstract task: allTasks) {
			tasks.add(new Task(task.getTaskId(), task.getTaskText(), task.getContainerTaskId(), task.getTaskStart(), task.getTaskEnd(), task.getCost(), task.getEffort(), task.isSimple()));
			
			for(TaskAbstract subtask: task.getSubtasks()) {
				tasks.add(new Task(subtask.getTaskId(), subtask.getTaskText(), subtask.getContainerTaskId(), subtask.getTaskStart(), subtask.getTaskEnd(), subtask.getCost(), subtask.getEffort(), subtask.isSimple()));
			}
		}
		return tasks;
	}
	
	public List<TaskAbstract> getTopLevelTasksOnly() {
		List<TaskAbstract> topTasks = new ArrayList<>();
		for(TaskAbstract topTask: allTasks) {
			topTasks.add(new Task(topTask.getTaskId(), topTask.getTaskText(), topTask.getContainerTaskId(), topTask.getTaskStart(), topTask.getTaskEnd(), topTask.getCost(), topTask.getEffort(), topTask.isSimple()));
		}
		return topTasks;
	}
	
	public List<TaskAbstract> getTasksInRange(int firstIncluded, int lastIncluded) {
		List<TaskAbstract> tasks = new ArrayList<>();
		
		for(TaskAbstract task: allTasks) {
			if(task.getTaskId() >= firstIncluded && task.getTaskId() <= lastIncluded) {
				tasks.add(new Task(task.getTaskId(), task.getTaskText(), task.getContainerTaskId(), task.getTaskStart(), task.getTaskEnd(), task.getCost(), task.getEffort(), task.isSimple()));
			}
			
			for(TaskAbstract subtask: task.getSubtasks()) {
				if(subtask.getTaskId() >= firstIncluded && subtask.getTaskId() <= lastIncluded) {
					tasks.add(new Task(subtask.getTaskId(), subtask.getTaskText(), subtask.getContainerTaskId(), subtask.getTaskStart(), subtask.getTaskEnd(), subtask.getCost(), subtask.getEffort(), subtask.isSimple()));
				}
			}
		}
		return tasks;
	}
	
	public boolean rawWriteToExcelFile(List<TaskAbstract> tasks) {
		IntermediateStateExcel excel = new IntermediateStateExcel();
		return excel.writeToExcel(tasks, fileInfo, fileType);
	}
	
	public String addFontedStyle(String styleName, short styleFontColor, short styleFontHeightInPoints,
			String styleFontName, boolean styleFontBold, boolean styleFontItalic, boolean styleFontStrikeout,
			short styleFillForegroundColor, String styleFillPatternString, String HorizontalAlignmentString,
			boolean styleWrapText) {
		return "";
	}
	
	public boolean createNewSheet(String sheetName, List<TaskAbstract> tasks, String headerStyleName,
			String topBarStyleName, String topDataStyleName, String nonTopBarStyleName, String nonTopDataStyleName,
			String normalStyleName) {
		return newStyledSheet.createStylesSheet(tasks, fileInfo, fileType, sheetName, headerStyleName, topBarStyleName, topDataStyleName, nonTopBarStyleName, nonTopDataStyleName, normalStyleName);
	}

}
