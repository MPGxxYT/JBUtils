package me.mortaldev.jbutils.utils;

public class SupplierRunnable<T> {
  private final T data;
  private final Runnable runnable;

  public SupplierRunnable(T data, Runnable runnable) {
    this.data = data;
    this.runnable = runnable;
  }

  public T get() {
    return data;
  }

  public void run() {
    runnable.run();
  }


}