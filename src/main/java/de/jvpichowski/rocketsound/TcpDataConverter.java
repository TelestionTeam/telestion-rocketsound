package de.jvpichowski.rocketsound;

import de.jvpichowski.rocketsound.messages.base.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.telestion.api.message.JsonMessage;
import org.telestion.core.connection.TcpConn;
import org.telestion.core.database.DbResponse;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.logging.Logger;

public class TcpDataConverter extends AbstractVerticle {

	private static final Logger logger = Logger.getLogger(TcpDataConverter.class.getName());

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		logger.info("TcpDataConv started");

		getVertx().eventBus().consumer("tcpIncoming", h -> {
			JsonMessage.on(TcpConn.Data.class, h, data -> {
				System.out.println(Arrays.toString(data.data()));
				convertRawData(data.data());
				convertGpsData(data.data());
				//getVertx().eventBus().publish("tcpOutgoing", data.json());
			});
		});

		startPromise.complete();
	}

	private void convertGpsData(byte[] data){
		if(data.length != 16){
			return;
		}
		System.out.println("GPS Received");
		ByteBuffer buffer = ByteBuffer.wrap(data);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		long millis = buffer.getShort();
		float latitude = buffer.getFloat();
		float longitude = buffer.getFloat();
		boolean fix = buffer.get() != 0;
		byte satCount = buffer.get();

		vertx.eventBus().publish( "org.telestion.core.database.MongoDatabaseService/out#save/de.jvpichowski.rocketsound.messages.base.GpsData",
				new DbResponse(GpsData.class, Arrays.asList(new GpsData(
								satCount, fix, latitude, longitude, millis).json())).json());

	}

	private void convertRawData(byte[] data){
		if(data.length != 56){
			return;
		}
		System.out.println("Raw Received");
		ByteBuffer buffer = ByteBuffer.wrap(data);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		long time = buffer.getLong();
		float magX = buffer.getFloat();
		float magY = buffer.getFloat();
		float magZ = buffer.getFloat();
		float gyroX = buffer.getFloat();
		float gyroY = buffer.getFloat();
		float gyroZ = buffer.getFloat();
		float accX = buffer.getFloat();
		float accY = buffer.getFloat();
		float accZ = buffer.getFloat();
		float press = buffer.getFloat();
		float alt = buffer.getFloat();
		float temp = buffer.getFloat();

		if(press > 1 && temp > 1 && alt > 1) {
			BaroData baro = new BaroData(
					new Pressure(press),
					new Temperature(temp),
					new Altitude(alt));

			//new FlightState();
			//new GpsData();
			////new Position() (Nicht wichtig)

			vertx.eventBus().publish("org.telestion.core.database.MongoDatabaseService/out#save/de.jvpichowski.rocketsound.messages.base.BaroData",
					new DbResponse(BaroData.class, Arrays.asList(baro.json())).json());
		}

		vertx.eventBus().publish( "org.telestion.core.database.MongoDatabaseService/out#save/de.jvpichowski.rocketsound.messages.base.NineDofData",
				new DbResponse(NineDofData.class, Arrays.asList(new NineDofData(
				new Accelerometer(accX, accY, accZ),
				new Gyroscope(gyroX, gyroY, gyroZ),
				new Magnetometer(magX, magY, magZ)).json())).json());
	}
}
