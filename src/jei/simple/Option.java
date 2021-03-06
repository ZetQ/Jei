package jei.simple;

import jei.Base;
import jei.annotation.NonNull;
import jei.error.NullValueException;
import jei.functional.Producer;
import jei.functional.Supplier;

public final class Option<T> extends Base
{
	private static final Option<?>
		NONE = new Option<>(null);
	
	public static <T> Option<T> none() {
		@SuppressWarnings("unchecked")
		Option<T> none = (Option<T>) Option.NONE;
		return none;
	}
	public static <T> Option<T> some(@NonNull T value) {
		return new Option<>(nonNull(value));
	}
	public static <T> Option<T> wrap(T value) {
		return value == null ? none() : new Option<>(value);
	}
	
	private final T
		value;
	
	private Option(T value) {
		this.value = value;
	}
	
	public T get() throws NullValueException {
		if(this.value == null) {
			throw new NullValueException();
		}
		return this.value;
	}
	public <O> Option<O> map(Producer<? super T, O> producer) {
		if(this.value != null) {
			return some(producer.call(this.value));
		}
		return none();
	}
	public <O> Option<O> flatMap(Producer<? super T, Option<O>> producer) {
		if(this.value != null) {
			return producer.call(this.value);
		}
		return none();
	}
	public boolean isset() {
		return this.value != null;
	}
	public T or(@NonNull T fallback) {
		if(this.value == null) {
			return nonNull(fallback);
		}
		return this.value;
	}
	public T orGet(Supplier<@NonNull T> supplier) {
		if(this.value == null) {
			return nonNull(supplier.call());
		}
		return this.value;
	}
	public <X extends Throwable> T orThrow(X error) throws X {
		if(this.value == null) {
			throw error;
		}
		return this.value;
	}
	public <O> O getOr(Producer<? super @NonNull T, O> producer, Supplier<@NonNull O> fallback) {
		if(this.value == null) {
			return nonNull(fallback.call());
		}
		return nonNull(producer.call(this.value));
	}
	
	@Override
	public boolean equals(Object object) {
		if(this == object) {
			return true;
		}
		if(object instanceof Option) {
			Option<?> optional = (Option<?>) object;
			if(this.isset()) {
				if(optional.isset()) {
					return this.get().equals(optional.get());
				} else {
					return false;
				}
			} else {
				return !optional.isset();
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		return this.isset() ? this.value.hashCode() : 0;
	}
	@Override
	public String toString() {
		return format("({0})?", this.value);
	}
}
