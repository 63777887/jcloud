package com.jwk.test.jvm;

import static java.lang.invoke.MethodHandles.lookup;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import org.springframework.stereotype.Service;

@Service
public class MethodHandleTest {

	public static void main(String[] args) throws Throwable {
		Object obj = System.currentTimeMillis() % 2 == 0 ? System.out : new ClassA();
		// 无论obj最终是哪个实现类，下面这句都能正确调用到println方法。
		getPrintlnMH(obj).invokeExact("icyfenix");
		MethodType mt = MethodType.methodType(void.class, String.class);
		lookup().findVirtual(ClassA.class, "show", mt).bindTo(new ClassA()).invokeExact("test");
	}

	private static MethodHandle getPrintlnMH(Object reveiver) throws Throwable {
		// MethodType：代表“方法类型”，包含了方法的返回值（methodType()的第一个参数）和
		// 具体参数（methodType()第二个及以后的参数）。
		MethodType mt = MethodType.methodType(void.class, String.class);
		// lookup()方法来自于MethodHandles.lookup，这句的作用是在指定类中查找符合给定的方法
		// 名称、方法类型，并且符合调用权限的方法句柄。
		// 因为这里调用的是一个虚方法，按照Java语言的规则，方法第一个参数是隐式的，代表该方法的接
		// 收者，也即this指向的对象，这个参数以前是放在参数列表中进行传递，现在提供了bindTo()
		// 方法来完成这件事情。
		return lookup().findVirtual(reveiver.getClass(), "println", mt).bindTo(reveiver);
	}

	private boolean testOne(boolean bool) {
		return bool;
	}

	public Integer testTwo(boolean bool) {

		return testOne(bool) ? 1 : 2;
	}

	static class ClassA {

		public void println(String s) {
			System.out.println(s);
		}

		public void show(String s) {
			System.out.println(s);
		}

	}

}