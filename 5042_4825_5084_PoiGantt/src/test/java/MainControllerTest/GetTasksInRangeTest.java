package MainControllerTest;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import dom.gantt.TaskAbstract;
import service.MainController;
import util.FileTypes;

public class GetTasksInRangeTest {
	@Test
	public void T4_V0_HappyDay() {
		MainController controller = new MainController();
		controller.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		List<TaskAbstract> tasksInRange = controller.getTasksInRange(101,202);
		
		assertEquals(4, tasksInRange.size());
		assertEquals("TaskId: 	101	Text: 	Visit all the closets to see what's missing	Mama: 	100	Start: 	1	End: 	5	Cost: 	50.0	Effort: 	5.0", tasksInRange.get(0).toString());
		assertEquals("TaskId: 	102	Text: 	Write down what you need to buy	Mama: 	100	Start: 	1	End: 	6	Cost: 	10.0	Effort: 	6.0", tasksInRange.get(1).toString());
		assertEquals("TaskId: 	200	Text: 	Proceed to the super market	Mama: 	0	Start: 	7	End: 	9	Cost: 	10.0	Effort: 	5.0", tasksInRange.get(2).toString());
		assertEquals("TaskId: 	201	Text: 	Drive the car	Mama: 	200	Start: 	7	End: 	9	Cost: 	10.0	Effort: 	5.0", tasksInRange.get(3).toString());
	}
	
	@Test
	public void T4_V0_OutOfRange() {
		MainController controller = new MainController();
		controller.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		List<TaskAbstract> tasksInRange = controller.getTasksInRange(400,500);
		
		assertEquals(0, tasksInRange.size());
	}
	
	@Test(expected = NullPointerException.class)
	public void T4_V0_NoFile() {
		MainController controller = new MainController();
		controller.getTasksInRange(100,200);
	}
}
