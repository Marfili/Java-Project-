package load;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import dom.gantt.TaskAbstract;
import dom.gantt.Task;
import util.FileTypes;

public class LoadText implements ILoad {
	private String separetor;
	private List<TaskAbstract> topTasks;
	
	public LoadText(FileTypes type) {
		topTasks = new ArrayList<>();
		
		if(type == FileTypes.CSV) {
			separetor = ",";
		} else if(type == FileTypes.CSV_EU) {
			separetor = ";";
		} else if(type == FileTypes.TSV) {
			separetor = "\t";
		} else {
			separetor = " ";
		}
	}

	public List<TaskAbstract> importFile(String path) {
		Path path2 = Paths.get(path);
        try {
            List<String> lines = Files.readAllLines(path2);
            
            for (String line: lines){
            	
            	String[] split = line.split(separetor);
            	
            	if (split.length == 3) {
            		int min = Integer.MAX_VALUE;
            		int max = Integer.MIN_VALUE;
            		double sumCost = 0;
            		double sumEffort = 0;
            		List<TaskAbstract> subtasks = new ArrayList<>();
            		
            		for (String task: lines){
            			String[] split2 = task.split(separetor);
            			if(split2[2].equals(split[0])) {
            				if(Integer.parseInt(split2[3]) < min) {
            					min = Integer.parseInt(split2[3]);
            				}
            				
            				if(Integer.parseInt(split2[4]) > max) {
            					max = Integer.parseInt(split2[4]);
            				}
            				
            				sumCost+= Double.parseDouble(split2[5]);
            				sumEffort+= Double.parseDouble(split2[6]);
            				
            				int taskId = Integer.parseInt(split2[0]);
                			int mamaId = Integer.parseInt(split2[2]);
                			int start = Integer.parseInt(split2[3]);
                			int end = Integer.parseInt(split2[4]);
                			double cost = Double.parseDouble(split2[5]);
                			double effort = Double.parseDouble(split2[6]);
                			subtasks.add(new Task(taskId, split2[1], mamaId, start, end, cost, effort, true));
            			}
            		}
            		
            		int taskId = Integer.parseInt(split[0]);
        			int mamaId = Integer.parseInt(split[2]);
            		topTasks.add(new Task(taskId, split[1], mamaId, min, max, sumCost, sumEffort, false, subtasks));
            		
            	} else if (split[2].equals("0")) {
            		int taskId = Integer.parseInt(split[0]);
        			int mamaId = Integer.parseInt(split[2]);
        			int start = Integer.parseInt(split[3]);
        			int end = Integer.parseInt(split[4]);
        			double cost = Double.parseDouble(split[5]);
        			double effort = Double.parseDouble(split[6]);
            		topTasks.add(new Task(taskId, split[1], mamaId, start, end, cost, effort, false));
            	}
            }
            
            return topTasks;
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
		return null;
	}
}
