package ex01_lombok;

public class Ex03_Main {
	public static void main(String[] args) {
		Ex03_User user = Ex03_User.builder().id("hong").pw("1234").build();
		System.out.println(user);
		Ex03_User user2 = new Ex03_User("kim","5678");
		System.out.println(user2.getId());
		System.out.println(user2.getPw());
		
	}
}
