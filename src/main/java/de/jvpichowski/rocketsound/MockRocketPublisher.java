package de.jvpichowski.rocketsound;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.jvpichowski.rocketsound.messages.base.*;
import de.jvpichowski.rocketsound.messages.sound.Amplitude;
import de.jvpichowski.rocketsound.messages.sound.Spectrum;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.telestion.api.config.Config;
import org.telestion.core.database.DbResponse;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MockRocketPublisher extends AbstractVerticle {

	private final Configuration forcedConfig;
	private Configuration config;

	public MockRocketPublisher() {
		forcedConfig = null;
	}

	public MockRocketPublisher(String outgoing) {
		forcedConfig = new Configuration(outgoing);
	}

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		config = Config.get(forcedConfig, config(), Configuration.class);

		vertx.setPeriodic(Duration.ofSeconds(1).toMillis(), h -> {
			vertx.eventBus().publish("org.telestion.core.database.MongoDatabaseService/out#save/de.jvpichowski.rocketsound.messages.sound.Amplitude",
			new DbResponse(Amplitude.class, List.of(new Amplitude(Math.random(), Math.random(), Math.random()).json())).json());
			vertx.eventBus().publish("org.telestion.core.database.MongoDatabaseService/out#save/de.jvpichowski.rocketsound.messages.sound.Spectrum",
			new DbResponse(Spectrum.class, List.of(new Spectrum(0, 10, new double[]{
					Math.random(), Math.random(), Math.random(), Math.random(), Math.random(),
					Math.random(), Math.random(), Math.random(), Math.random(), Math.random()}).json())).json());
			vertx.eventBus().publish("org.telestion.core.database.MongoDatabaseService/out#save/de.jvpichowski.rocketsound.messages.base.Velocity",
			new DbResponse(Velocity.class, List.of(new Velocity(Math.random(), Math.random()).json())).json());
			vertx.eventBus().publish("org.telestion.core.database.MongoDatabaseService/out#save/de.jvpichowski.rocketsound.messages.base.NineDofData",
			new DbResponse(NineDofData.class, List.of(new NineDofData(
					new Accelerometer(Math.random(), Math.random(), Math.random()),
					new Gyroscope(Math.random(), Math.random(), Math.random()),
					new Magnetometer(Math.random(), Math.random(), Math.random())
			).json())).json());
			vertx.eventBus().publish("org.telestion.core.database.MongoDatabaseService/out#save/de.jvpichowski.rocketsound.messages.base.BaroData",
			new DbResponse(BaroData.class, List.of(new BaroData(
					new Pressure(Math.random()),
					new Temperature(Math.random()),
					new Altitude(Math.random())
			).json())).json());
			vertx.eventBus().publish("org.telestion.core.database.MongoDatabaseService/out#save/de.jvpichowski.rocketsound.messages.base.GpsData",
			new DbResponse(GpsData.class, List.of(new GpsData(1, false, 49.70518+Math.random()*0.002, 9.89143+Math.random()*0.002, System.currentTimeMillis()).json())).json());
			var stateIdx = (int)(Math.random()*6);
			vertx.eventBus().publish("org.telestion.core.database.MongoDatabaseService/out#save/de.jvpichowski.rocketsound.messages.base.FlightState",
			new DbResponse(FlightState.class, List.of(new FlightState(stateIdx,
					new String[]{"-","preparation", "flight", "apogee", "landing", "recovery"}[stateIdx]).json())).json());

/*
			//vertx.eventBus().publish(config.address, new Amplitude(3.7).json());
			//vertx.eventBus().publish(config.address,
			//		new Spectrum(2.7, 1004.3, new double[]{2.6, 0.0, 3.5, 100, 980.5}).json());
			//vertx.eventBus().publish(config.address,
			//		new GpsData(3, 7, 4343345.0, -376.322, 42134894).json());
			vertx.eventBus().publish(config.address, new DbResponse(NineDofData.class, Arrays.asList(new NineDofData(
					new Accelerometer(Math.random(), Math.random(), Math.random()),
					new Gyroscope(Math.random(), Math.random(), Math.random()),
					new Magnetometer(Math.random(), Math.random(), Math.random())).json())).json());
			//vertx.eventBus().publish(config.address, new BaroData(
			//		new Pressure(67773.3),
			//		new Temperature(24.3),
			//		new Altitude(287.0)).json());

 */
		});
		startPromise.complete();
	}

	private static record Configuration(@JsonProperty String address) {
		@SuppressWarnings("unused")
		private Configuration() {
			this("Outgoing");
		}
	}
}
