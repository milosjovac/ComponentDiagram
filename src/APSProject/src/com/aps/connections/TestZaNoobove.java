package com.aps.connections;

public class TestZaNoobove {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Animal start = new Dog();
		Animal end = new Cat();
		
		Animal pomS = start;
		start = end;
		
		pomS.makeSound();

	}

	
}

class Dog implements Animal {

	public Dog() {

	}

	@Override
	public String makeSound() {
		System.out.println("av");
		return null;
	}

}

 class Cat implements Animal {

	@Override
	public String makeSound() {
		System.out.println("mjau");
		return null;
	}

}

 interface Animal {
	String makeSound();
}
