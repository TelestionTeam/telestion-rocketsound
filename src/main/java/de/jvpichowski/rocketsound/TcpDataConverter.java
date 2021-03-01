package de.jvpichowski.rocketsound;

import de.jvpichowski.rocketsound.messages.base.Accelerometer;
import de.jvpichowski.rocketsound.messages.base.Gyroscope;
import de.jvpichowski.rocketsound.messages.base.Magnetometer;
import de.jvpichowski.rocketsound.messages.base.NineDofData;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.telestion.api.message.JsonMessage;
import org.telestion.core.connection.TcpConn;
import org.telestion.core.database.DbResponse;

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
				convertData(data.data());
				getVertx().eventBus().publish("tcpOutgoing", data.json());
			});
		});

		startPromise.complete();
	}

	private void convertData(byte[] data){
		if(data.length != 9){
			return;
		}
		vertx.eventBus().publish( "org.telestion.core.database.MongoDatabaseService/out#save/de.jvpichowski.rocketsound.messages.base.NineDofData",
				new DbResponse(NineDofData.class, Arrays.asList(new NineDofData(
				new Accelerometer(data[0], data[1], data[2]),
				new Gyroscope(data[3], data[4], data[5]),
				new Magnetometer(data[6], data[7], data[8])).json())).json());
	}
}
