import java.util.LinkedList;
import java.util.Queue;

public class Q5 {
	public static void main(String[] args) {
		String expression = "- + * 9 + 2 8 * + 4 8 6 3";
		Queue<String> queue = new LinkedList<String>();
		
		for (String split : expression.split(" ")) {
			queue.offer(split);
		}

		int size = queue.size(); // which will not affect the size of original queue
		for (;queue.size() != 1;) {
			Queue<String> temp = new LinkedList<String>(); // for next scan

			for (int j = 0; j < size; j++) {
				Queue<String> temp2 = new LinkedList<String>(); // current identification
				String operator = queue.remove();

				if (operator.matches("^[0-9]*$")) {
					temp.offer(operator);

				} else if (operator.matches("^[+\\-*/]$")) {
					String item2 = queue.remove(); // the first digit after operator

					if (!item2.matches("^[0-9]*$")) {
						temp.offer(operator);
						temp2.offer(item2);
						copyQueue(temp2, queue);
						copyQueue(queue, temp2);

					} else {
						String item3 = queue.remove(); // the second digit after operator

						if (!item3.matches("^[0-9]*$")) {
							temp.offer(operator);
							temp2.offer(item2);
							temp2.offer(item3);
							copyQueue(temp2, queue);
							copyQueue(queue, temp2);

						} else {
							int num1 = Integer.parseInt(item2);
							int num2 = Integer.parseInt(item3);
							String result = String.valueOf(calculate(num1, num2, operator));
							temp.offer(result);
							size -= 2;
						}
					}

				} else {
					System.out.println("Error data type!");
					break;
				}
			}
			queue.clear();
			copyQueue(queue, temp);
		}
		System.out.println(queue);
	}

	private static int calculate(int num1, int num2, String operator) {
		int result = 0;

		switch (operator) {
		case "+":
			result = num1 + num2;
			break;

		case "-":
			result = num1 - num2;
			break;

		case "*":
			result = num1 * num2;
			break;

		case "/":
			result = num1 / num2;
			break;
		}
		
		return result;
	}
	
	private static void copyQueue(Queue<String> q1, Queue<String> q2){
		int size = q2.size();
		for (int i = 0; i < size; i++) {
			q1.offer(q2.remove());
		}
	}
}
