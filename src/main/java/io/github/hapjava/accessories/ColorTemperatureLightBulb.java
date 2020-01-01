package io.github.hapjava.accessories;

import io.github.hapjava.HomekitCharacteristicChangeCallback;
import java.util.concurrent.CompletableFuture;

public interface ColorTemperatureLightBulb extends Lightbulb {

  /**
   * Retrieves the current color temperature of the light Color temperature which is represented in
   * reciprocal megaKelvin (MK-1) or mirek scale. (M = 1,000,000 / K where M is the desired mirek
   * value and K is temperature in Kelvin)
   *
   * @return a future that will contain the color temperature, expressed as an integer between 50
   *     and 400.
   */
  CompletableFuture<Integer> getTemperature();

  /**
   * Sets the current color temperature of the light
   *
   * @param value the temperature, on a scale of 50 to 400, to set
   * @return a future that completes when the temperature is changed
   * @throws Exception when the temperature cannot be set
   */
  CompletableFuture<Void> setTemperature(Integer value) throws Exception;

  /**
   * Subscribes to changes in the temperature of the light.
   *
   * @param callback the function to call when the state changes.
   */
  void subscribeTemperature(HomekitCharacteristicChangeCallback callback);

  /** Unsubscribes from changes in the temperature of the light. */
  void unsubscribeTemperature();
}
