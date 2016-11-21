package code.elix_x.excomms.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class ReflectionHelper {

	private static final Field fieldConstructorModifiers;
	private static final Field fieldFieldModifiers;
	private static final Field fieldMethodModifiers;

	static{
		try{
			fieldConstructorModifiers = Constructor.class.getDeclaredField("modifiers");
			fieldConstructorModifiers.setAccessible(true);
			fieldFieldModifiers = Field.class.getDeclaredField("modifiers");
			fieldFieldModifiers.setAccessible(true);
			fieldMethodModifiers = Method.class.getDeclaredField("modifiers");
			fieldMethodModifiers.setAccessible(true);
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public static Field findField(Class<?> claz, String... names){
		Exception e = null;
		Class<?> clas = claz;
		while(claz != null){
			for(String name : names){
				try{
					return clas.getDeclaredField(name);
				} catch(Exception ee){
					e = ee;
				}
			}
			clas = claz.getSuperclass();
		}
		throw new IllegalArgumentException(e);
	}

	public static Method findMethod(Class<?> claz, String[] names, Class<?>... args){
		Exception e = null;
		Class<?> clas = claz;
		while(claz != null){
			for(String name : names){
				try{
					return clas.getDeclaredMethod(name, args);
				} catch(Exception ee){
					e = ee;
				}
			}
			clas = claz.getSuperclass();
		}
		throw new IllegalArgumentException(e);
	}

	public static enum Modifier {

		PUBLIC, PRIVATE, PROTECTED, STATIC, FINAL, SYNCHRONIZED, VOLATILE, TRANSIENT, NATIVE, INTERFACE, ABSTRACT, STRICT, BRIDGE, VARARGS, SYNTHETIC, ANNOTATION, ENUM, MANDATED;

		final int modifier;

		private Modifier(){
			modifier = (int) new AClass<java.lang.reflect.Modifier>(java.lang.reflect.Modifier.class).getDeclaredField(this.name()).setAccessible(true).get(null);
		}

		private boolean is(int original){
			return (original & modifier) != 0;
		}

		private int set(int original, boolean on){
			if(on){
				return original | modifier;
			} else{
				return original & (~modifier);
			}
		}

	}

	public static class AClass<C> {

		protected final Class<C> clas;

		public AClass(Class<C> clas){
			this.clas = clas;
		}

		public AClass(String clas){
			try{
				this.clas = (Class<C>) Class.forName(clas);
			} catch(Exception e){
				throw new RuntimeException(e);
			}
		}

		public Optional<AClass<? super C>> getSuperclass(){
			return clas.getSuperclass() != null ? Optional.of(new AClass<>(clas.getSuperclass())) : Optional.empty();
		}

		public boolean isInterface(){
			return clas.isInterface();
		}

		public AInterface<C> asInterface(){
			return new AInterface<>(clas);
		}

		public boolean isEnum(){
			return clas.isEnum();
		}

		public <E extends Enum<E>> AEnum<E> asEnum(){
			return new AEnum<E>((Class<E>) clas);
		}

		public boolean isAnnotation(){
			return clas.isAnnotation();
		}

		public AAnnotation<C> asAnnotation(){
			return new AAnnotation<>(clas);
		}

		public AConstructor<C> getDeclaredConstructor(Class<?>... args){
			try{
				return new AConstructor<C>(this, clas.getDeclaredConstructor(args));
			} catch(ReflectiveOperationException e){
				throw new RuntimeException(e);
			}
		}

		public List<AConstructor<C>> getDeclaredConstructors(){
			return Lists.transform(Arrays.asList(clas.getDeclaredConstructors()), constructor -> new AConstructor<>(this, (Constructor<C>) constructor));
		}

		public <T> AField<C, T> getDeclaredField(String... names){
			return new AField<C, T>(this, findField(clas, names));
		}

		public List<AField<C, ?>> getDeclaredFields(){
			return Lists.transform(Arrays.asList(clas.getDeclaredFields()), field -> new AField<>(this, field));
		}

		@SuppressWarnings("unchecked")
		public List<AField<? super C, ?>> getFields(){
			List sup = new ArrayList<>();
			getSuperclass().ifPresent(clas -> sup.addAll(clas.getFields()));
			sup.addAll(getDeclaredFields());
			return sup;
		}

		public <T> AMethod<C, T> getDeclaredMethod(String[] names, Class<?>... args){
			return new AMethod<C, T>(this, findMethod(clas, names, args));
		}

		public List<AMethod<C, ?>> getDeclaredMethods(){
			return Lists.transform(Arrays.asList(clas.getDeclaredMethods()), method -> new AMethod<>(this, method));
		}

		@SuppressWarnings("unchecked")
		public List<AMethod<? super C, ?>> getMethods(){
			List sup = new ArrayList<>();
			getSuperclass().ifPresent(clas -> sup.addAll(clas.getMethods()));
			sup.addAll(getDeclaredMethods());
			return sup;
		}

		public static class AInterface<C> extends AClass<C> {

			private AInterface(Class<C> clas){
				super(clas);
			}

		}

		public static class AEnum<C extends Enum<C>> extends AClass<C> {

			private AEnum(Class<C> clas){
				super(clas);
			}

			public C getEnum(int ordinal){
				return clas.getEnumConstants()[ordinal];
			}

			public C getEnum(String name){
				return Enum.valueOf(clas, name);
			}

		}

		public static class AAnnotation<C> extends AClass<C> {

			private AAnnotation(Class<C> clas){
				super(clas);
			}

		}

	}

	private static abstract class ReflectionObject<C, T, R extends ReflectionObject<C, T, R>> {

		private final AClass<C> clas;
		private final T t;

		private ReflectionObject(AClass<C> clas, T t){
			this.clas = clas;
			this.t = t;
		}

		public AClass<C> clas(){
			return clas;
		}

		public final T get(){
			return t;
		}
		
		public List<Modifier> modifiers(){
			return Lists.newArrayList(Modifier.values()).stream().filter(modifier -> is(modifier)).collect(Collectors.toList());
		}

		public abstract boolean is(Modifier modifier);

		public abstract R set(Modifier modifier, boolean on);

		public abstract boolean isAccessible();

		public abstract R setAccessible(boolean accessible);

	}

	private static abstract class AccessibleReflectionObject<C, T extends AccessibleObject, R extends AccessibleReflectionObject<C, T, R>> extends ReflectionObject<C, T, R> {

		private AccessibleReflectionObject(AClass<C> clas, T t){
			super(clas, t);
		}

		@Override
		public boolean isAccessible(){
			return get().isAccessible();
		}

		@Override
		public R setAccessible(boolean accessible){
			get().setAccessible(accessible);
			return (R) this;
		}

	}

	public static class AConstructor<C> extends AccessibleReflectionObject<C, Constructor<C>, AConstructor<C>> {

		private AConstructor(AClass<C> clas, Constructor<C> constructor){
			super(clas, constructor);
		}

		@Override
		public boolean is(Modifier modifier){
			return modifier.is(get().getModifiers());
		}

		@Override
		public AConstructor<C> set(Modifier modifier, boolean on){
			try{
				fieldConstructorModifiers.setInt(get(), modifier.set(get().getModifiers(), on));
			} catch(Exception e){
				throw new RuntimeException(e);
			}
			return this;
		}

		public C newInstance(Object... args){
			try{
				return get().newInstance(args);
			} catch(Exception e){
				throw new RuntimeException(e);
			}
		}

	}

	public static class AField<C, T> extends AccessibleReflectionObject<C, Field, AField<C, T>> {

		private AField(AClass<C> clas, Field field){
			super(clas, field);
		}

		@Override
		public boolean is(Modifier modifier){
			return modifier.is(get().getModifiers());
		}

		@Override
		public AField<C, T> set(Modifier modifier, boolean on){
			try{
				fieldFieldModifiers.setInt(get(), modifier.set(get().getModifiers(), on));
			} catch(Exception e){
				throw new RuntimeException(e);
			}
			return this;
		}

		public AField<C, T> setFinal(boolean finall){
			return set(Modifier.FINAL, finall);
		}

		public <I extends C> T get(I instance){
			try{
				return (T) get().get(instance);
			} catch(Exception e){
				throw new RuntimeException(e);
			}
		}

		public <I extends C> void set(I instance, Object t){
			try{
				get().set(instance, t);
			} catch(Exception e){
				throw new RuntimeException(e);
			}
		}

	}

	public static class AMethod<C, T> extends AccessibleReflectionObject<C, Method, AMethod<C, T>> {

		private AMethod(AClass<C> clas, Method method){
			super(clas, method);
		}

		@Override
		public boolean is(Modifier modifier){
			return modifier.is(get().getModifiers());
		}

		@Override
		public AMethod<C, T> set(Modifier modifier, boolean on){
			try{
				fieldMethodModifiers.setInt(get(), modifier.set(get().getModifiers(), on));
			} catch(Exception e){
				throw new RuntimeException(e);
			}
			return this;
		}

		public <I extends C> T invoke(I instance, Object... args){
			try{
				return (T) get().invoke(instance, args);
			} catch(Exception e){
				throw new RuntimeException(e);
			}
		}

	}

}
