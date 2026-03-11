package MainControllerTest;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import dom.gantt.TaskAbstract;
import service.MainController;
import util.FileTypes;

public class GetTopLevelTasksOnlyTest {
	@Test
	public void T3_V0_HappyDay() {
		MainController controller = new MainController();
		controller.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		List<TaskAbstract> topTasks = controller.getTopLevelTasksOnly();
		
		assertEquals(3, topTasks.size());
		assertEquals("TaskId: 	100	Text: 	Prepare a shopping list	Mama: 	0	Start: 	1	End: 	6	Cost: 	60.0	Effort: 	11.0", topTasks.get(0).toString());
		assertEquals("TaskId: 	200	Text: 	Proceed to the super market	Mama: 	0	Start: 	7	End: 	9	Cost: 	10.0	Effort: 	5.0", topTasks.get(1).toString());
		assertEquals("TaskId: 	300	Text: 	Buy and pay	Mama: 	0	Start: 	9	End: 	28	Cost: 	482.0	Effort: 	13.0", topTasks.get(2).toString());
	}
	
	@Test(expected = NullPointerException.class)
	public void T3_V0_NoFile() {
		MainController controller = new MainController();
		controller.getTopLevelTasksOnly();
	}
}
