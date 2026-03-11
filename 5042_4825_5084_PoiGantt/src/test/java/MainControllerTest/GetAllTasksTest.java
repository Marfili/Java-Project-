package MainControllerTest;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import dom.gantt.TaskAbstract;
import service.MainController;
import util.FileTypes;

public class GetAllTasksTest {
	@Test
	public void T2_V0_HappyDay() {
		MainController controller = new MainController();
		controller.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		List<TaskAbstract> tasks = controller.getAllTasks();
		
		assertEquals(8, tasks.size());
		assertEquals("TaskId: 	100	Text: 	Prepare a shopping list	Mama: 	0	Start: 	1	End: 	6	Cost: 	60.0	Effort: 	11.0", tasks.get(0).toString());
		assertEquals("TaskId: 	101	Text: 	Visit all the closets to see what's missing	Mama: 	100	Start: 	1	End: 	5	Cost: 	50.0	Effort: 	5.0", tasks.get(1).toString());
		assertEquals("TaskId: 	102	Text: 	Write down what you need to buy	Mama: 	100	Start: 	1	End: 	6	Cost: 	10.0	Effort: 	6.0", tasks.get(2).toString());
		assertEquals("TaskId: 	200	Text: 	Proceed to the super market	Mama: 	0	Start: 	7	End: 	9	Cost: 	10.0	Effort: 	5.0", tasks.get(3).toString());
		assertEquals("TaskId: 	201	Text: 	Drive the car	Mama: 	200	Start: 	7	End: 	9	Cost: 	10.0	Effort: 	5.0", tasks.get(4).toString());
		assertEquals("TaskId: 	300	Text: 	Buy and pay	Mama: 	0	Start: 	9	End: 	28	Cost: 	482.0	Effort: 	13.0", tasks.get(5).toString());
		assertEquals("TaskId: 	307	Text: 	Put stuff in the market basket	Mama: 	300	Start: 	9	End: 	15	Cost: 	40.0	Effort: 	12.0", tasks.get(6).toString());
		assertEquals("TaskId: 	302	Text: 	Pay at the cashier and leave	Mama: 	300	Start: 	15	End: 	28	Cost: 	442.0	Effort: 	1.0", tasks.get(7).toString());
	}
	
	@Test(expected = NullPointerException.class)
	public void T2_V0_NoFile() {
		MainController controller = new MainController();
		controller.getAllTasks();
	}
}
