package me.mortaldev.jbutils.utils;

/**
 * A class that holds a supplier of data and a runnable that can be
 * executed when the data is requested. This is useful for creating
 * lazy-executed code that is associated with some data.
 *
 * @param <T> the type of data that is held by the supplier
 */
public class SupplierRunnable<T> {
  private final T data;
  private final Runnable runnable;

  public SupplierRunnable(T data, Runnable runnable) {
    this.data = data;
    this.runnable = runnable;
  }

  /**
   * Gets the data that is held by this supplier runnable.
   *
   * @return the data that is held by this supplier runnable
   */
  public T get() {
    return data;
  }

  /**
   * Executes the runnable associated with this supplier runnable.
   *
   * @see Runnable#run()
   */
  public void run() {
    runnable.run();
  }


}