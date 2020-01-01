package io.github.hapjava.impl.characteristics.lightbulb;

import io.github.hapjava.HomekitCharacteristicChangeCallback;
import io.github.hapjava.accessories.ColorTemperatureLightBulb;
import io.github.hapjava.characteristics.EventableCharacteristic;
import io.github.hapjava.characteristics.IntegerCharacteristic;
import java.util.concurrent.CompletableFuture;

public class ColorTemperatureCharacteristic extends IntegerCharacteristic
    implements EventableCharacteristic {

  private ColorTemperatureLightBulb lightBulb;

  public ColorTemperatureCharacteristic(ColorTemperatureLightBulb lightBulb) {
    super(
        "000000CE-0000-1000-8000-0026BB765291",
        true,
        true,
        "Adjust temperature of the light",
        50,
        400,
        "");
    this.lightBulb = lightBulb;
  }

  @Override
  protected void setValue(Integer value) throws Exception {
    lightBulb.setTemperature(value);
  }

  @Override
  protected CompletableFuture<Integer> getValue() {
    return lightBulb.getTemperature();
  }

  @Override
  public void subscribe(HomekitCharacteristicChangeCallback callback) {
    lightBulb.subscribeTemperature(callback);
  }

  @Override
  public void unsubscribe() {
    lightBulb.unsubscribeTemperature();
  }
}
